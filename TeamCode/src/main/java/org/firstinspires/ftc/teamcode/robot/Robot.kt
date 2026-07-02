package org.firstinspires.ftc.teamcode.robot

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import com.pedropathing.ftc.localization.Encoder
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.behaviors.EndCondition
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.commands.Commands.waitUntil
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.AnalogSensor
import com.qualcomm.robotcore.hardware.Servo

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

        // Shooter
        val motorShooterLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorShooterRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        motorShooterLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorShooterLeft.direction = DcMotorSimple.Direction.FORWARD
        motorShooterLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motorShooterRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motorShooterRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motorShooterRight.direction = DcMotorSimple.Direction.REVERSE
        motorShooterRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val servoTurret1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servoTurret2 = hardwareMap.get(Servo::class.java, "turretServo2")

        val servoHood = hardwareMap.get(Servo::class.java, "servoHood")
        servoHood.scaleRange(0.2394, 0.83)

        val encoderOuttake = Encoder(hardwareMap.get(DcMotorEx::class.java, "motorLF"))
        encoderOuttake.setDirection(Encoder.REVERSE)
        val voltageSensor = hardwareMap.voltageSensor.iterator().next()

        // Intake
        val motorIntake = hardwareMap.get(DcMotorEx::class.java, "motorIntake")

        // Transfer
        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
        val finger = hardwareMap.get(Servo::class.java, "finger")
        finger.scaleRange(0.3844, 0.67)
        val distanceSensor = hardwareMap.get(AnalogInput::class.java, "distanceSensor")

        drive = Drive(follower)
        shooter = Shooter(
            motorLeft = motorShooterLeft,
            motorRight = motorShooterRight,
            servo1 = servoTurret1,
            servo2 = servoTurret2,
            finger = finger,
            hood = servoHood,
            voltageSensor = voltageSensor
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

    val allStartCommand : Command = sequential(
        intake.startIntakeCommand,
        transfer.startTransferCommand
    )

    fun intakeBalls(time: Double = 50.0): Command = sequential(
        allStartCommand,
        waitUntil { transfer.isBallPresent() }, // wait for sensor
        transfer.stopTransferCommand,
        waitMs(time),
        intake.stopIntakeCommand
    ).setEnd { endCondition ->
        if (endCondition == EndCondition.INTERRUPTED) {
            intake.power = 0.0
            transfer.power = 0.0
        }
    }

    fun shootBalls(rpm : Double = Shooter.ShooterConfig.rpmFar) : Command = sequential(
        shooter.goToRpmCommand(rpm),
        shooter.openFingerCommand,
        allStartCommand,
        waitMs(300.0),
        intake.stopIntakeCommand,
        waitMs(700.0),
        transfer.stopTransferCommand,
        shooter.closeFingerCommand,
        shooter.goToRpmCommand(Shooter.ShooterConfig.rpmRest)
    )

}