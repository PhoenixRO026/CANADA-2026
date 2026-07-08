package org.firstinspires.ftc.teamcode.teleop

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.commands.Commands.waitUntil
import com.pedropathing.ivy.groups.Groups.sequential
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
open class AutoHeadingTest : LinearOpMode() {
    open val pipeline : Int = 1

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        val robot = Robot(hardwareMap)
        Scheduler.reset()

        robot.limelight.setPipeline(pipeline)

        val timeKeep = TimeKeep()

        waitForStart()

        robot.follower.startTeleopDrive()
        robot.shooter.turretGoToAngle(0.5)
//        robot.shooter.hoodDown()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            robot.follower.update()
            robot.limelight.updateHeadingError()

            if (gamepad1.dpad_right) {
                robot.shooter.turretGoToAngle(45.0)
            }
            if (gamepad1.dpad_left) {
                robot.shooter.turretGoToAngle(-45.0)
            }
            if (gamepad1.dpad_up) {
                robot.shooter.turretGoToAngle(0.0)
            }

            Scheduler.execute()
            panelsTelemetry.addData("turret heading error", robot.limelight.headingErrorDeg)
            panelsTelemetry.addData("servo position", robot.shooter.turretPosition)
            panelsTelemetry.update(telemetry)
        }
    }
}