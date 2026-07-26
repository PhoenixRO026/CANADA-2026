package org.firstinspires.ftc.teamcode.teleop

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.auto.AutoMemory
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.pedroPathing.DrawingClone
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
open class SummerDriveDuo : LinearOpMode() {
    open val pipeline : Int = 1
    open val goalPose : Pose = Pose(55.0, 135.0, Math.toRadians(90.0))
    open val side : Robot.Side = Robot.Side.BLUE

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val robot = Robot(hardwareMap, AutoMemory.lastAutoPose)
        Scheduler.reset()

        robot.limelight.setPipeline(pipeline)

        val intakeBalls = ButtonReader { gamepad2.right_bumper }
        val ejectBalls = ButtonReader { gamepad2.dpad_left }
        val shootBalls = ButtonReader { gamepad2.y }
        val stopIntake = ButtonReader { gamepad2.left_bumper }
        val rpmToRest = ButtonReader { gamepad2.dpad_up }
        val startShooter = ButtonReader { gamepad2.dpad_down }
        val resetOdo = ButtonReader { gamepad2.x }
        val buttons = listOf(intakeBalls, ejectBalls, shootBalls, rpmToRest, stopIntake, startShooter, resetOdo)
        val timeKeep = TimeKeep()

        waitForStart()

        var driverCommand : Command? = null

        robot.follower.startTeleopDrive()
        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(0.5)
        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            buttons.forEach { it.readValue() }
            timeKeep.resetDeltaTime()

            robot.follower.update()
            robot.limelight.updateHeadingError()
            robot.limelight.updateDistance()

            val targetRpm = robot.shooter.neededRpm(robot.distanceFromGoal(side))
            val targetAngle = robot.shooter.neededAngle(robot.limelight.aprilTagDistance)
            robot.shooter.hoodToPosition(targetAngle)

            if (gamepad1.left_trigger >= 0.2) {
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
                robot.intakeBalls().schedule()
            }
            if (stopIntake.wasJustPressed()) {
                robot.allStopCommand().schedule()
            }

            if (shootBalls.wasJustPressed()) {
                robot.shootBalls(targetRpm).schedule()
            }

            if (ejectBalls.wasJustPressed()) {
                robot.ejectBalls().schedule()
            }

            if (startShooter.wasJustPressed()) {
                robot.shooter.goToRpm(4000.0)
            }
            if (rpmToRest.wasJustPressed()) {
                robot.shooter.goToRpm(0.0)
            }

            if (gamepad2.right_trigger > 0) {
                robot.shooter.turretPosition += 0.2 * timeKeep.deltaTime.asS
            }
            if (gamepad2.left_trigger > 0) {
                robot.shooter.turretPosition -= 0.2 * timeKeep.deltaTime.asS
            }
            if (gamepad2.right_trigger < 0.1 || gamepad2.left_trigger < 0.1) {
                robot.updateHeading(side)
            }

            if (resetOdo.wasJustPressed()) {
                robot.resetRobotPose()
            }

            robot.shooter.updateRpm(timeKeep.deltaTime)

            DrawingClone.drawDebug(robot.follower)

            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("target rpm", robot.shooter.targetRpm)
            panelsTelemetry.addData("shooter rpm", robot.shooter.shooterPower)
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("robot heading", Math.toDegrees(robot.follower.pose.heading))
            panelsTelemetry.addData("turret heading", robot.shooter.turretAngle)
            panelsTelemetry.addData("needed angle", robot.neededTurretAngle(side))
            panelsTelemetry.addData("turret position", robot.shooter.servo1.position)
            panelsTelemetry.addData("power intake", robot.intake.power)
            panelsTelemetry.addData("power transfer", robot.transfer.power)
            panelsTelemetry.addData("sensor distance", robot.transfer.distance)
            panelsTelemetry.addData("calculated rpm", targetRpm)
            panelsTelemetry.addData("calculated angle", targetAngle)
            panelsTelemetry.addData("distance camera", robot.limelight.aprilTagDistance)
            panelsTelemetry.addData("distance odometry", robot.distanceFromGoal(side))
            panelsTelemetry.update(telemetry)
        }
    }
}