package org.firstinspires.ftc.teamcode.library

import com.pedropathing.geometry.Pose

const val turretOffsetFromRobot = 66.994 / 25.5
const val limelightOffsetFromTurret = 159.601 / 25.4

fun limelightToRobotPos(limelightPose: Pose, turretHeadingDiff: Double): Pose {
    return limelightPose.toTurretPos(turretHeadingDiff).toRobotPose()
}

// turret to robot
fun Pose.toRobotPose(): Pose {
    val offsetPos = Pose(
        turretOffsetFromRobot,
        0.0,
        0.0,
        this.coordinateSystem
    ).rotate(this.heading, false)
    return this.plus(offsetPos)
}

// limelight to turret
fun Pose.toTurretPos(turretHeadingDiff: Double): Pose {
    val offsetPose = Pose(
        -limelightOffsetFromTurret,
        0.0,
        -turretHeadingDiff,
        this.coordinateSystem
    ).rotate(this.heading, false)
    return this.plus(offsetPose)
}