package org.firstinspires.ftc.teamcode.teleop

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.commands.Commands.waitUntil
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.robot.Robot
import kotlin.compareTo
import kotlin.text.toDouble
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.library.buttons.ToggleButtonReader

@TeleOp
open class Data : LinearOpMode() {
    open val pipeline : Int = 1

    @Configurable
    object DataConfig {
        @JvmField var rpm = 0.0
        @JvmField var hoodAngle = 0.0
    }

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        robot.limelight.setPipeline(pipeline)
        val intakeBalls = ButtonReader { gamepad1.dpad_right }
        val stopIntake = ButtonReader { gamepad1.dpad_down }
        val shootBalls = ButtonReader { gamepad1.dpad_up }
        val buttons = listOf(intakeBalls, stopIntake, shootBalls)
        val timeKeep = TimeKeep()

        waitForStart()

        robot.follower.startTeleopDrive()
        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(0.5)
        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }

            timeKeep.resetDeltaTime()

            robot.follower.update()
            robot.limelight.updateHeadingError()
            robot.limelight.updateDistance()

            if (gamepad1.left_trigger >= 0.2) {
                robot.drive.isSlowMode = true
            }
            else {
                robot.drive.isSlowMode = false
            }

            robot.drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )
            if (gamepad1.y) {
                robot.drive.resetFieldCentric()
            }

            if (gamepad2.right_trigger > 0) {
                robot.shooter.turretPosition += 0.1 * timeKeep.deltaTime.asS
            }
            if (gamepad2.left_trigger > 0) {
                robot.shooter.turretPosition -= 0.1 * timeKeep.deltaTime.asS
            }

            if (intakeBalls.wasJustPressed()) {
                robot.intakeBalls().schedule()
            }
            if (shootBalls.wasJustPressed()) {
                robot.shootBalls(DataConfig.rpm, DataConfig.hoodAngle).schedule()
            }
            if (stopIntake.wasJustPressed()) {
                robot.allStopCommand().schedule()
            }
            robot.shooter.updateRpm(timeKeep.deltaTime)
            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("target rpm", robot.shooter.targetRpm)
            panelsTelemetry.addData("shooter power", robot.shooter.shooterPower)
            panelsTelemetry.addData("hood angle", robot.shooter.hoodPosition)
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("turret position", robot.shooter.servo1.position)
            panelsTelemetry.addData("distance", robot.limelight.aprilTagDistance)
            panelsTelemetry.update(telemetry)
        }
    }
}