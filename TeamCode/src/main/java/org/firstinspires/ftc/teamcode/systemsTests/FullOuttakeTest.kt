package org.firstinspires.ftc.teamcode.systemsTests

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class FullOuttakeTest : LinearOpMode() {
    @Configurable
    object OuttakeConfig {
        @JvmField var turretStep = 0.01
        @JvmField var hoodStep = 0.01
        @JvmField var motorPower = 0.7
    }
    override fun runOpMode() {
        val motorLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        motorRight.direction = DcMotorSimple.Direction.REVERSE

        val servoHood = hardwareMap.get(Servo::class.java, "servoHood")
        servoHood.position = 0.0

        val joinedTelemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        waitForStart()

        while (opModeIsActive()) {
            motorLeft.power = OuttakeTest.OuttakeConfig.motorPower
            motorRight.power = OuttakeTest.OuttakeConfig.motorPower

            if (gamepad1.dpad_up) {
                servoHood.position = (servoHood.position + OuttakeTest.OuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            if (gamepad1.dpad_down) {
                servoHood.position = (servoHood.position - OuttakeTest.OuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            joinedTelemetry.addData("power", motorLeft.power)
            joinedTelemetry.addData("hood position", servoHood.position)
            joinedTelemetry.update()
        }
    }
}