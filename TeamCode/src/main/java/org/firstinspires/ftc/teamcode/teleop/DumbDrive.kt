package org.firstinspires.ftc.teamcode.teleop

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.bylazar.telemetry.TelemetryManager
import com.pedropathing.geometry.Pose
import com.pedropathing.math.Vector
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.absoluteValue

@TeleOp
class DumbDrive : LinearOpMode() {
    @Configurable
    object DumbDriveConfig {
        @JvmField var motorPower = 1.0
    }
    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val motorLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")

        val motors = Motors(hardwareMap)

        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
        val motorIntake = hardwareMap.get(DcMotorEx::class.java, "motorIntake")
        motorLeft.direction = DcMotorSimple.Direction.REVERSE

        val odo = hardwareMap.get(GoBildaPinpointDriver::class.java, "odo")

        odo.setEncoderResolution(
            GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
        )
        odo.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        )

        odo.resetPosAndIMU()

        var headingOffset = 0.0

        var lastBumper = false
        var shootEnable = false

        waitForStart()

        while (opModeIsActive()) {
            odo.update()

            val odoHeading = odo.getHeading(AngleUnit.RADIANS)

            if (gamepad1.y) {
                headingOffset = odoHeading
            }

            val heading = odoHeading - headingOffset

            motors.driveFieldCentric(
                forward = -gamepad1.left_stick_y.toDouble(),
                strafe = -gamepad1.left_stick_x.toDouble(),
                rotate = -gamepad1.right_stick_x.toDouble(),
                heading = heading
            )

            if (gamepad1.right_bumper && !lastBumper) {
                shootEnable = !shootEnable
            }
            lastBumper = gamepad1.right_bumper
            if (shootEnable) {
                motorLeft.power = DumbDriveConfig.motorPower
                motorRight.power = DumbDriveConfig.motorPower
            } else {
                motorLeft.power = 0.0
                motorRight.power = 0.0
            }

            if (gamepad1.left_trigger > 0.1) {
                motorTransfer.power = 1.0
            } else {
                motorTransfer.power = 0.0
            }
            if (gamepad1.right_trigger > 0.1) {
                motorIntake.power = 1.0
            } else {
                motorIntake.power = 0.0
            }

            if (gamepad1.dpad_left) {
                motors.motorLF.power = 1.0
            } else if (gamepad1.dpad_up) {
                motors.motorRF.power = 1.0
            } else if (gamepad1.dpad_right) {
                motors.motorRB.power = 1.0
            } else if (gamepad1.dpad_down) {
                motors.motorLB.power = 1.0
            }

            panelsTelemetry.addData("shooter power", motorLeft.power)
            panelsTelemetry.addData("heading", Math.toDegrees(heading))
            panelsTelemetry.addData("left stick x", gamepad1.left_stick_x)
            motors.telemetry(panelsTelemetry)
            panelsTelemetry.update(telemetry)
        }
    }

    data class Motors(
        val motorLF : DcMotorEx,
        val motorLB : DcMotorEx,
        val motorRB : DcMotorEx,
        val motorRF : DcMotorEx
    ) {
        constructor(hardwareMap: HardwareMap) : this(
            motorLF = hardwareMap.get(DcMotorEx::class.java, "motorLF"),
            motorLB = hardwareMap.get(DcMotorEx::class.java, "motorLB"),
            motorRB = hardwareMap.get(DcMotorEx::class.java, "motorRB"),
            motorRF = hardwareMap.get(DcMotorEx::class.java, "motorRF")
        )

        init {
            motorLF.direction = DcMotorSimple.Direction.FORWARD
            motorLB.direction = DcMotorSimple.Direction.REVERSE
            motorRF.direction = DcMotorSimple.Direction.REVERSE
            motorRB.direction = DcMotorSimple.Direction.FORWARD
        }

        fun drive(drivePose: Pose) {
            val x = drivePose.x
            val y = drivePose.y
            val heading = drivePose.heading

            val powerLF = x - y - heading
            val powerLB = x + y - heading
            val powerRB = x - y + heading
            val powerRF = x + y + heading

            val maxPower =
                listOf(1.0, powerLF, powerLB, powerRB, powerRF)
                    .maxOf { it.absoluteValue }

            motorLF.power = powerLF / maxPower
            motorLB.power = powerLB / maxPower
            motorRF.power = powerRF / maxPower
            motorRB.power = powerRB / maxPower
        }

        fun driveFieldCentric(forward: Double, strafe: Double, rotate: Double, heading: Double) {
            val driveVec = Vector(Pose(
                forward,
                strafe
            )).apply {
                rotateVector(-heading)
            }

            val drivePos = Pose(
                driveVec.xComponent,
                driveVec.yComponent,
                rotate
            )

            drive(drivePos)
        }

        fun telemetry(telemetry: TelemetryManager) {
            telemetry.apply {
                addData("motorLF", motorLF.power)
                addData("motorLB", motorLB.power)
                addData("motorRF", motorRF.power)
                addData("motorRB", motorRB.power)
            }
        }
    }
}