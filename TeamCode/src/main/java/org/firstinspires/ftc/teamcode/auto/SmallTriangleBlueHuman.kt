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
class SmallTriangleBlueHuman : LinearOpMode() {
    private val startPose = Pose(55.7, 9.0, Math.toRadians(90.0))
    private val intakeFarPose = Pose(23.0, 35.0, Math.toRadians(180.0))
    private val intakeHumanPose = Pose(16.0, 10.0, Math.toRadians(180.0))
    private val smallTriangleShootPose = Pose(55.7, 25.6, Math.toRadians(180.0))
    private val intakeBetweenPose = Pose(12.0, 25.0, Math.toRadians(180.0))

    private val shootFarRPM = 5400.0
    private val hoodFar = 0.6
    private val turretPoseFar = 0.6

    private lateinit var robot : Robot

    private lateinit var scorePreload: PathChain
    private lateinit var intakeFar: PathChain
    private lateinit var  shootFar: PathChain
    private lateinit var intakeHuman: PathChain
    private lateinit var  shootHuman: PathChain
    private lateinit var intakeBetween: PathChain
    private lateinit var shootBetween: PathChain


    private fun buildPaths() {
        scorePreload = robot.follower.pathBuilder()
            .addPath(BezierLine(startPose, smallTriangleShootPose))
            .setLinearHeadingInterpolation(startPose.heading, smallTriangleShootPose.heading)
            .build()

        intakeFar = robot.follower.pathBuilder()
            .addPath(BezierCurve(smallTriangleShootPose, Pose(54.5, 37.0), intakeFarPose))
            .setLinearHeadingInterpolation(smallTriangleShootPose.heading, intakeFarPose.heading)
            .build()

        shootFar = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeFarPose, smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeHuman = robot.follower.pathBuilder()
            .addPath(BezierLine(smallTriangleShootPose, intakeHumanPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        shootHuman = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeHumanPose, smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()

        intakeBetween = robot.follower.pathBuilder()
            .addPath(BezierCurve(smallTriangleShootPose, Pose(40.0, 26.5), intakeBetweenPose))
            .build()

        shootBetween = robot.follower.pathBuilder()
            .addPath(BezierLine(intakeBetweenPose, smallTriangleShootPose))
            .setConstantHeadingInterpolation(Math.PI)
            .build()
    }

    fun autoRoutine() : Command = sequential (
        parallel(
            follow(robot.follower, scorePreload),
            robot.shooter.goToRpmCommand(shootFarRPM)
        ),
        robot.shootBallsAuto(shootFarRPM),

        //Far Line
        parallel(
            follow(robot.follower, intakeFar),
            robot.intakeBalls()
        ),
        parallel(
            follow(robot.follower, shootFar),
            robot.allStopCommand(),
            robot.shooter.goToRpmCommand(shootFarRPM)
        ),
        robot.shootBallsAuto(shootFarRPM),

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
            robot.shooter.goToRpmCommand(shootFarRPM)
        ),
        robot.shootBallsAuto(shootFarRPM),


    )

    override fun runOpMode() {
        robot = Robot(hardwareMap, startPose)

        Scheduler.reset()
        buildPaths()
        val timeKeep = TimeKeep()

        waitForStart()
        robot.shooter.turretPosition = 0.8
        robot.shooter.hoodToPosition(hoodFar)

        Scheduler.schedule(autoRoutine())

        while (opModeIsActive()) {
            robot.follower.update()

            val goalDist = robot.distanceFromGoal(Robot.Side.BLUE)

            robot.shooter.updateRpm(timeKeep.deltaTime)

            Scheduler.execute()
        }
    }
}
