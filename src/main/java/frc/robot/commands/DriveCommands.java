package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;

import static edu.wpi.first.units.Units.Rotation;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.stream.Stream;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.Vision;
import frc.robot.util.StaticUtil;

// Make class inextendable
public final class DriveCommands {
    // Make class uninstantiable
    private DriveCommands() {}

    private static final double THROTTLE_GOV = 2;

    public static Command alignToTag(
        int ID,
        CommandSwerveDrivetrain drive,
        Vision vision
    ) {
        return Commands.run(
            () -> {
                vision.update();
                Pose2d pose = vision.getPose2d();
                Pose2d tagPose = new Pose2d();

                Optional<Pose3d> tagPoseOption = StaticUtil.getTagFieldLayoutAM().getTagPose(ID);
                    
                if ( tagPoseOption.isPresent() ) {
                    tagPose = tagPoseOption.get().toPose2d();
                } else {
                    tagPose = new Pose2d();
                }

                Rotation2d desiredHeading = tagPose.getTranslation().minus(pose.getTranslation()).getAngle();

                SwerveRequest.FieldCentricFacingAngle req = new SwerveRequest.FieldCentricFacingAngle()
                .withTargetDirection(desiredHeading);

                req.HeadingController.setPID(0.8, 0.0025, 0.0);
                req.HeadingController.enableContinuousInput(-Math.PI, Math.PI);

                drive.applyRequest(() -> req);
            }, 
            drive, vision);
    }

    public static Command drive(
        DoubleSupplier forwardSupplier, 
        DoubleSupplier strafeSupplier, 
        DoubleSupplier rotSupplier,
        DoubleSupplier throttleSupplier,
        CommandSwerveDrivetrain drive
    ) {
        return Commands.run(() -> {
            drive.applyRequest(() -> new SwerveRequest.FieldCentric()
                    .withVelocityX(strafeSupplier.getAsDouble() * (throttleSupplier.getAsDouble() * THROTTLE_GOV))
                    .withVelocityY(forwardSupplier.getAsDouble() * (throttleSupplier.getAsDouble() * THROTTLE_GOV))
                    .withRotationalRate(rotSupplier.getAsDouble() * StaticUtil.MaxAngularRate));
        }, drive);
    }
}
