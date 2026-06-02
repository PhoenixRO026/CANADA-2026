package org.firstinspires.ftc.teamcode.teleop

import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.robot.Robot
import kotlin.compareTo
import kotlin.text.toDouble
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.library.buttons.ToggleButtonReader

@TeleOp
class SummerDrive : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        val intakeStart = ButtonReader {gamepad1.dpad_right}
        val intakeStop = ButtonReader {gamepad1.dpad_left}
        val buttons = listOf(intakeStart, intakeStop)

        waitForStart()
        robot.follower.startTeleopDrive()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }

            robot.follower.update()
            Scheduler.execute()

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

            if (intakeStart.wasJustPressed()) {
                robot.intake.startIntakeCommand.schedule()
            }

            if (intakeStop.wasJustPressed()) {
                robot.intake.stopIntakeCommand.schedule()
            }
            Scheduler.reset()
        }
    }
}