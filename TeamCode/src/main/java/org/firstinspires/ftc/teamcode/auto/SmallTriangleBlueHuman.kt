package org.firstinspires.ftc.teamcode.auto

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.groups.Groups.race
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class SmallTriangleBlueHuman : LinearOpMode() {
    private val startPose = Pose(55.0, 9.0, Math.toRadians(90.0))
    private val scorePreloadPose = Pose(55.0, 12.0, Math.toRadians(90.0))
    private val intakeFarPose = Pose(12.0, 35.0, Math.toRadians(180.0))
    private val intakeHumanPose = Pose(12.0, 9.5, Math.toRadians(180.0))
    private val smallTriangleShootPose = Pose(42.0, 9.5, Math.toRadians(180.0))
    private val intakeBetweenPose = Pose(12.0, 25.0, Math.toRadians(180.0))

//    private val shootFarRPM = 5200.0
//    private val hoodFar = 0.83
//    private val turretPoseFar = 0.75
//    private val turretPosePreload = 0.43

    private lateinit var robot : Robot

    private lateinit var scorePreload: PathChain
    private lateinit var intakeFar: PathChain
    private lateinit var shootFar: PathChain
    private lateinit var intakeHuman: PathChain
    private lateinit var  shootHuman: PathChain
    private lateinit var intakeBetween: PathChain
    private lateinit var shootBetween: PathChain


    private fun buildPaths() {
        scorePreload = robot.follower.pathBuilder()
            .addPath(BezierLine(startPose, scorePreloadPose))
            .setLinearHeadingInterpolation(startPose.heading, scorePreloadPose.heading)
            .build()

        intakeFar = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(45.0, 30.0), intakeFarPose))
            .setLinearHeadingInterpolation(scorePreloadPose.heading, intakeFarPose.heading)
            .build()

        shootFar = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeFarPose, smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeHuman = robot.follower.pathBuilder()
            .addPath(BezierLine(smallTriangleShootPose, intakeHumanPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootHuman = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeHumanPose, Pose(20.0, 15.0), smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeBetween = robot.follower.pathBuilder()
            .addPath(BezierCurve(smallTriangleShootPose, Pose(40.0, 26.5), intakeBetweenPose))
            .build()

        shootBetween = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeBetweenPose, Pose(20.0, 14.0), smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        parallel(
            follow(robot.follower, scorePreload),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.43)
        ),
        robot.shootBallsFar(5200.0, 0.83),

        //Far Line
        parallel(
            follow(robot.follower, intakeFar),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.765)
        ),
        robot.shootBallsFar(5200.0, 0.83),

        //Human Line
        parallel(
            follow(robot.follower, intakeHuman),
            robot.intakeBalls()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.765)

        ),
        robot.shootBallsFar(5200.0, 0.83),

        // Between pose
        parallel(
            follow(robot.follower, intakeBetween),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.765)
        ),
        robot.shootBallsFar(5200.0, 0.83),

        //Human Line
        parallel(
            follow(robot.follower, intakeHuman),
            robot.intakeBalls()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.765)

        ),
        robot.shootBallsFar(5200.0, 0.83),

        //Human Line
        parallel(
            follow(robot.follower, intakeHuman),
            robot.intakeBalls()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(5200.0,0.83),
            robot.shooter.turretToPosition(0.765)

        ),
        robot.shootBallsFar(5200.0, 0.83),

    )

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        robot = Robot(hardwareMap, startPose)

        Scheduler.reset()
        buildPaths()
        val timeKeep = TimeKeep()

        waitForStart()
        robot.shooter.openFinger()
        Scheduler.schedule(autoRoutine())
        robot.shooter.turretPosition = 0.43
        robot.shooter.hoodToPosition(0.83)

        while (opModeIsActive()) {
            robot.follower.update()
            timeKeep.resetDeltaTime()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)

            robot.shooter.updateRpm(timeKeep.deltaTime)

            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("motor intake", robot.intake.power)
            panelsTelemetry.addData("motor transfer", robot.transfer.power)
            panelsTelemetry.addData("wait for rpm", robot.shooter.shooterBusy())
            panelsTelemetry.addData("delta time", timeKeep.deltaTime)
            panelsTelemetry.update(telemetry)
        }
    }
}
