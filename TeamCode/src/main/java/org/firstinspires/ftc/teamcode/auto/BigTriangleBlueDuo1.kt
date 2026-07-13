package org.firstinspires.ftc.teamcode.auto

import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.groups.Groups.race
import com.pedropathing.ivy.pedro.PedroCommands
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class BigTriangleBlueDuo1 : LinearOpMode() {
    private val startPose = Pose(18.0, 118.0, Math.toRadians(144.0))
    private val scorePreloadPose = Pose(40.0, 95.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(20.0, 84.0, Math.toRadians(180.0))
    private val closeShootPose = Pose(44.5, 83.0, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(15.0, 59.0, Math.toRadians(180.0))
    private val gateApproachPose = Pose(25.0, 66.0, Math.toRadians(180.0))

    // UPDATED: Shifted X coordinates out to 15.0 to align with the expanded wall clearance layout
    private val gateRamPose = Pose(15.5, 46.0, Math.toRadians(155.0))
    private val gateTurnPose = Pose(15.5, 45.7, Math.toRadians(180.0))
    private val middleShootPose = Pose(49.5, 77.5, Math.toRadians(180.0))
    private val gateShootPose = Pose(49.5, 77.5, Math.toRadians(180.0))

    // First Cycle Paths (from middle shoot)
    private lateinit var intakeGateFirstApproach: PathChain

    // Subsequent Cycle Paths (from gate shoot)
    private lateinit var intakeGateSubsequentApproach: PathChain

    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var shootClose: PathChain
    private lateinit var shootMiddle: PathChain
    private lateinit var intakeMiddle: PathChain
    private lateinit var gateTurn: PathChain
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
            .addPath(BezierCurve(intakeMiddlePose, Pose(35.0, 60.0), middleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        // --- FIRST CYCLE TO GATE ---
        intakeGateFirstApproach = robot.follower.pathBuilder()
            .addPath(BezierCurve(middleShootPose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            .addPath(BezierCurve(gateApproachPose, Pose(30.0, 48.0), Pose(13.0, 62.0), gateRamPose))
            .setLinearHeadingInterpolation(gateApproachPose.heading, gateRamPose.heading)
            .build()

        // --- SUBSEQUENT CYCLE TO GATE ---
        intakeGateSubsequentApproach = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateShootPose, Pose(35.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(Math.PI)
            .addPath(BezierCurve(gateApproachPose, Pose(30.0, 48.0), Pose(13.0, 62.0), gateRamPose))
            .setLinearHeadingInterpolation(gateApproachPose.heading, gateRamPose.heading)
            .build()

        gateTurn = robot.follower.pathBuilder()
            .addPath(BezierLine(gateRamPose, gateTurnPose))
            .setLinearHeadingInterpolation(gateRamPose.heading, gateTurnPose.heading)
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateTurnPose, Pose(47.0, 47.0), gateShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        // Preload
        parallel(
            robot.shooter.goToRpmCommand(robot.shooter.neededRpm(120.0)),
            follow(robot.follower, scorePreload)
        ),
        robot.shootBallsAuto(),

        // Close Line
        parallel(
            follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        // Middle Line
        parallel(
            PedroCommands.follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        // First Gate Cycle

        race(
            follow(robot.follower, intakeGateFirstApproach),
            waitMs(2200.0)
        ),
        robot.intakeBalls(),
        follow(robot.follower, gateTurn),
        parallel(
            follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        // Subsequent Gate Cycle

        race(
            follow(robot.follower, intakeGateSubsequentApproach),
            waitMs(2200.0)
        ),
        robot.intakeBalls(),

        follow(robot.follower, gateTurn),
        parallel(
            follow(robot.follower, shootGate),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        // Subsequent Gate Cycle 2

            race(
                follow(robot.follower, intakeGateSubsequentApproach),
                waitMs(2200.0)
            ),
        robot.intakeBalls(),
    follow(robot.follower, gateTurn),
        parallel(
        follow(robot.follower, shootGate),
        robot.allStopCommand(),
        robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto()
    )

    override fun runOpMode() {
        robot = Robot(hardwareMap, startPose)

        Scheduler.reset()
        buildPaths()
        val timeKeep = TimeKeep()

        waitForStart()

        robot.shooter.closeFinger()
        robot.shooter.turretGoToAngle(0.0)

        Scheduler.schedule(autoRoutine())

        while (opModeIsActive()) {
            robot.follower.update()
            robot.limelight.updateDistance()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)
            val autoRpm = robot.shooter.neededRpm(goalDist)
            val autoAngle = robot.shooter.neededAngle(robot.limelight.aprilTagDistance)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)

            Scheduler.execute()
        }
    }
}