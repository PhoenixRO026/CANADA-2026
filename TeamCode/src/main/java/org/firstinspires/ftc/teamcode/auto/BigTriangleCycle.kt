package org.firstinspires.ftc.teamcode.auto

import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.groups.Groups.race
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower
import org.firstinspires.ftc.teamcode.robot.Robot
import org.firstinspires.ftc.teamcode.robot.Shooter


@Autonomous
class BigTriangleCycle : LinearOpMode() {
    private val startPose = Pose(18.0, 118.0, Math.toRadians(144.0))
    private val scorePreloadPose = Pose(40.0, 95.0, Math.toRadians(180.0))
    private val intakeClosePose = Pose(20.0, 82.5, Math.toRadians(180.0))
    private val intakeMiddlePose = Pose(16.0, 58.0, Math.toRadians(180.0))
    private val intakeGatePose = Pose(12.0, 58.0, Math.toRadians(145.0))
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
            .addPath(
                BezierLine(
                    startPose,
                    scorePreloadPose
                )
            )
            .setLinearHeadingInterpolation(
                startPose.heading,
                scorePreloadPose.heading
            )
            .build()

        intakeClose = robot.follower.pathBuilder()
            .addPath(
                BezierCurve(
                    scorePreloadPose,
                    Pose(47.34, 80.61), // control point
                    intakeClosePose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootClose = robot.follower.pathBuilder()
            .addPath(
                BezierLine(
                    intakeClosePose,
                    bigTriangleShootPose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeMiddle = robot.follower.pathBuilder()
            .addPath(
                BezierCurve(
                    bigTriangleShootPose,
                    Pose(35.0710, 60.340),
                    intakeMiddlePose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootMiddle = robot.follower.pathBuilder()
            .addPath(
                BezierLine(
                    intakeMiddlePose,
                    bigTriangleShootPose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeGate = robot.follower.pathBuilder()
            .addPath(
                BezierCurve(
                    bigTriangleShootPose,
                    Pose(35.071, 60.340),
                    intakeGatePose
                )
            )
            .setLinearHeadingInterpolation(bigTriangleShootPose.heading, intakeGatePose.heading)
            .build()

        shootGate = robot.follower.pathBuilder()
            .addPath(
                BezierLine(
                    intakeGatePose,
                    bigTriangleShootPose
                )
            )
            .setLinearHeadingInterpolation(intakeGatePose.heading, bigTriangleShootPose.heading)
            .build()
}
    fun autoRoutine() : Command = sequential (
        parallel (
            robot.shooter.goToAutoRpmCommand(),
                follow(robot.follower, scorePreload)
            ),
        robot.shootBallsAuto(),
        parallel(
            follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.shooter.goToAutoRpmCommand(),
            robot.shooter.goToAutoAngleCommand()
        ),
        robot.shootBallsAuto(),
        parallel(
            follow(robot.follower, intakeMiddle),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.shooter.goToAutoRpmCommand(),
            robot.shooter.goToAutoAngleCommand()
        ),
        robot.shootBallsAuto(),
        parallel(
            follow(robot.follower, intakeGate),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.shooter.goToAutoRpmCommand(),
            robot.shooter.goToAutoAngleCommand()
        ),
        robot.shootBallsAuto(),
        parallel(
            follow(robot.follower, intakeGate),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.shooter.goToAutoRpmCommand(),
            robot.shooter.goToAutoAngleCommand()
        ),
        robot.shootBallsAuto(),
        parallel(
            follow(robot.follower, intakeGate),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootClose),
            robot.allStopCommand(),
            robot.shooter.goToAutoRpmCommand(),
            robot.shooter.goToAutoAngleCommand()
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

            val autoRpm = robot.shooter.neededRpm(robot.limelight.aprilTagDistance)
            val autoAngle = robot.shooter.neededAngle(robot.limelight.aprilTagDistance)

            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)

            Scheduler.execute()
        }
    }
}