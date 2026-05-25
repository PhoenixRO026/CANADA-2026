package org.firstinspires.ftc.teamcode.teleop

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.robot.Robot
import kotlin.compareTo
import kotlin.text.toDouble

@TeleOp
class SummerDrive : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap)

        waitForStart()
        robot.follower.startTeleopDrive()

        while (opModeIsActive()) {
            robot.follower.update()

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
        }
    }
}