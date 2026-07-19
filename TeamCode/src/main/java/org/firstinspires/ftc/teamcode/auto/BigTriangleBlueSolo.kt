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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.LoggedOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot
import org.psilynx.psikit.ftc.wrappers.MotorWrapper
import org.psilynx.psikit.ftc.wrappers.PinpointWrapper

@Autonomous
class BigTriangleBlueSolo : LinearOpMode() {
    private val startPose = Pose(18.0, 118.0, Math.toRadians(144.0))
    // FIXED: Preload now goes to (59.5, 68)
    private val scorePreloadPose = Pose(59.5, 68.0, Math.toRadians(180.0))

    // Middle Poses
    private val intakeMiddlePose = Pose(19.0, 59.0, Math.toRadians(180.0))
    private val shootMiddlePose = Pose(59.5, 68.0, Math.toRadians(180.0))

    // Gate Poses
    private val gateApproachPose = Pose(18.0, 65.0, Math.toRadians(180.0))
    private val gateRamPose = Pose(14.0, 48.0, Math.toRadians(150.0))
    private val turnToWall = Pose(13.0, 55.0, Math.toRadians(180.0))
    private val bigTriangleShootPose = Pose(59.5, 68.0, Math.toRadians(180.0))

    // Close Poses (Moved to the end)
    private val intakeClosePose = Pose(22.0, 84.0, Math.toRadians(180.0))
    private val shootClosePose = Pose(42.0, 84.0, Math.toRadians(180.0))

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

        // MIDDLE is now first after preload
        intakeMiddle = robot.follower.pathBuilder()
            // FIXED: Control point changed to (58, 58) per JSON
            .addPath(BezierCurve(scorePreloadPose, Pose(58.0, 58.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(35.0, 60.0), shootMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        // GATE Sequence
        openGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(shootMiddlePose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateApproachPose, Pose(30.0, 53.0), Pose(15.0, 58.0), gateRamPose))
            .setLinearHeadingInterpolation(
                gateApproachPose.heading,
                gateRamPose.heading
            )
            .setTValueConstraint(0.90)
            .setTimeoutConstraint(250.0)
            .build()

        turnGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateRamPose, Pose(18.0, 55.0), turnToWall))
            .setLinearHeadingInterpolation(
                gateRamPose.heading,
                turnToWall.heading
            )
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(turnToWall, Pose(37.0, 50.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        // CLOSE sequence happens last, linking from bigTriangleShootPose
        intakeClose = robot.follower.pathBuilder()
            // FIXED: Control point added (50, 83) per JSON
            .addPath(BezierCurve(bigTriangleShootPose, Pose(50.0, 83.0), intakeClosePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootClose = robot.follower.pathBuilder()
            // FIXED: Now a direct BezierLine to shootClosePose
            .addPath(BezierLine(intakeClosePose, shootClosePose))
            .setConstantHeadingInterpolation(Math.PI)
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

        // --- MOVED CLOSE LINE TO END ---
        robot.allStartCommand(),

        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(3600.0)
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
        robot.shooter.turretPosition = 0.67
        robot.shooter.hoodToPosition(hoodFar)

        val localizer = robot.follower.poseTracker.localizer as? PinpointLocalizer
        val wrapper = localizer?.pinpoint as? PinpointWrapper
        val drivetrain = robot.follower.drivetrain as? Mecanum
        val motors = drivetrain?.motors?.map { it as? MotorWrapper }

        while (opModeIsActive()) {
//            wrapper?.cacheResets?.forEach { it() }
//            motors?.forEach { it?.cacheResets?.forEach { it() } }

            robot.follower.update()
            robot.limelight.updateDistance()
            robot.limelight.updateLimelightPose()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)
            robot.shooter.hoodToPosition(robot.shooter.neededAngle(robot.distanceFromGoal(Robot.Side.BLUE)))

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