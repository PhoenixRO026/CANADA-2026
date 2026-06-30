package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.commonlibs.units.Duration
import com.pedropathing.ftc.localization.Encoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.library.controller.PIDController

class Shooter(
    val motorLeft : DcMotorEx,
    val motorRight: DcMotorEx,
    val servo1 : Servo,
    val servo2 : Servo,
    val voltageSensor : VoltageSensor,
) {
    @Config
    data object ShooterConfig {
        @JvmField
        var controllerRpm = PIDController(
            kP = 0.0,
            kI = 0.0,
            kD = 0.0,
            stabilityThreshold = 50.0
        )
        @JvmField var kS = 0.0
        @JvmField var kV = 0.0
        @JvmField var servoRange = 255.0
    }
    var targetRpm = 0.0
    val currentRpm get() = motorLeft.velocity

    var shooterPower
        get() = motorRight.power
        set(value) {
            motorRight.power = value
            motorLeft.power = value
        }

    var turretPosition
        get() = servo1.position
        set(value) {
            servo1.position = value
            servo2.position = value
        }

    fun updateHeading(headingError : Double) { // Asta nus daca o sa mearga ca nu stiu raportul servo-rulment
        turretPosition += ShooterConfig.servoRange * headingError / 270
    }

    fun updateRpm(deltaTime : Duration) {
        val voltage = voltageSensor.voltage
        val pidPower = ShooterConfig.controllerRpm.calculate(currentRpm, targetRpm, deltaTime)
        val feedforwardPower = ShooterConfig.kS + ShooterConfig.kV * targetRpm
        shooterPower = pidPower + feedforwardPower / voltage
    }
}