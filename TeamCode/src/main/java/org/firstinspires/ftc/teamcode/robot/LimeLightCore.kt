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
    enum class AutoCase { PPG, PGP, GPP, UNKNOWN }
    var currentCase: AutoCase = AutoCase.UNKNOWN
        private set

    var headingErrorDeg: Double = 0.0
        private set

    var tagVisible: Boolean = false
        private set

    var aprilTagDistance: Double = 0.0

    private fun updateDistance() {
        val fid = camera.latestResult
            ?.fiducialResults
            ?.firstOrNull { it.fiducialId in listOf(20, 24) }
            ?: run { aprilTagDistance = Double.NaN; return }

        val pose = fid.targetPoseCameraSpace ?: run {
            aprilTagDistance = Double.NaN
            return
        }

        val x = pose.position.x
        val z = pose.position.z

        aprilTagDistance = kotlin.math.sqrt(x * x + z * z)
    }

    fun getDistance() : Double {
        updateDistance()
        return aprilTagDistance
    }

    fun setPipeline(index: Int) {
        camera.pipelineSwitch(index)
    }

    fun updateCase() {
        val id = camera.latestResult
            ?.fiducialResults
            ?.firstOrNull { it.fiducialId in listOf(21, 22, 23) }
            ?.fiducialId

        currentCase = when (id) {
            21 -> AutoCase.GPP
            22 -> AutoCase.PGP
            23 -> AutoCase.PPG
            else -> AutoCase.UNKNOWN
        }
    }

    fun updateHeadingError() {
        val fid = camera.latestResult
            ?.fiducialResults
            ?.firstOrNull()

        if (fid != null) {
            val currHeadingError = fid.targetXDegrees
            headingErrorDeg = currHeadingError
            tagVisible = true
        } else {
            headingErrorDeg = 0.0
            tagVisible = false
        }
    }
}
