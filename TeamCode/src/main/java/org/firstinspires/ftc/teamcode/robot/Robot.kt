package org.firstinspires.ftc.teamcode.robot

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import com.pedropathing.ftc.localization.Encoder
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.sequential

class Robot(
    hardwareMap: HardwareMap,
    pose: Pose = Pose(0.0, 0.0, Math.toRadians(0.0))
) {
    val follower : Follower = Constants.createFollower(hardwareMap)
    val drive : Drive
    val intake : Intake
    val shooter: Shooter
    val transfer: Transfer

    init {
        follower.setStartingPose(pose)
        follower.update()

        // Shooter
//        val motorShooterTop = hardwareMap.get(DcMotorEx::class.java, "motorShooterTop")
//        val motorShooterBottom = hardwareMap.get(DcMotorEx::class.java, "motorShooterBottom")
//        val motorTurret = hardwareMap.get(DcMotorEx::class.java, "motorTurret")
//
//        motorShooterTop.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        motorShooterTop.direction = DcMotorSimple.Direction.REVERSE
//        motorShooterTop.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//
//        motorShooterBottom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        motorShooterBottom.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        motorShooterBottom.direction = DcMotorSimple.Direction.FORWARD
//        motorShooterBottom.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//
//        motorTurret.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        motorTurret.direction = DcMotorSimple.Direction.REVERSE
//        motorTurret.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        // Shooter
//        val encoderOuttake = Encoder(hardwareMap.get(DcMotorEx::class.java, "motorLF"))
//        val encoderTurret = Encoder(hardwareMap.get(DcMotorEx::class.java, "motorRB"))
//
//        encoderOuttake.setDirection(Encoder.REVERSE)
//        encoderTurret.setDirection(Encoder.REVERSE)

        // Intake
        val motorIntake = hardwareMap.get(DcMotorEx::class.java, "motorIntake")

        // Transfer
        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
//        val finger = hardwareMap.get(Servo::class.java, "finger


        drive = Drive(follower)
        shooter = Shooter(

            )
        transfer = Transfer(
            motor = motorTransfer,
        )
        intake = Intake(
            motor = motorIntake
        )
    }
    val intakeBalls : Command = sequential(
        intake.startIntakeCommand,
        waitMs(1000.0),
        intake.stopIntakeCommand,
    )

}