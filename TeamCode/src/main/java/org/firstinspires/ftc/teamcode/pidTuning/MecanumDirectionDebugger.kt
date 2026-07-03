package org.firstinspires.ftc.teamcode.pidTuning

import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx

@TeleOp
class MecanumDirectionDebugger : LinearOpMode() {
    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val motorLF = hardwareMap.get(DcMotorEx::class.java, "motorLF")
        val motorLB = hardwareMap.get(DcMotorEx::class.java, "motorLB")
        val motorRB = hardwareMap.get(DcMotorEx::class.java, "motorRB")
        val motorRF = hardwareMap.get(DcMotorEx::class.java, "motorRF")

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                motorLF.power = 0.5
            } else {
                motorLF.power = 0.0
            }

            if (gamepad1.dpad_right) {
                motorRF.power = 0.5
            } else {
                motorRF.power = 0.0
            }

            if (gamepad1.dpad_down) {
                motorRB.power = 0.5
            } else {
                motorRB.power = 0.0
            }

            if (gamepad1.dpad_left) {
                motorLB.power = 0.5
            } else {
                motorLB.power = 0.0
            }

        }
    }
}