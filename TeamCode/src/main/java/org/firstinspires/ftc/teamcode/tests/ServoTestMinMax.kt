package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoTestMinMax : LinearOpMode(){
    override fun runOpMode() {
        val servo1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servo2 = hardwareMap.get(Servo::class.java, "turretServo2")

        servo1.position = 0.0
        servo2.position = 0.0

        waitForStart()

        servo1.position = 1.0
        servo2.position = 1.0

        while (opModeIsActive()) {
            idle()
        }
    }
}