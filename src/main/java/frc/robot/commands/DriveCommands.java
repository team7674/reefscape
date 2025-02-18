package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;

import java.util.Optional;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
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

    public static Command alignToTag(
        int ID,
        CommandSwerveDrivetrain drive,
        Vision vision
    ) {
        return Commands.run(
            () -> {
                Pose2d pose = vision.getPose2d();

                Optional<Pose3d> tagPoseOption = StaticUtil.getTagFieldLayoutAM().getTagPose(ID);
                
                Pose2d tagPose = ( tagPoseOption.isPresent() ) ? tagPoseOption.get().toPose2d() : new Pose2d();

                Twist2d relativeTrans = pose.log(tagPose);

                drive.applyRequest(() -> new SwerveRequest.FieldCentric())
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
                    .withVelocityX(strafeSupplier.getAsDouble())
                    .withVelocityY(forwardSupplier.getAsDouble())
                    .withRotationalRate(rotSupplier.getAsDouble() * StaticUtil.MaxAngularRate));
        }, drive);
    }
}
