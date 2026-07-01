package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.library.controller.PIDController

class Transfer(
    val motor: DcMotorEx,
    val distanceSensor: AnalogInput
) {

    data object TransferConfig {
        val maxVoltage = 3.3
        val maxDistanceMm = 1000.0
    }

    var distance= voltageToDistanceMm(distanceSensor.voltage)

    fun voltageToDistanceMm(voltage: Double): Double {

        // Cross-multiplication formula
        return (voltage / TransferConfig.maxVoltage) * TransferConfig.maxDistanceMm
    }

    var power
        get() = motor.power
        set(value) {
            motor.power = value
        }

    fun startTransfer() { power = 1.0 }
    fun stopTransfer() { power = 0.0 }
    fun reverseTransfer() { power = -1.0 }

    val startTransferCommand : Command = instant { startTransfer() }
    val stopTransferCommand : Command = instant { stopTransfer() }
    val reverseTransferCommand : Command = instant { reverseTransfer() }
}