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

        val intakeBalls = ToggleButtonReader ({ gamepad1.dpad_right })
        val ejectBalls = ButtonReader { gamepad1.dpad_left }
        val shootBalls = ButtonReader { gamepad1.dpad_up }
        val rpmToRest = ButtonReader { gamepad1.left_bumper }
        val buttons = listOf(intakeBalls, ejectBalls, shootBalls, rpmToRest)
        val timeKeep = TimeKeep()

        waitForStart()

        var driverCommand : Command? = null

        robot.follower.startTeleopDrive()
        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(0.0)
//        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }
            timeKeep.resetDeltaTime()

            robot.follower.update()

            //-----------------------
            // debug zone
            //-----------------------

            if(gamepad1.x){
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

            if(gamepad1.left_trigger >= 0.2) {
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

            if (intakeBalls.state) {
                if(!robot.intakeBalls().isScheduled && !robot.shootBalls().isScheduled) {
                    robot.intakeBalls().schedule()
                }
            }
            else {
                if(robot.intakeBalls().isScheduled) { // sau Scheduler.isRunning(robot.intakeBalls())
                    Scheduler.cancel(robot.intakeBalls())
                    robot.allStopCommand().schedule()
                }
            }

            if (shootBalls.wasJustPressed() && !robot.intakeBalls().isScheduled && !robot.shootBalls().isScheduled) {
                robot.shootBalls().schedule()
            }

            if (ejectBalls.wasJustPressed()) {
                robot.ejectBalls().schedule()
            }

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.limelight.updateHeadingError()
            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("turret position", robot.shooter.servo1.position)
            panelsTelemetry.addData("power intake", robot.intake.power)
            panelsTelemetry.addData("power transfer", robot.transfer.power)
            panelsTelemetry.addData("sensor distance", robot.transfer.distance)
            panelsTelemetry.update(telemetry)
        }
    }
}