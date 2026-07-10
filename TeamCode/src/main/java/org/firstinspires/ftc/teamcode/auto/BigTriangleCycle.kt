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
    private val intakeMiddlePose = Pose()
    private val bigTriangleShootPose = Pose(44.5, 82.5, Math.toRadians(180.0))

    private lateinit var robot : Robot
    private lateinit var scorePreload: PathChain
    private lateinit var intakeClose: PathChain
    private lateinit var returnToShoot: PathChain

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
                    Pose(47.34, 80.61),
                    intakeClosePose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        returnToShoot = robot.follower.pathBuilder()
            .addPath(
                BezierLine(
                    intakeClosePose,
                    bigTriangleShootPose
                )
            )
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        parallel (
            robot.shooter.goToRpmCommand(Shooter.ShooterConfig.rpmFar),
                follow(robot.follower, scorePreload),
            ),
        robot.shootBallsAuto(Shooter.ShooterConfig.rpmFar),
        parallel(
            follow(robot.follower, intakeClose),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, returnToShoot),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(Shooter.ShooterConfig.rpmFar),
        ),
        robot.shootBallsAuto(Shooter.ShooterConfig.rpmFar)
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
            Scheduler.execute()
            robot.shooter.updateRpm(timeKeep.deltaTime)
            robot.updateHeading(Robot.Side.BLUE)
        }
    }
}