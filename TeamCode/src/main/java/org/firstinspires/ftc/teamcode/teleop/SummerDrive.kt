package org.firstinspires.ftc.teamcode.teleop

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.robot.Robot
import kotlin.compareTo
import kotlin.text.toDouble
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.library.buttons.ToggleButtonReader

@TeleOp
open class SummerDrive : LinearOpMode() {
    open val pipeline : Int = 1

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        robot.limelight.setPipeline(pipeline)

        val intakeBalls = ToggleButtonReader ({ gamepad1.dpad_right })
        val ejectBalls = ButtonReader { gamepad1.dpad_left }
        val shootBalls = ButtonReader { gamepad2.y }
        val rpmToRest = ButtonReader { gamepad2.left_bumper }
        val buttons = listOf(intakeBalls, shootBalls, rpmToRest)
        val timeKeep = TimeKeep()

        waitForStart()

        var driverCommand = robot.intakeBalls().until { !intakeBalls.state }

        robot.follower.startTeleopDrive()
        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(45.0)
        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }
            timeKeep.resetDeltaTime()

            robot.follower.update()
            Scheduler.execute()

            if(gamepad1.left_trigger >= 0.2) {
                robot.drive.isSlowMode = true
            }
            else {
                robot.drive.isSlowMode = false
            }

            robot.drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )
            if (gamepad1.y) {
                robot.drive.resetFieldCentric()
            }

            if (intakeBalls.wasJustPressed()) {
                if (intakeBalls.state) {
                    driverCommand = robot.intakeBalls()
                        .until { !intakeBalls.state }
                    driverCommand!!.schedule()
                }
                else {
                    driverCommand = null
                }
            }

            if (shootBalls.wasJustPressed()) {
                val shootCommand = robot.shootBalls()
                    .unless { driverCommand != null && driverCommand!!.isScheduled() }

                shootCommand.schedule()
            }

            if (ejectBalls.wasJustPressed()) {
                robot.intake.stopIntakeCommand.schedule()
            }

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.limelight.updateHeadingError()
            Scheduler.reset()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("turret position", robot.shooter.servo1.position)
            panelsTelemetry.addData("power intake", robot.intake.power)
            panelsTelemetry.addData("power transfer", robot.transfer.power)
            panelsTelemetry.addData("sensor distance", robot.transfer.distance)
            panelsTelemetry.update(telemetry)
        }
    }
}