package org.firstinspires.ftc.teamcode.auto

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
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
    private val scorePreloadPose = Pose(40.0, 95.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(22.0, 84.0, Math.toRadians(180.0))
    private val closeShootPose = Pose(44.5, 83.0, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(17.0, 59.0, Math.toRadians(180.0))
    private val gateApproachPose = Pose(23.5, 66.0, Math.toRadians(180.0))
    private val gateRamPose = Pose(12.0, 46.0, Math.toRadians(150.0))
    private val bigTriangleShootPose = Pose(44.5, 82.5, Math.toRadians(180.0))

    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var shootClose: PathChain
    private lateinit var shootMiddle: PathChain
    private lateinit var intakeMiddle: PathChain
    private lateinit var intakeGate: PathChain
    private lateinit var shootGate: PathChain

    private fun buildPaths() {
        scorePreload = robot.follower.pathBuilder()
            .addPath(BezierLine(startPose, scorePreloadPose))
            .setLinearHeadingInterpolation(startPose.heading, scorePreloadPose.heading)
            .build()

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(43.0, 84.0), intakeClosePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, closeShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(closeShootPose, Pose(48.0, 53.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(35.0, 60.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            // UPDATED: First control point of the cubic Bezier shifted to (30.0, 48.0)
            .addPath(BezierCurve(gateApproachPose, Pose(30.0, 48.0), Pose(11.0, 62.0), gateRamPose))
            .setLinearHeadingInterpolation(gateApproachPose.heading, gateRamPose.heading)
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierLine(gateRamPose, bigTriangleShootPose))
            .setLinearHeadingInterpolation(gateRamPose.heading, bigTriangleShootPose.heading)
            .build()
    }

    fun autoRoutine() : Command = Groups.sequential(
        // Preload
        Groups.parallel(
            robot.shooter.goToRpmCommand(robot.shooter.neededRpm(125.0)),
            PedroCommands.follow(robot.follower, scorePreload)
        ),
        robot.shootBallsAuto(),

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

        // Gate Cycles (1 through 5)
        Groups.parallel(PedroCommands.follow(robot.follower, intakeGate), robot.intakeBalls()),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        Groups.parallel(PedroCommands.follow(robot.follower, intakeGate), robot.intakeBalls()),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        Groups.parallel(PedroCommands.follow(robot.follower, intakeGate), robot.intakeBalls()),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        Groups.parallel(PedroCommands.follow(robot.follower, intakeGate), robot.intakeBalls()),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        Groups.parallel(PedroCommands.follow(robot.follower, intakeGate), robot.intakeBalls()),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto()
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
            panelsTelemetry.update(telemetry)
        }
    }
}