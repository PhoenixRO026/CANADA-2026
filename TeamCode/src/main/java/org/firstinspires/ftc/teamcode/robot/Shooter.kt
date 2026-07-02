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
) {
    @Configurable
    object ShooterConfig {
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
        @JvmField var targetRpmTolerance = 50.0

        @JvmField var fingerOpen = 1.0
        @JvmField var fingerClose = 0.0

        @JvmField var rpmFar = 3400.0
        @JvmField var rpmNear = 3000.0
        @JvmField var rpmRest = 1000.0
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

    var hoodPosition
        get() = hood.position
        set(value) {
            hood.position = value
        }
    fun hoodToPosition(position: Double) { hoodPosition = position }
    fun hoodDown() { hoodToPosition(0.0) }

    private var turretPosition = 0.5
        get() = servo1.position
        set(value) {
            field = value.coerceIn(0.175, 0.850)
            servo1.position = field
            servo2.position = field
        }


    var turretAngle get() = servoToDeg(turretPosition)
        set(value) {
            turretPosition = degToServo(value)
        }

    var openFingerCommand : Command = instant { openFinger() }
    var closeFingerCommand : Command = instant { closeFinger() }

    fun openFinger() { finger.position = ShooterConfig.fingerOpen }
    fun closeFinger() { finger.position = ShooterConfig.fingerClose }

    fun turretGoToAngle(targetAngle: Double) { turretAngle = targetAngle }

    fun updateHeading(headingError : Double) {
        turretPosition += degToServo(headingError)
    }

    fun updateRpm(deltaTime : Duration) {
        val voltage = voltageSensor.voltage
        val pidPower = ShooterConfig.controllerRpm.calculate(currentRpm, targetRpm, deltaTime)
        val feedforwardPower = ShooterConfig.kS + ShooterConfig.kV * targetRpm
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