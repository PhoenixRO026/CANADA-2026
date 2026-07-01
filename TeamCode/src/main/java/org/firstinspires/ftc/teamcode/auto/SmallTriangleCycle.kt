package org.firstinspires.ftc.teamcode.auto.paths

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower
import org.firstinspires.ftc.teamcode.robot.Robot


@Autonomous
class SmallTriangleCycle : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap)

        val path = follower.pathBuilder()
            .addPath(
                BezierLine(
                    Pose(11.809, 34.843),
                    Pose(43.989, 8.353)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .addPath(
                BezierLine(
                    Pose(43.989, 8.353),
                    Pose(9.173, 8.266)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .addPath(
                BezierLine(
                    Pose(9.173, 8.266),
                    Pose(43.942, 8.324)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .build()
    }
}