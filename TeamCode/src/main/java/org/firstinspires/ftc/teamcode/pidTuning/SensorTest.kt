package org.firstinspires.ftc.teamcode.pidTuning

import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.AnalogInput
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class SensorTest : LinearOpMode() {
    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val robot = Robot(hardwareMap)
        waitForStart()

        while (opModeIsActive()) {
            panelsTelemetry.addData("distance", robot.transfer.distance)
            panelsTelemetry.update(telemetry)
        }
    }
}