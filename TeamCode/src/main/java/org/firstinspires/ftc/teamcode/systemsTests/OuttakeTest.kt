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
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp
class OuttakeTest : LinearOpMode() {
    @Configurable
    object OuttakeConfig {
        @JvmField var motorPower = 0.5
        @JvmField var hoodStep = 0.01
    }

    override fun runOpMode() {
        val joinedTelemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

        val motorLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        val encoder = Encoder(motorLeft)
        val servoHood = hardwareMap.get(Servo::class.java, "servoHood")
        val motorTransfer = hardwareMap.get(DcMotorEx::class.java, "MTransfer")
        encoder.setDirection(Encoder.REVERSE)
        motorRight.direction = DcMotorSimple.Direction.REVERSE
        servoHood.position = 0.0

        waitForStart()

        encoder.reset()

        while (opModeIsActive()) {

            motorLeft.power = OuttakeConfig.motorPower
            motorRight.power = OuttakeConfig.motorPower


            val rpm = motorLeft.velocity * 60.0 / 28

            if (gamepad1.dpad_up) {
                servoHood.position = (servoHood.position + OuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            if (gamepad1.dpad_down) {
                servoHood.position = (servoHood.position - OuttakeConfig.hoodStep).coerceIn(0.0, 1.0)
            }
            if (gamepad1.a) {
                motorTransfer.power = 1.0
            }
            if (gamepad1.b){
                motorTransfer.power = - 1.0
            }
            joinedTelemetry.addData("power", motorLeft.power)
            joinedTelemetry.addData("hood position", servoHood.position)
            joinedTelemetry.addData("rpm", rpm)
            joinedTelemetry.update()
        }
    }
}
