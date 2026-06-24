package org.firstinspires.ftc.teamcode.systemsTests

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class TurretTest : LinearOpMode() {
    @Configurable
    object TurretConfig {
        @JvmField var turretStep = 0.01
        @JvmField var servo1Direction = 1
        @JvmField var servo2Direction = 1
    }

    override fun runOpMode() {
        val servo1 = hardwareMap.get(Servo::class.java, "turretServo1")
        val servo2 = hardwareMap.get(Servo::class.java, "turretServo2")
//        servo1.direction = Servo.Direction.REVERSE

        servo1.position = 0.0
        servo2.position = 0.0

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                servo1.position = (servo1.position + TurretConfig.turretStep).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position + TurretConfig.turretStep).coerceIn(0.0, 1.0)
            }

            if (gamepad1.left_bumper) {
                servo1.position = (servo1.position - TurretConfig.turretStep).coerceIn(0.0, 1.0)
                servo2.position = (servo2.position - TurretConfig.turretStep).coerceIn(0.0, 1.0)
            }
        }
    }
}