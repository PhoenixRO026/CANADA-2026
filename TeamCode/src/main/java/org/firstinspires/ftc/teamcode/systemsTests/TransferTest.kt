package org.firstinspires.ftc.teamcode.systemsTests

import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class TransferTest : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        val intakeStart = ButtonReader { gamepad1.dpad_right }
        val intakeStop = ButtonReader { gamepad1.dpad_left }
        val transferStart = ButtonReader {gamepad1.dpad_up }
        val transferStop = ButtonReader {gamepad1.dpad_down }
        val getBalls = ButtonReader { gamepad1.right_bumper }
        val buttons = listOf(intakeStart, intakeStop, transferStart, transferStop, getBalls)

        waitForStart()
        robot.follower.startTeleopDrive()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }
            robot.follower.update()
            Scheduler.execute()

            if (intakeStart.wasJustPressed()) {
                robot.intake.startIntakeCommand.schedule()
            }
            if (intakeStop.wasJustPressed()) {
                robot.intake.stopIntakeCommand.schedule()
            }

            if (getBalls.wasJustPressed()) {
                robot.intakeBalls.schedule()
            }

            Scheduler.execute()
            Scheduler.reset()
        }
    }
}