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
class BigTriangleRedSolo : LinearOpMode() {
    // X mirrored: 144 - blueX | Heading mirrored: 180 - blueHeading
    private val startPose = Pose(126.0, 118.0, Math.toRadians(36.0))
    private val scorePreloadPose = Pose(92.0, 84.0, Math.toRadians(0.0))
    private val intakeClosePose = Pose(122.0, 84.0, Math.toRadians(0.0))
    private val intakeMiddlePose = Pose(125.0, 59.0, Math.toRadians(0.0))
    private val gateApproachPose = Pose(126.5, 65.0, Math.toRadians(0.0))
    private val gateRamPose = Pose(130.0, 48.0, Math.toRadians(30.0))
    private val turnToWall = Pose(131.0, 55.0, Math.toRadians(0.0))
    private val bigTriangleShootPose = Pose(84.5, 68.0, Math.toRadians(0.0))

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

        intakeClose = robot.follower.pathBuilder()
            .addPath(BezierCurve(scorePreloadPose, Pose(101.0, 84.0), intakeClosePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeClosePose, bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .setTranslationalConstraint(0.07)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(96.0, 53.0), intakeMiddlePose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(BezierCurve(intakeMiddlePose, Pose(109.0, 60.0), bigTriangleShootPose))
            .setConstantHeadingInterpolation(0.0)
            .setTranslationalConstraint(0.07)
            .build()

        openGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(bigTriangleShootPose, Pose(109.0, 65.0), gateApproachPose))
            .setConstantHeadingInterpolation(0.0)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(
                BezierCurve(
                    gateApproachPose,
                    Pose(114.0, 53.0),
                    Pose(129.0, 58.0),
                    gateRamPose
                )
            )
            .setLinearHeadingInterpolation(
                gateApproachPose.heading,
                gateRamPose.heading
            )
            .setTValueConstraint(0.90)
            .setTimeoutConstraint(250.0)
            .build()

        turnGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(gateRamPose, Pose(126.0, 55.0), turnToWall))
            .setLinearHeadingInterpolation(
                gateRamPose.heading,
                turnToWall.heading
            )
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(BezierCurve(turnToWall, Pose(107.0, 50.0), bigTriangleShootPose))
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
        robot.shootBallsAuto(),
        // Close Line
        Groups.parallel(
            PedroCommands.follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        Groups.parallel(
            PedroCommands.follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.goToRpmAndAngleCommand(robot.distanceFromGoal(Robot.Side.RED))
        ),
        robot.shootBallsAuto(3600.0),
        // Middle Line
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

        // Gate Cycles (1 through 5)
        PedroCommands.follow(robot.follower, openGate),
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
        PedroCommands.follow(robot.follower, openGate),
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
        PedroCommands.follow(robot.follower, openGate),
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

        // Gate Cycles (4)
        PedroCommands.follow(robot.follower, openGate),
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

        // Gate Cycles (5)
        PedroCommands.follow(robot.follower, openGate),
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

        // FIXED: Mirrored turret heading across mid-point (1.0 - 0.67 = 0.33)
        robot.shooter.turretPosition = 0.33
        robot.shooter.hoodToPosition(hoodFar)

        while (opModeIsActive()) {
            robot.follower.update()
            robot.limelight.updateDistance()
            robot.limelight.updateLimelightPose()

            // FIXED: Target methods changed to RED side
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