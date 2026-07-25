package org.firstinspires.ftc.teamcode.teleop

import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class SummerDriveBlue : SummerDriveDuo() {
    override val pipeline = 1
    override val goalPose = Pose(55.0, 135.0, Math.toRadians(90.0))
    override val side = Robot.Side.BLUE
}

@TeleOp
class SummerDriveRed : SummerDriveDuo() {
    override val pipeline = 2
    override val goalPose = Pose(136.5, 135.0, Math.toRadians(90.0))
    override val side = Robot.Side.BLUE
}

