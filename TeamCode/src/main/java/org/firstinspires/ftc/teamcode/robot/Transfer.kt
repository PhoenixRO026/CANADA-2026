package org.firstinspires.ftc.teamcode.robot

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo

class Transfer(
    val motor: DcMotorEx,
) {
    var power
        get() = motor.power
        set(value) {
            motor.power = value
        }

    fun startTransfer() { power = 1.0 }
    fun stopTransfer() { power = 0.0 }

    val startTransferCommand : Command = instant { startTransfer() }
    val stopTransferCommand : Command = instant { stopTransfer() }
}