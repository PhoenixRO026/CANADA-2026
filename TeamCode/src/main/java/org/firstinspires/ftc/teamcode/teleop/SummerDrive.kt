package org.firstinspires.ftc.teamcode.teleop

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@TeleOp
class SummerDrive : LinearOpMode() {
    override fun runOpMode() {
        val follower = Constants.createFollower(hardwareMap)

        waitForStart()

        while (opModeIsActive()) {

        }
    }
}