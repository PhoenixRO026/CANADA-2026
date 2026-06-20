package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.pedropathing.ftc.localization.Encoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.library.controller.PIDController

class Shooter(
    val motorTop : DcMotorEx,
    val motorBottom : DcMotorEx,
    val servo1 : Servo,
    val servo2 : Servo
) {
    @Config
    data object ShooterConfig {

    }
    var motorPower
        get() = motorTop.power
        set(value) {
            motorTop.power = value
            motorBottom.power = value
        }

}