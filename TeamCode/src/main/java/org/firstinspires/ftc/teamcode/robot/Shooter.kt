package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.commonlibs.units.Duration
import com.commonlibs.units.deg
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
        @JvmField var servoRange = 360.0
        @JvmField var gearRatio = 9.0 / 10.0
        @JvmField var maxFinalDegrees = servoRange * gearRatio

    }

    fun servoToDeg(servoPos: Double): Double {
        return servoPos * ShooterConfig.maxFinalDegrees
    }

    fun degToServo(deg: Double): Double {
        return deg / ShooterConfig.maxFinalDegrees
    }

    var targetRpm = 0.0
    val currentRpm get() = motorLeft.velocity

    var shooterPower
        get() = motorLeft.power
        set(value) {
            motorLeft.power = value
            motorRight.power = value
        }

    private var turretPosition = 0.5
        get() = servo1.position
        set(value) {
            field = value.coerceIn(0.0, 1.0)
            servo1.position = field
            servo2.position = field
        }


    var turretAngle get() = servoToDeg(turretPosition)
        set(value) {
            turretPosition = degToServo(value)
        }

    //tho nuj daca trebe daca ii servo
    fun updateHeading(headingError : Double) { // Asta nus daca o sa mearga ca nu stiu raportul servo-rulment // cum functioneaza formula? - Coman
        turretPosition += degToServo(headingError)
    }

    fun updateRpm(deltaTime : Duration) {
        val voltage = voltageSensor.voltage
        val pidPower = ShooterConfig.controllerRpm.calculate(currentRpm, targetRpm, deltaTime)
        val feedforwardPower = ShooterConfig.kS + ShooterConfig.kV * targetRpm
        shooterPower = pidPower + feedforwardPower / voltage
    }
}