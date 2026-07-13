package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import org.firstinspires.ftc.teamcode.library.controller.LowPassFilter
import com.commonlibs.units.Duration
import com.pedropathing.ftc.FTCCoordinates
import com.pedropathing.geometry.PedroCoordinates
import com.pedropathing.geometry.Pose
import kotlin.math.abs
import kotlin.math.pow
import com.pedropathing.ftc.InvertedFTCCoordinates
import com.pedropathing.ftc.PoseConverter
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import kotlin.math.sqrt

class LimeLightCore(
    val camera: Limelight3A,
) {
    var headingErrorDeg: Double = 0.0
        private set

    var tagVisible: Boolean = false
        private set

    var aprilTagDistance: Double = 0.0
        private set

    var limelightPose : Pose = Pose(0.0, 0.0, 0.0)
        private set

    var limelightPoseValid: Boolean = false

    fun updateLimelightPose() {
        val result = camera.latestResult ?: run {
            aprilTagDistance = Double.NaN
            limelightPoseValid = false
            return
        }

        if (!result.isValid || result.staleness > 100) {
            aprilTagDistance = Double.NaN
            limelightPoseValid = false
            return
        }

        val fid = result.fiducialResults
            .firstOrNull { it.fiducialId in listOf(20, 24) }
            ?: run {
                aprilTagDistance = Double.NaN
                limelightPoseValid = false
                return
            }

        val pose = fid.robotPoseFieldSpace ?: run {
            limelightPoseValid = false
            return
        }

        val ftcPose = Pose2D(
            pose.position.unit,
            pose.position.x,
            pose.position.y,
            AngleUnit.RADIANS,
            pose.orientation.getYaw(AngleUnit.RADIANS)
        )

        limelightPose = PoseConverter.pose2DToPose(
            ftcPose,
            FTCCoordinates.INSTANCE
        ).getAsCoordinateSystem(
            PedroCoordinates.INSTANCE
        )
        limelightPoseValid = true
    }

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
