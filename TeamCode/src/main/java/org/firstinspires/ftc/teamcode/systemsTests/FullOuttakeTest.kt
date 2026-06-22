package org.firstinspires.ftc.teamcode.systemsTests

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.ftc.localization.Encoder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class FullOuttakeTest : LinearOpMode() {
    @Configurable
    object FullOuttakeConfig {
        @JvmField var motorPower = 0.0
        @JvmField var hoodStep = 0.01
        @JvmField var turretStep = 0.01
    }

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val motorLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        val encoder = Encoder(motorLeft)
        encoder.setDirection(Encoder.REVERSE)

        val servoHood = hardwareMap.get(Servo::class.java, "servoHood")
        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
        motorRight.direction = DcMotorSimple.Direction.REVERSE
        servoHood.position = 0.0

        val servo1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servo2 = hardwareMap.get(Servo::class.java, "turretServo2")
        servo1.direction = Servo.Direction.REVERSE

        servo1.position = 0.0
        servo2.position = 0.0

        waitForStart()

        encoder.reset()

        while (opModeIsActive()) {
            motorLeft.power = FullOuttakeConfig.motorPower
            motorRight.power = FullOuttakeConfig.motorPower

            val rpm = motorLeft.velocity * 60.0 / 28

            if (gamepad1.dpad_up) {
                servoHood.position = (servoHood.position + FullOuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            if (gamepad1.dpad_down) {
                servoHood.position = (servoHood.position - FullOuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            if (gamepad1.a) {
                motorTransfer.power = 1.0
            }
            if (gamepad1.b){
                motorTransfer.power = -1.0
            }
            if (gamepad1.x) {
                motorTransfer.power = 0.0
            }

            if (gamepad1.right_bumper) {
                servo1.position = (servo1.position + FullOuttakeConfig.turretStep).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position + FullOuttakeConfig.turretStep).coerceIn(0.0, 1.0)
            }

            if (gamepad1.left_bumper) {
                servo1.position = (servo1.position - FullOuttakeConfig.turretStep).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position - FullOuttakeConfig.turretStep).coerceIn(0.0, 1.0)
            }
            panelsTelemetry.addData("rpm", rpm)
            panelsTelemetry.addData("power", motorLeft.power)
            panelsTelemetry.addData("hood position", servoHood.position)
            panelsTelemetry.addData("turret position", servo1.position)
            panelsTelemetry.update(telemetry)
        }
    }
}
