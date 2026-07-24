package org.firstinspires.ftc.teamcode.auto

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.ftc.drivetrains.Mecanum
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer
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
import com.qualcomm.robotcore.eventloop.opmode.LoggedOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.psilynx.psikit.ftc.HardwareMapWrapper
import org.psilynx.psikit.ftc.wrappers.MotorWrapper
import org.psilynx.psikit.ftc.wrappers.PinpointWrapper

@Autonomous
class SmallTriangleBlueHuman : LoggedOpMode() {
    private val startPose = Pose(55.0, 9.0, Math.toRadians(90.0))
    private val scorePreloadPose = Pose(55.0, 12.0, Math.toRadians(90.0))
    private val intakeFarPose = Pose(12.0, 35.0, Math.toRadians(180.0))
    private val intakeHumanPose = Pose(12.0, 9.5, Math.toRadians(170.0))
    private val kindaBetweenPose = Pose(12.0, 24.0, Math.toRadians(180.0))
    private val smallTriangleShootPose = Pose(42.0, 9.5, Math.toRadians(180.0))

    private lateinit var robot : Robot

    private lateinit var scorePreload: PathChain
    private lateinit var intakeFar: PathChain
    private lateinit var shootFar: PathChain
    private lateinit var intakeHuman: PathChain
    private lateinit var kindaBetween: PathChain
    private lateinit var shootHuman: PathChain

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

        // Shuffles from human intake pose to the next coordinate
        kindaBetween = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeHumanPose, Pose(20.0, 15.0), kindaBetweenPose))
            .setLinearHeadingInterpolation(intakeHumanPose.heading, kindaBetweenPose.heading)
            .build()

        shootHuman = robot.follower.pathBuilder()
            .addPath(BezierCurve(kindaBetweenPose, Pose(20.0, 15.0), smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        // Preload
        parallel(
            follow(robot.follower, scorePreload),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.38)
        ),
        robot.shootBallsFar(4300.0, 0.46),

        // Far Line
        race(
            follow(robot.follower, intakeFar),
            robot.intakeBallsAuto()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.75)
        ),
        robot.shootBallsFar(4300.0, 0.46),

        // cycle 1
        race(
            sequential(
                follow(robot.follower, intakeHuman),
                follow(robot.follower, kindaBetween)
            ),
            robot.intakeBallsAuto()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.75)
        ),
        robot.shootBallsFar(4300.0, 0.46),

        // cycle 2
        race(
            sequential(
                follow(robot.follower, intakeHuman),
                follow(robot.follower, kindaBetween)
            ),
            robot.intakeBallsAuto()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.75)
        ),
        robot.shootBallsFar(4300.0, 0.46),

        // cycle 3
        race(
            sequential(
                follow(robot.follower, intakeHuman),
                follow(robot.follower, kindaBetween)
            ),
            robot.intakeBallsAuto()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.75)
        ),
        robot.shootBallsFar(4300.0, 0.46),

        // cycle 4
        race(
            sequential(
                follow(robot.follower, intakeHuman),
                follow(robot.follower, kindaBetween)
            ),
            robot.intakeBallsAuto()
        ),
        robot.allStopCommand(),

        parallel(
            follow(robot.follower, shootHuman),
            robot.rpmAndAngleTo(4300.0, 0.46),
            robot.shooter.turretToPosition(0.75)
        ),
        robot.shootBallsFar(4300.0, 0.46),

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

        val localizer = robot.follower.poseTracker.localizer as? PinpointLocalizer
        val wrapper = localizer?.pinpoint as? PinpointWrapper
        val drivetrain = robot.follower.drivetrain as? Mecanum
        val motors = drivetrain?.motors?.map { it as? MotorWrapper }

        while (opModeIsActive()) {
            wrapper?.cacheResets?.forEach { it() }
            motors?.forEach { it?.cacheResets?.forEach { it() } }

            robot.follower.update()
            timeKeep.resetDeltaTime()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)

            robot.shooter.updateRpm(timeKeep.deltaTime)

            Scheduler.execute()

            panelsTelemetry.addData("pos", robot.follower.pose)
            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("motor intake", robot.intake.power)
            panelsTelemetry.addData("motor transfer", robot.transfer.power)
            panelsTelemetry.addData("wait for rpm", robot.shooter.shooterBusy())
            panelsTelemetry.addData("delta time", timeKeep.deltaTime)
            panelsTelemetry.update(telemetry)
        }
    }
}