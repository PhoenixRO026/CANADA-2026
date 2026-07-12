package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import org.firstinspires.ftc.teamcode.library.controller.LowPassFilter
import com.commonlibs.units.Duration
import kotlin.math.abs
import kotlin.math.pow

class LimeLightCore(
    val camera: Limelight3A,
) {
    var headingErrorDeg: Double = 0.0
        private set

    var tagVisible: Boolean = false
        private set

    var aprilTagDistance: Double = 0.0

    fun updateDistance() {
        val fid = camera.latestResult
            ?.fiducialResults
            ?.firstOrNull { it.fiducialId in listOf(20, 24) }
            ?: run { aprilTagDistance = Double.NaN; return }

        val pose = fid.targetPoseCameraSpace ?: run {
            aprilTagDistance = 0.0
            return
        }

        val x = pose.position.x
        val y = pose.position.y
        val z = pose.position.z

        aprilTagDistance = kotlin.math.sqrt(x * x + y * y + z * z) * 100
    }

    fun setPipeline(index: Int) {
        camera.pipelineSwitch(index)
    }

    fun updateHeadingError() {
        val fid = camera.latestResult
            ?.fiducialResults
            ?.firstOrNull()

        if (fid != null) {
            headingErrorDeg = -fid.targetXDegrees
            tagVisible = true
        } else {
            headingErrorDeg = 0.0
            tagVisible = false
        }
    }
}
