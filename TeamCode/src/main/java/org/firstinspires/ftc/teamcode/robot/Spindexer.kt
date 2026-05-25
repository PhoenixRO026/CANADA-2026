package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo

class Spindexer(
    val servoTransfer1: Servo,
    val servoTransfer2: Servo,
    val finger: Servo,
    val colorSensor: NormalizedColorSensor
) {
}