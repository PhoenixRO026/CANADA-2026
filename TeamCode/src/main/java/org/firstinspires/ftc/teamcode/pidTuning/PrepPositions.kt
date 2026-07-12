package org.firstinspires.ftc.teamcode.prepPositions

import com.acmerobotics.roadrunner.now
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.TimeKeep

@TeleOp
class PrepPositions : LinearOpMode() {
    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val finger = hardwareMap.get(Servo::class.java, "finger")
        val hood = hardwareMap.get(Servo::class.java, "servoHood")
        finger.scaleRange(0.3844, 0.67)

        var previousTime: Double
        var deltaTime: Double
        var now: Double

        waitForStart()
        previousTime = now()

        finger.position = 0.5
        hood.position = 0.3

        while (opModeIsActive()) {
            deltaTime = now() - previousTime
            previousTime = now()

            if (gamepad1.dpad_up) {
                hood.position += (0.1 * deltaTime).coerceIn(0.3, 0.83)
            }
            if (gamepad1.dpad_down) {
                hood.position -= (0.1 * deltaTime).coerceIn(0.3, 0.83)
            }
            if (gamepad1.dpad_right) {
                finger.position += 0.1 * deltaTime
            }
            if (gamepad1.dpad_left) {1
                finger.position -= 0.1 * deltaTime
            }
            panelsTelemetry.addData("finger pos", finger.position)
            panelsTelemetry.addData("hood pos", hood.position)
            panelsTelemetry.addData("delta time", deltaTime)
            panelsTelemetry.update(telemetry)
        }

    }
}