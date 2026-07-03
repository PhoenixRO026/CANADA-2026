package org.firstinspires.ftc.teamcode.systemsTests

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.ftc.localization.Encoder
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader

@TeleOp
class StupidTransferTest : LinearOpMode() {
    override fun runOpMode() {
        val motor = hardwareMap.get(DcMotorEx::class.java, "motorIntake")
        val motor2= hardwareMap.get(DcMotorEx::class.java, "motorTransfer")
        val encoder = Encoder(motor)
        encoder.setDirection(Encoder.REVERSE)

        val joinedTelemetry = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)
        val loopTimer = ElapsedTime()

        waitForStart()

        encoder.reset()
        loopTimer.reset()

        while (opModeIsActive()) {
            val dt = loopTimer.seconds()
            loopTimer.reset()

            motor.power = if (gamepad1.right_bumper) 1.0 else 0.0
            motor2.power =if (gamepad1.left_bumper) 1.0 else 0.0

            encoder.update()

            val ticksPerSecond =  encoder.deltaPosition / motor.motorType.ticksPerRev / dt

            val rpm = ticksPerSecond * 60.0

            joinedTelemetry.addData("Rotations per minute", rpm)
            joinedTelemetry.addData("Ticks/sec", ticksPerSecond)
            joinedTelemetry.addData("Delta ticks", encoder.deltaPosition)
            joinedTelemetry.addData("Ticks/rev", motor.motorType.ticksPerRev)
            joinedTelemetry.addData("Power", motor.power)
            joinedTelemetry.update()
        }
    }
}