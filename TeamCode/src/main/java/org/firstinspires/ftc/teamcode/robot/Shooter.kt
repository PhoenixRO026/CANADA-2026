package org.firstinspires.ftc.teamcode.robot

import com.bylazar.configurables.annotations.Configurable
import com.commonlibs.units.Duration
import com.commonlibs.units.deg
import com.pedropathing.ftc.localization.Encoder
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.commands.Commands.waitUntil
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import kotlin.math.abs

class Shooter(
    val motorLeft : DcMotorEx,
    val motorRight: DcMotorEx,
    val servo1 : Servo,
    val servo2 : Servo,
    val finger : Servo,
    val hood : Servo,
    val voltageSensor : VoltageSensor,
    val motorEncoder : DcMotorEx
) {
    @Configurable
    object ShooterConfig {
        @JvmField
        var controllerRpm = PIDController(
            kP = 0.0011,
            kD = 0.0000001,
            kI = 0.0012,
            stabilityThreshold = 50.0
        )
        @JvmField var kS = 1.4
        @JvmField var kV = 0.002
        @JvmField var servoRange = 360.0
        @JvmField var gearRatio = 9.0 / 10.0
        @JvmField var maxFinalDegrees = servoRange * gearRatio // 324
        @JvmField var targetRpmTolerance = 50.0

        @JvmField var fingerOpen = 0.0
        @JvmField var fingerClose = 1.0

        @JvmField var hoodDown = 0.00384
        @JvmField var hoodUp = 0.976

        @JvmField var rpmFar = 5000.0
        @JvmField var rpmNear = 3000.0
        @JvmField var rpmRest = 0.0
    }

    fun servoToDeg(servoPos: Double): Double {
        return (servoPos - 0.5) * ShooterConfig.maxFinalDegrees
    }

    fun degToServo(deg: Double): Double {
        return deg / ShooterConfig.maxFinalDegrees + 0.5
    }

    var targetRpm = 0.0
    val currentRpm get() = motorEncoder.velocity * 60.0 / 28

    var shooterPower
        get() = motorLeft.power
        set(value) {
            motorLeft.power = value
            motorRight.power = value
        }

    var hoodPosition
        get() = hood.position
        set(value) {
            hood.position = value
        }
    fun hoodToPosition(position: Double) { hoodPosition = position }
    fun hoodDown() { hoodToPosition(ShooterConfig.hoodDown) }

    fun hoodToPositionCommand(position : Double) : Command = instant { hoodToPosition(position) }

    var turretPosition = 0.5
        get() = servo1.position
        set(value) {
            field = value.coerceIn(0.175, 0.850)
            servo1.position = field
            servo2.position = field
        }


    var turretAngle get() = -servoToDeg(turretPosition)
        set(value) {
            turretPosition = degToServo(value)
        }

    var fingerPosition
        get() = finger.position
        set(value) {
            finger.position = value
        }

    fun openFingerCommand() : Command = instant { openFinger() }
    fun closeFingerCommand() : Command = instant { closeFinger() }

    fun openFinger() { fingerPosition = ShooterConfig.fingerOpen }
    fun closeFinger() { fingerPosition = ShooterConfig.fingerClose }

    fun turretGoToAngle(targetAngle: Double) { turretAngle = targetAngle }

    fun updateHeading(headingError : Double) {
        turretPosition += degToServo(headingError)
    }

    fun updateRpm(deltaTime : Duration) {
        val voltage = voltageSensor.voltage
        val pidPower = ShooterConfig.controllerRpm.calculate(currentRpm, targetRpm, deltaTime)

        var kS = ShooterConfig.kS
        if (targetRpm == 0.0) {
            kS = 0.0
        }
        val feedforwardPower = kS + ShooterConfig.kV * targetRpm
        shooterPower = pidPower + feedforwardPower / voltage
    }

    fun shooterBusy(): Boolean {
        val busy = abs(targetRpm - currentRpm) > ShooterConfig.targetRpmTolerance
        return busy
    }

    fun goToRpm(rpm : Double) {
        targetRpm = rpm
    }

    fun goToRpmCommand(rpm: Double): Command =
        sequential(
            instant { goToRpm(rpm) },
            waitUntil { !shooterBusy() }
        )
}