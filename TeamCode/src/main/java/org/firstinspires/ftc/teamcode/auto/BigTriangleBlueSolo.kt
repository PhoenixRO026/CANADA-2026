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
    private val scorePreloadPose = Pose(42.0, 94.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(21.0, 88.0, Math.toRadians(180.0))
    private val closeShootPose = Pose(47.0, 85.0, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(18.0, 57.0, Math.toRadians(180.0))
    private val gateApproachPose = Pose(19.0, 65.0, Math.toRadians(180.0))
    private val gateRamPose = Pose(14.0, 52.0, Math.toRadians(120.0))
    private val bigTriangleShootPose = Pose(44.5, 82.5, Math.toRadians(180.0))
    private val turnToWall = Pose(15.0, 46.0, Math.toRadians(120.0))

    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var shootClose: PathChain
    private lateinit var shootMiddle: PathChain
    private lateinit var intakeMiddle: PathChain
    private lateinit var openGate: PathChain
    private lateinit var intakeGate: PathChain
    private lateinit var shootGate: PathChain

    private fun buildPaths() {
        scorePreload = robot.follower.pathBuilder()
            .addPath(BezierLine(startPose, scorePreloadPose))
            .setLinearHeadingInterpolation(startPose.heading, scorePreloadPose.heading)
            .setTranslationalConstraint(0.07)
            .build()

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(43.0, 84.0), intakeClosePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, closeShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(closeShootPose, Pose(48.0, 53.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(39.0, 59.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .setTranslationalConstraint(0.07)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(BezierLine(gateApproachPose, gateRamPose))
            .setLinearHeadingInterpolation(gateApproachPose.heading, gateRamPose.heading)
            .addPath(BezierLine(gateRamPose, turnToWall))
            .setLinearHeadingInterpolation(gateRamPose.heading, turnToWall.heading)
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierLine(gateRamPose, bigTriangleShootPose))
            .setLinearHeadingInterpolation(gateRamPose.heading, bigTriangleShootPose.heading)
            .setTranslationalConstraint(0.07)
            .build()
    }

    fun autoRoutine() : Command = Groups.sequential(
        // Preload
        Groups.parallel(
            robot.shooter.goToRpmCommand(robot.shooter.neededRpm(125.0)),
            PedroCommands.follow(robot.follower, scorePreload)
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),

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
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),

        // Middle Line
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        robot.allStartCommand(),

        // Gate Cycles (1 through 5)
        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeGate),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        robot.allStartCommand(),

        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeGate),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        robot.allStartCommand(),

        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeGate),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        robot.allStartCommand(),

        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeGate),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
        robot.allStartCommand(),

        PedroCommands.follow(
            robot.follower,
            openGate
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeGate),
            robot.intakeBallsAuto()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
        robot.resetRobotPoseCommand(),
    )

    override fun runOpMode() {
        val panelsTelemetry = PanelsTelemetry.telemetry
        robot = Robot(hardwareMap, startPose)

        Scheduler.reset()
        buildPaths()
        val timeKeep = TimeKeep()

        waitForStart()

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