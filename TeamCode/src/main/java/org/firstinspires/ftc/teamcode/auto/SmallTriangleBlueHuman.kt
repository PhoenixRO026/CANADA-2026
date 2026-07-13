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
    private val startPose = Pose(55.0, 9.0, Math.toRadians(90.0))
    private val intakeFarPose = Pose(23.0, 35.0, Math.toRadians(180.0))
    private val intakeHumanPose = Pose(12.0, 8.0, Math.toRadians(180.0))
    private val smallTriangleShootPose = Pose(42.0, 8.0, Math.toRadians(180.0))
    private val intakeBetweenPose = Pose(12.0, 25.0, Math.toRadians(180.0))

    private val shootPreloadRPM = 5400.0
    private val shootFarRPM = 5200.0
    private val turretPosePreload = 0.6
    private val turretPoseFar = 0.5

    private lateinit var robot : Robot

    private lateinit var scorePreload: PathChain
    private lateinit var intakeFar: PathChain
    private lateinit var  shootFar: PathChain
    private lateinit var intakeHuman: PathChain
    private lateinit var  shootHuman: PathChain
    private lateinit var intakeBetween: PathChain
    private lateinit var shootBetween: PathChain


    private fun buildPaths() {
        intakeFar = robot.follower.pathBuilder()
            .addPath(BezierCurve(startPose, Pose(54.5, 37.0), intakeFarPose))
            .setLinearHeadingInterpolation(startPose.heading, intakeFarPose.heading)
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
