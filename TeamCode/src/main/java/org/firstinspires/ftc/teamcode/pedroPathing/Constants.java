package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.FusionLocalizer;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(11.8)
            .forwardZeroPowerAcceleration(-24.792451684671523)
            .lateralZeroPowerAcceleration(-76.53178123105344)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.07, 0, 0.002, 0.02))
            .headingPIDFCoefficients(new PIDFCoefficients(0.9, 0, 0.002, 0.02))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.015, 0, 0.0002, 0.6, 0.02))
            .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.05861, 0.000196)) // (kP, kLinear, kQuadratic)
            .centripetalScaling(0.0);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1.8, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("motorRF")
            .rightRearMotorName("motorRB")
            .leftRearMotorName("motorLB")
            .leftFrontMotorName("motorLF")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(81.96077457938608)
            .yVelocity(59.05904550627461);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0.0)
            .strafePodX(0.0)
            .distanceUnit(DistanceUnit.MM)
            .hardwareMapName("odo")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
//                .setLocalizer(new FusionLocalizer(
//                        new PinpointLocalizer(hardwareMap, localizerConstants),
//                        new Pose(0.0, 0.0, 0.0),
//                        new Pose(0.0, 0.0, 0.0),
//                        new Pose(0.0, 0.0, 0.0),
//                        3
//                ))
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
