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
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class BigToSmallRed : LinearOpMode() {
    private val startPose = Pose(123.0, 118.0, Math.toRadians(180.0 - 144.0))
    private val scorePreloadPose = Pose(144.0 - 42.0, 95.0, Math.toRadians(180.0 - 180.0))
    private val intakeClosePose = Pose(119.0, 82.0, Math.toRadians(180.0 - 180.0))
    private val closeShootPose = Pose(144.0 - 44.5, 83.0, Math.toRadians(180.0 - 180.0))
    private val intakeMiddlePose = Pose(129.0, 57.0, Math.toRadians(180.0 - 180.0))
    private val intakeFarPose = Pose(119.0, 35.0, Math.toRadians(180.0 - 180.0))
    private val bigTriangleShootPose = Pose(90.0, 83.0, Math.toRadians(180.0 - 180.0))
    private val intakeHumanPose = Pose(144.0 - 9.0, 8.0, Math.toRadians(180.0 - 180.0))
    private val openGatePose = Pose(144.0 - 16.3, 67.7, Math.toRadians(180.0 - 180.0))

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
            .addPath(BezierLine(startPose, bigTriangleShootPose))
            .setLinearHeadingInterpolation(startPose.heading, bigTriangleShootPose.heading)
            .build()

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierLine(bigTriangleShootPose, intakeClosePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(89.0, 50.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeMiddlePose, openGatePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(openGatePose, Pose(106.5, 60.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeFar = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(90.0, 28.0), intakeFarPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootFar = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeFarPose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeHuman = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(144.0 - 23.0, 48.0), Pose(144.0 - 39.0, 7.5), intakeHumanPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootHuman = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeHumanPose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        // Preload
        parallel (
            robot.rpmAndAngleTo(3300.0, 0.46),
            follow(robot.follower, scorePreload),
            robot.shooter.turretToPosition(0.32)
        ),
        robot.shootBallsAuto(3300.0),

        // Close Line
        parallel(
            follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(3300.0, 0.46),
            robot.shooter.turretToPosition(0.32)

        ),
        robot.shootBallsAuto(3300.0),

        // Middle Line
        parallel(
            follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootMiddle),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(3300.0, 0.46),
            robot.shooter.turretToPosition(0.32)

        ),
        robot.shootBallsAuto(3300.0),

        //Far Line
        parallel(
            follow(robot.follower, intakeFar),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.rpmAndAngleTo(3300.0, 0.46),
            robot.shooter.turretToPosition(0.32)

        ),
        robot.shootBallsAuto(3300.0),

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
            robot.rpmAndAngleTo(3300.0, 0.46),
            robot.shooter.turretToPosition(0.32)
        ),
        robot.shootBallsAuto(3300.0),
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

            val goalDist = robot.distanceFromGoal(Robot.Side.RED)
            val autoRpm = robot.shooter.neededRpm(goalDist)
            val autoAngle = robot.shooter.neededAngle(robot.limelight.aprilTagDistance)

            robot.shooter.updateRpm(timeKeep.deltaTime)

            Scheduler.execute()

            panelsTelemetry.addData("rpm", robot.shooter.currentRpm)
            panelsTelemetry.update(telemetry)
        }
        robot.rememberPose()
    }
}