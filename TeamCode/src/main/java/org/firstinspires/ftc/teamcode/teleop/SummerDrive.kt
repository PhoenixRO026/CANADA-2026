package org.firstinspires.ftc.teamcode.teleop

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
open class SummerDrive : LinearOpMode() {
    open val pipeline : Int = 1

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        robot.limelight.setPipeline(pipeline)

        val intakeBalls = ButtonReader { gamepad1.dpad_right }
        val ejectBalls = ButtonReader { gamepad1.dpad_left }
        val shootBalls = ButtonReader { gamepad1.dpad_up }
        val stopIntake = ButtonReader { gamepad1.dpad_down}
        val rpmToRest = ButtonReader { gamepad1.left_bumper }
        val startShooter = ButtonReader {gamepad1.right_bumper}
        val turretGoRight = ButtonReader { gamepad1.right_trigger_pressed }
        val turretGoLeft = ButtonReader { gamepad1.right_trigger_pressed }
        val buttons = listOf(intakeBalls, ejectBalls, shootBalls, rpmToRest, stopIntake, startShooter, turretGoLeft, turretGoRight)
        val timeKeep = TimeKeep()

        waitForStart()

        var driverCommand : Command? = null

        robot.follower.startTeleopDrive()
        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(0.5)
//        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }
            timeKeep.resetDeltaTime()

            robot.follower.update()

            //-----------------------
            // debug zone
            //-----------------------

            if (gamepad1.x) {
                val intake : Command = sequential (
                    robot.shooter.closeFingerCommand(),
                    robot.intake.startIntakeCommand(),
                    robot.transfer.startTransferCommand(),
                    waitUntil { robot.transfer.isBallPresent() },
                    robot.transfer.stopTransferCommand(),
                    robot.intake.stopIntakeCommand(),
                    robot.shooter.openFingerCommand()
                )
                intake.schedule()
            }

            //-----------------------
            // debug zone
            //-----------------------

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

            if (intakeBalls.wasJustPressed()) {
                robot.intakeBalls().schedule()
            }
            if (stopIntake.wasJustPressed()) {
                robot.allStopCommand().schedule()
                robot.shooter.openFingerCommand().schedule()
            }

            if (shootBalls.wasJustPressed()) {
                robot.shootBalls().schedule()
            }

            if (ejectBalls.wasJustPressed()) {
                robot.ejectBalls().schedule()
            }

            if (startShooter.wasJustPressed()) {
                robot.shooter.goToRpmCommand(4000.0).schedule()
            }

            if (turretGoRight.wasJustPressed()) {
                robot.shooter.turretPosition += 0.1 * timeKeep.deltaTime.asS
            }
            if (turretGoLeft.wasJustPressed()) {
                robot.shooter.turretPosition -= 0.1 * timeKeep.deltaTime.asS
            }
            else {
                robot.shooter.updateHeading(robot.limelight.headingErrorDeg)
            }

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.limelight.updateHeadingError()
            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("target rpm", robot.shooter.targetRpm)
            panelsTelemetry.addData("shooter rpm", robot.shooter.shooterPower)
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("turret position", robot.shooter.servo1.position)
            panelsTelemetry.addData("power intake", robot.intake.power)
            panelsTelemetry.addData("power transfer", robot.transfer.power)
            panelsTelemetry.addData("sensor distance", robot.transfer.distance)
            panelsTelemetry.update(telemetry)
        }
    }
}