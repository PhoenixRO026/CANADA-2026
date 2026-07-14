package org.firstinspires.ftc.teamcode.auto

import com.bylazar.telemetry.PanelsTelemetry
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
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class BigTriangleBlueSolo : LinearOpMode() {
    private val startPose = Pose(18.0, 118.0, Math.toRadians(144.0))
    private val scorePreloadPose = Pose(52.0, 84.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(22.0, 84.0, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(19.0, 59.0, Math.toRadians(180.0))
    private val gateApproachPose = Pose(20.0, 65.0, Math.toRadians(180.0))
    private val gateRamPose = Pose(16.0, 49.0, Math.toRadians(150.0))
    private val turnToWall = Pose(13.0, 55.0, Math.toRadians(180.0))
    private val bigTriangleShootPose = Pose(60.0, 68.0, Math.toRadians(180.0))

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

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierLine(scorePreloadPose, intakeClosePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, scorePreloadPose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(48.0, 55.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(35.0, 60.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateApproachPose, Pose(24.0, 52.0), Pose(15.0, 62.0), gateRamPose))
            .setLinearHeadingInterpolation(
                gateApproachPose.heading,
                gateRamPose.heading
            )
            .setTValueConstraint(0.90)
            .setTimeoutConstraint(250.0)
            .build()

        turnGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateRamPose, Pose(5.0, 52.0), Pose(27.0, 52.0), turnToWall))
            .setLinearHeadingInterpolation(
                gateRamPose.heading,
                turnToWall.heading
            )
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(turnToWall, Pose(40.0, 55.0), bigTriangleShootPose))
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
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
        // Close Line
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
        // Middle Line
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(195.0))
        ),
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
        robot.allStartCommand(),

        // Gate Cycles (1 through 5)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(100.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                waitMs(50.0),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(115.0))
        ),
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
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
                waitMs(50.0),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(115.0))
        ),
        Groups.parallel(
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        ),
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
                waitMs(50.0),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(115.0))
        ),
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
        robot.allStartCommand(),

        // Gate Cycles (4)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(100.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                waitMs(50.0),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(115.0))
        ),
        Groups.parallel(
            robot.shootBallsAuto(),
            robot.resetRobotPoseCommand(),
        ),
        robot.allStartCommand(),

        // Gate Cycles (5)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        waitMs(100.0),
        Groups.race(
            Groups.sequential(
                PedroCommands.follow(robot.follower, intakeGate),
                waitMs(50.0),
                PedroCommands.follow(robot.follower, turnGate),
            ),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.shooter.neededRpm(115.0))
        ),
        Groups.parallel(
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        ),
        robot.allStartCommand(),
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

        while (opModeIsActive()) {
            robot.follower.update()
            robot.limelight.updateDistance()
            robot.limelight.updateLimelightPose()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)
            val autoRpm = robot.shooter.neededRpm(goalDist)
            val autoAngle = robot.shooter.neededAngle(goalDist)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)

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