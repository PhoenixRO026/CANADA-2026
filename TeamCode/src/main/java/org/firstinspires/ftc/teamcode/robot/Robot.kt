package org.firstinspires.ftc.teamcode.robot

import com.commonlibs.units.degToRad
import com.commonlibs.units.mmToInch
import com.commonlibs.units.rad
import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.commands.Commands.waitUntil
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.groups.Groups.race
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.Servo
import java.lang.Math.pow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

class Robot(
    hardwareMap: HardwareMap,
    pose: Pose = Pose(0.0, 0.0, Math.toRadians(0.0))
) {
    val follower : Follower = Constants.createFollower(hardwareMap)
    val drive : Drive
    val intake : Intake
    val shooter: Shooter
    val transfer: Transfer
    val limelight : LimeLightCore

    init {
        follower.setStartingPose(pose)
        follower.update()

        val camera = hardwareMap.get(Limelight3A::class.java, "limelight")
        camera.setPollRateHz(60)
        camera.start()
        val pinpoint = hardwareMap.get(GoBildaPinpointDriver::class.java, "odo")

        // Shooter
        val motorShooterLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorShooterRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        motorShooterLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorShooterLeft.direction = DcMotorSimple.Direction.REVERSE
        motorShooterLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motorShooterRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorShooterRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorShooterRight.direction = DcMotorSimple.Direction.FORWARD
        motorShooterRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val servoTurret1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servoTurret2 = hardwareMap.get(Servo::class.java, "turretServo2")

        val servoHood = hardwareMap.get(Servo::class.java, "servoHood")

        val motorRB = hardwareMap.get(DcMotorEx::class.java, "motorRB")
        val voltageSensor = hardwareMap.voltageSensor.iterator().next()

        // Intake
        val motorIntake = hardwareMap.get(DcMotorEx::class.java, "motorIntake")

        // Transfer
        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
        val finger = hardwareMap.get(Servo::class.java, "finger")
        finger.scaleRange(0.4, 0.609)
        val distanceSensor = hardwareMap.get(AnalogInput::class.java, "distanceSensor")

        drive = Drive(follower)
        shooter = Shooter(
            motorLeft = motorShooterLeft,
            motorRight = motorShooterRight,
            servo1 = servoTurret1,
            servo2 = servoTurret2,
            finger = finger,
            hood = servoHood,
            voltageSensor = voltageSensor,
            motorEncoder = motorRB,
            )
        transfer = Transfer(
            motor = motorTransfer,
            distanceSensor = distanceSensor
        )
        intake = Intake(
            motor = motorIntake
        )
        limelight = LimeLightCore(
            camera = camera
        )
    }

    enum class Side { RED, BLUE}

    var BlueGoal = Pose(10.0, 135.0)
    var RedGoal = Pose(136.5, 135.0)

    fun distanceFromGoal(side : Side) : Double {
        val goal : Pose

        if (side == Side.RED)
            goal = RedGoal
        else
            goal = BlueGoal

        val d = sqrt((follower.pose.x - goal.x).pow(2) + (follower.pose.y - goal.y).pow(2))
        drive.distanceFromGoal = d
        return d
    }

    fun neededTurretAngle(side : Side) : Double {
        val goal : Pose

        if (side == Side.RED)
            goal = RedGoal
        else
            goal = BlueGoal


        val robotHeading = Math.toDegrees(follower.pose.heading)


        val normalAngle = 180.0 * robotHeading.sign * (-1) + robotHeading
        val offset = 67.026.mmToInch()

        val newRobotPose = Pose(follower.pose.x + offset * cos(normalAngle.degToRad()), follower.pose.y + offset * sin(normalAngle.degToRad()), robotHeading)

        val dx = goal.x - newRobotPose.x
        val dy = goal.y - newRobotPose.y

        val robotAngle = Math.toDegrees(atan2(dy, dx))

        var turretAngle = robotAngle - robotHeading

        // Normalize to [-180, 180]
        while (turretAngle > 180.0)
            turretAngle -= 360.0

        while (turretAngle < -180.0)
            turretAngle += 360.0

        return -turretAngle
    }

    fun updateHeading(side: Side) {
        val angle = neededTurretAngle(side)

        if (!angle.isFinite()) {
            return
        }

        shooter.turretGoToAngle(angle.coerceIn(-90.0, 90.0))
    }

    fun allStartCommand() : Command = parallel(
        intake.startIntakeCommand(),
        transfer.startTransferCommand()
    )

    fun allStopCommand() : Command = parallel(
        intake.stopIntakeCommand(),
        transfer.stopTransferCommand()
    )

    fun intakeBalls(time: Double = 5000.0): Command =
        sequential (
            shooter.closeFingerCommand(),
            intake.startIntakeCommand(),
            transfer.startTransferCommand(),
            race (
                waitUntil { transfer.isBallPresent() },
                waitMs(1000.0)
            ),
            transfer.slowTransferCommand(),
    )

    fun goToRpmAndAngleCommand(dist : Double) : Command = sequential(
        instant { shooter.autoRpm = shooter.neededRpm(dist) },
        instant { shooter.autoAngle = shooter.neededAngle(dist) },
        shooter.goToAutoRpmCommand(),
        shooter.goToAutoAngleCommand()
    )

    var shootMany : Command = sequential(
        shooter.goToRpmCommand(Shooter.ShooterConfig.rpmFar),
        shooter.openFingerCommand(),
        allStartCommand(),
        waitMs(9000.0),
        allStopCommand(),
        shooter.closeFingerCommand(),
        shooter.goToRpmCommand(Shooter.ShooterConfig.rpmRest)
    )

    fun shootBalls(rpm : Double = Shooter.ShooterConfig.rpmFar, hoodPos : Double = Shooter.ShooterConfig.hoodDown) : Command = sequential(
        parallel(
            shooter.goToRpmCommand(rpm),
            shooter.openFingerCommand(),
            shooter.hoodToPositionCommand(hoodPos)
        ),
        allStartCommand(),
        waitMs(200.0),
        intake.stopIntakeCommand(),
        waitMs(400.0),
        transfer.stopTransferCommand(),
        shooter.closeFingerCommand(),
        shooter.goToRpmCommand(Shooter.ShooterConfig.rpmRest),
    )

    fun shootBallsAuto() : Command = sequential(
        parallel(
            shooter.goToRpmCommand(shooter.autoRpm),
            shooter.openFingerCommand(),
            shooter.hoodToPositionCommand(shooter.autoAngle)
        ),
        waitMs(200.0),
        allStartCommand(),
        waitMs(200.0),
        intake.stopIntakeCommand(),
        waitMs(400.0),
        transfer.stopTransferCommand(),
        shooter.closeFingerCommand(),
        shooter.goToRpmCommand(Shooter.ShooterConfig.rpmRest)
    )

    fun ejectBalls() : Command = sequential(
        intake.reverseIntakeCommand(),
        transfer.reverseTransferCommand(),
        waitMs(500.0),
        transfer.stopTransferCommand(),
        intake.stopIntakeCommand()
    )
}