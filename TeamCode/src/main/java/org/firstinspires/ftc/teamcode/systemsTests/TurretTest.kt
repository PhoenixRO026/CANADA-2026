package org.firstinspires.ftc.teamcode.systemsTests

import com.acmerobotics.roadrunner.now
import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class TurretTest : LinearOpMode() {
    @Configurable
    object TurretConfig {
        @JvmField var turretStep = 0.1
        @JvmField var servo1Direction = 1
        @JvmField var servo2Direction = 1
    }

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val servo1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servo2 = hardwareMap.get(Servo::class.java, "turretServo2")
        val distanceSensor = hardwareMap.get(AnalogInput::class.java, "distanceSensor")
//        servo1.direction = Servo.Direction.REVERSE

        servo1.position = 0.5
        servo2.position = 0.5


        val maxVoltage = 3.3
        val maxDistanceMm = 1000.0

        fun voltageToDistanceMm(voltage: Double): Double {
            return (voltage / maxVoltage) * maxDistanceMm
        }

        var distance= voltageToDistanceMm(distanceSensor.voltage)

        fun isBallPresent(): Boolean {
            return distance in 30.0 .. 50.0
        }

        var previousTime: Double
        var deltaTime: Double
        var now: Double

        waitForStart()
        previousTime = now()

        while (opModeIsActive()) {
            distance= voltageToDistanceMm(distanceSensor.voltage)

            deltaTime = now() - previousTime
            previousTime = now()
            if (gamepad1.right_bumper) {
                servo1.position = (servo1.position + TurretConfig.turretStep * deltaTime).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position + TurretConfig.turretStep * deltaTime).coerceIn(0.0, 1.0)
            }

            if (gamepad1.left_bumper) {
                servo1.position = (servo1.position - TurretConfig.turretStep * deltaTime).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position - TurretConfig.turretStep * deltaTime).coerceIn(0.0, 1.0)
            }
            panelsTelemetry.addData("distance", distance)
            panelsTelemetry.addData("is ball there", isBallPresent())
            panelsTelemetry.addData("pos 1", servo1.position)
            panelsTelemetry.addData("pos 2", servo2.position)
            panelsTelemetry.update(telemetry)
        }
    }
}