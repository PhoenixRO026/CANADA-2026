package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.pedropathing.ftc.localization.Encoder
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.library.controller.PIDController

class Shooter(
    val motorTop : DcMotorEx,
    val motorBottom: DcMotorEx,
    val motorTurret: DcMotorEx,
    val encoderTurret: Encoder,
    val encoderOuttake: Encoder,
    val voltageSensor: VoltageSensor
) {
    @Config
    data object ShooterConfig {
        @JvmField
        var controllerRpm = PIDController(
            kP = 0.001,
            kD = 0.000027,
            kI = 0.00125,
            stabilityThreshold = 50.0
        )
        @JvmField var robotAngularVelkP = 0.15
        @JvmField var targetRpmTolerance = 30

        @JvmField var kS = 1.4
        @JvmField var kV = 0.0029

        @JvmField
        var controllerTurret = PIDController(
            kP = 0.0004,
            kD = 0.00001,
            kI = 0.0001,
            stabilityThreshold = 0.2
        )
        @JvmField
        var controllerAngleHold = PIDController(
            kP = 0.03,
            kD = 0.0008,
            kI = 0.001,
            stabilityThreshold = 0.2,
        )
        @JvmField var ticksPerRev = 8192.0 * (108.0/22.0)

        @JvmField var targetPosTolerance = 75
        @JvmField var minTurretPosition = -10000.0
        @JvmField var maxTurretPosition = 16000.0
        @JvmField var limitTolerence = 50
        @JvmField var gearRatio = 23.0 / 30.0
        @JvmField var rpmFar = 3450.0
        @JvmField var rpmClose = 2850.0
        @JvmField var rpmRest = 2200.0
        @JvmField var shootClosePos = 5250.0
        @JvmField var shootFarPos = 7500.0
        @JvmField var shootChicagoFarPos = 14000.0

        @JvmField var cameraPos = 12000.0
        // in dreapta creste pozitia
    }
}