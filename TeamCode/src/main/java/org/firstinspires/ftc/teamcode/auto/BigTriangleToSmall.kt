package org.firstinspires.ftc.teamcode.auto

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
class BigTriangleToSmall : LinearOpMode() {
    private val startPose = Pose(18.8, 119.0, Math.toRadians(144.0))
    private val scorePreloadPose = Pose(40.0, 95.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(20.0, 84.0, Math.toRadians(180.0))
    private val closeShootPose = Pose(44.5, 83.0, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(23.0, 59.5, Math.toRadians(180.0))
    private val intakeFarPose = Pose(15.0, 35.0, Math.toRadians(180.0))
    private val bigTriangleShootPose = Pose(49.5, 77.5, Math.toRadians(180.0))
    private val intakeHumanPose = Pose(9.0, 8.0, Math.toRadians(180.0))
    private val openGatePose = Pose(16.3, 67.7, Math.toRadians(180.0))

    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var shootClose: PathChain
    private lateinit var shootMiddle: PathChain
    private lateinit var intakeMiddle: PathChain
    private lateinit var intakeFar: PathChain
    private lateinit var  shootFar: PathChain
    private lateinit var intakeHuman: PathChain
    private lateinit var  shootHuman: PathChain
    private lateinit var  openGate: PathChain


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
            .addPath(BezierCurve(closeShootPose, Pose(55.0, 56.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeMiddlePose, openGatePose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(openGatePose, Pose(39.0, 69.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeFar = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(54.5, 27.0), intakeFarPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootFar = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeFarPose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeHuman = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(23.0, 48.0), Pose(39.0, 7.5), intakeHumanPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootHuman = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeHumanPose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        // Preload
        parallel (
            robot.shooter.goToRpmCommand(robot.shooter.neededRpm(125.0)),
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
            follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        //Far Line
        parallel(
            follow(robot.follower, intakeFar),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),

        //Human Line
        race(
            parallel(
                follow(robot.follower, intakeHuman),
                robot.intakeBalls()
            ),
            waitMs(3500.0)
        ),
        robot.allStopCommand(),
        parallel(
            follow(robot.follower, shootHuman),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.BLUE))
        ),
        robot.shootBallsAuto(),
    )

    override fun runOpMode() {
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
            val autoAngle = robot.shooter.neededAngle(robot.limelight.aprilTagDistance)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)

            Scheduler.execute()
        }
    }
}
