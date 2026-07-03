package org.firstinspires.ftc.teamcode.pidTuning

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.controller.PIDController

@TeleOp
class ShooterTuning : LinearOpMode() {
    @Configurable
    object ShooterTuningConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.0011,
            kD = 0.0000001,
            kI = 0.0012,
            stabilityThreshold = 50.0
        )
        var kS = 1.4
        @JvmField
        var kV = 0.002
        @JvmField var targetRpm = 1000.0
    }

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry

        val motorLeft = hardwareMap.get(DcMotorEx::class.java, "motorShooterLeft")
        val motorRight = hardwareMap.get(DcMotorEx::class.java, "motorShooterRight")
        val rightBack = hardwareMap.get(DcMotorEx::class.java, "motorRB")
        motorLeft.direction = DcMotorSimple.Direction.REVERSE
//        val encoder = Encoder(motorLeft)
//        encoder.setDirection(Encoder.REVERSE)

        val voltageSensor = hardwareMap.voltageSensor.iterator().next()

        val timeKeep = TimeKeep()

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            val currentRpm = rightBack.velocity * 60.0 / 28
            
            val pidPower = ShooterTuningConfig.controller.calculate(currentRpm, ShooterTuningConfig.targetRpm, timeKeep.deltaTime)
            val feedforwardPower = ShooterTuningConfig.kS + ShooterTuningConfig.kV * ShooterTuningConfig.targetRpm
            val voltage = voltageSensor.voltage
            val shooterPower = pidPower + feedforwardPower / voltage

            motorRight.power = shooterPower
            motorLeft.power = shooterPower

            panelsTelemetry.addData("currentRpm", currentRpm)
            panelsTelemetry.addData("targetRpm", ShooterTuningConfig.targetRpm)
            panelsTelemetry.addData("shooterPower", shooterPower)
            panelsTelemetry.update(telemetry)
        }
    }
}