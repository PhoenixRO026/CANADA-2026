package org.firstinspires.ftc.teamcode.robot

import com.pedropathing.follower.Follower

class Drive(
    val follower: Follower
) {
    data object DriveConfig {
        @JvmField val slowSpeed = 0.4
    }
    fun driveFieldCentric(forward: Double, strafe : Double, rotate : Double) {
        follower.setTeleOpDrive(forward, strafe, rotate, driveFieldCentric(forward, strafe, rotate))
    }
}