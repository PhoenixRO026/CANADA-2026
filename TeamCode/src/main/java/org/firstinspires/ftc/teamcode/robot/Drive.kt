package org.firstinspires.ftc.teamcode.robot

import com.pedropathing.follower.Follower

class Drive(
    private val follower: Follower
) {
    data object DriveConfig {
        @JvmField val slowSpeed = 0.4
    }

    var isSlowMode = false

    private var headingOffset = 0.0

    val heading: Double
        get() = follower.heading - headingOffset

    private val currentSpeed: Double
        get() = if (isSlowMode) DriveConfig.slowSpeed else 1.0

    fun resetFieldCentric() {
        headingOffset = follower.heading
    }

    fun driveFieldCentric(forward: Double, strafe: Double, rotate: Double) {
        follower.setTeleOpDrive(
            forward * currentSpeed,
            strafe * currentSpeed,
            rotate * currentSpeed,
            false,
            headingOffset
        )
    }

    fun driveRobotCentric(forward: Double, strafe: Double, rotate: Double) {
        follower.setTeleOpDrive(
            forward * currentSpeed,
            strafe * currentSpeed,
            rotate * currentSpeed,
            true
        )
    }
}