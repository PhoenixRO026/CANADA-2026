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
import com.pedropathing.ivy.groups.Groups
import com.pedropathing.ivy.pedro.PedroCommands
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LoggedOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.psilynx.psikit.ftc.wrappers.Limelight3AWrapper
import org.psilynx.psikit.ftc.wrappers.MotorWrapper
import org.psilynx.psikit.ftc.wrappers.PinpointWrapper

@Autonomous
class BigTriangleRedSolo : LoggedOpMode() {
    // Mirrored start and scoring poses for Red Alliance
    private val startPose = Pose(126.0, 26.0, Math.toRadians(324.0))
    private val scorePreloadPose = Pose(84.5, 76.0, Math.toRadians(0.0))

    // Middle Poses (Mirrored)
    private val intakeMiddlePose = Pose(125.0, 85.0, Math.toRadians(0.0))
    private val shootMiddlePose = Pose(84.5, 76.0, Math.toRadians(0.0))

    // Gate Poses (Mirrored)
    private val gateApproachPose = Pose(126.0, 79.0, Math.toRadians(0.0))
    private val gateRamPose = Pose(130.0, 96.0, Math.toRadians(330.0))
    private val turnToWall = Pose(131.0, 89.0, Math.toRadians(0.0))
    private val bigTriangleShootPose = Pose(84.5, 76.0, Math.toRadians(0.0))

    // Close Poses (Mirrored)
    private val intakeClosePose = Pose(122.0, 60.0, Math.toRadians(0.0))
    private val shootClosePose = Pose(102.0, 60.0, Math.toRadians(0.0))

    private val hoodFar = 0.6
    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var shootClose: PathChain
    private lateinit var shootMiddle: PathChain
    private lateinit var intakeMiddle: PathChain
    private lateinit var openGate: PathChain
    private lateinit var intakeGate: PathChain
    private lateinit var turnGate: PathChain
    private lateinit var shootGate: PathChain

    private fun buildPaths() {
        scorePreload = robot.follower.pathBuilder()
            .addPath(BezierLine(startPose, scorePreloadPose))
            .setLinearHeadingInterpolation(startPose.heading, scorePreloadPose.heading)
            .setTranslationalConstraint(0.07)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(86.0, 86.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(109.0, 84.0), shootMiddlePose))
            .setConstantHeadingInterpolation(0.0)
            .setTranslationalConstraint(0.07)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(shootMiddlePose, Pose(109.0, 79.0), gateApproachPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateApproachPose, Pose(114.0, 91.0), Pose(129.0, 86.0), gateRamPose))
            .setLinearHeadingInterpolation(
                gateApproachPose.heading,
                gateRamPose.heading
            )
            .setTValueConstraint(0.90)
            .setTimeoutConstraint(250.0)
            .build()

        turnGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateRamPose, Pose(126.0, 89.0), turnToWall))
            .setLinearHeadingInterpolation(
                gateRamPose.heading,
                turnToWall.heading
            )
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(turnToWall, Pose(107.0, 94.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .setTranslationalConstraint(0.07)
            .build()

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(94.0, 61.0), intakeClosePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, shootClosePose))
            .setConstantHeadingInterpolation(0.0)
            .setTranslationalConstraint(0.07)
            .build()
    }

    fun autoRoutine() : Command = Groups.sequential(
        // Preload
        Groups.parallel(
            robot.shooter.goToRpmCommand(robot.shooter.neededRpm(125.0)),
            PedroCommands.follow(robot.follower, scorePreload)
        ),
        robot.shootBallsAuto(3500.0),

        // 1. Middle Line
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(3600.0)
        ),
        robot.shootBallsAuto(3600.0),
        robot.allStartCommand(),

        // Gate Cycles (1)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(200.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(3600.0)
        ),
        robot.shootBallsAuto(3600.0),
        robot.allStartCommand(),

        // Gate Cycles (2)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(100.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(3600.0)
        ),
        robot.shootBallsAuto(3600.0),
        robot.allStartCommand(),

        // Gate Cycles (3)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(100.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(3600.0)
        ),
        robot.shootBallsAuto(3600.0),
        robot.allStartCommand(),

        // Close Line
        robot.allStartCommand(),

        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.RED))
        ),
        robot.shootBallsAuto(3600.0)
    )

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        robot = Robot(hardwareMap, startPose)

        robot.limelight.setPipeline(1)

        Scheduler.reset()
        buildPaths()
        val timeKeep = TimeKeep()

        waitForStart()

        robot.shooter.openFinger()
        Scheduler.schedule(autoRoutine())
        robot.shooter.turretPosition = 0.67
        robot.shooter.hoodToPosition(hoodFar)

        val localizer = robot.follower.poseTracker.localizer as? PinpointLocalizer
        val wrapper = localizer?.pinpoint as? PinpointWrapper
        val drivetrain = robot.follower.drivetrain as? Mecanum
        val motors = drivetrain?.motors?.map { it as? MotorWrapper }
        val limelight = robot.limelight.camera as? Limelight3AWrapper

        while (opModeIsActive()) {
            wrapper?.cacheResets?.forEach { it() }
            motors?.forEach { it?.cacheResets?.forEach { it() } }
            limelight?.cacheResets?.forEach { it() }

            robot.follower.update()
            robot.limelight.updateDistance()
            robot.limelight.updateLimelightPose()

            val goalDist = robot.distanceFromGoal(Robot.Side.RED)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.RED)
            robot.shooter.hoodToPosition(robot.shooter.neededAngle(robot.distanceFromGoal(Robot.Side.RED)))

            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.addData("distance from goal", goalDist)
            panelsTelemetry.addData("sensor distance", robot.transfer.distance)
            panelsTelemetry.addData("follower busy", robot.follower.isBusy)
            panelsTelemetry.addData("follower stuck", robot.follower.isRobotStuck)
            panelsTelemetry.addData("limelightPose", robot.limelight.limelightPose)
            panelsTelemetry.addData("robotPose", robot.follower.pose)
            panelsTelemetry.update(telemetry)
        }
    }
}