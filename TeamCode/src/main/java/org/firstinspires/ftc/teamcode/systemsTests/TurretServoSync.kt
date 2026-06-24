package org.firstinspires.ftc.teamcode.systemsTests

import com.acmerobotics.roadrunner.now
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class TurretServoSync : LinearOpMode() {
    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val servo1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servo2 = hardwareMap.get(Servo::class.java, "turretServo2")

        servo1.position = 0.0
        servo2.position = 0.0

        var previousTime: Double
        var deltaTime : Double
        var now : Double

        var offset = 0.0

        waitForStart()
        previousTime = now()


        while (opModeIsActive()) {
            now = now()
            deltaTime = now - previousTime
            previousTime = now

            if (gamepad1.dpad_right) {
                offset += 0.01 * deltaTime
            }
            if (gamepad1.dpad_left) {
                offset -= 0.01 * deltaTime
            }

            if (gamepad1.dpad_up) {
                servo1.position += 0.01 * deltaTime
                servo2.position += 0.01 * deltaTime
            }
            if (gamepad1.dpad_down) {
                servo1.position -= 0.01 * deltaTime
                servo2.position -= 0.01 * deltaTime
            }
            servo1.position += offset

            panelsTelemetry.addData("servo1 pos", servo1.position)
            panelsTelemetry.addData("servo2 pos", servo2.position)
            panelsTelemetry.addData("offset", offset)
            panelsTelemetry.addData("delta time", deltaTime)
            panelsTelemetry.update(telemetry)
        }
    }
}