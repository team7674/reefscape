package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.Vision;
//import frc.robot.util.StaticUtil;

/*

// Make class inextendable
public final class DriveCommands {

    static double targetAngle;

    // Make class uninstantiable
    private DriveCommands() {}

    private static final double THROTTLE_GOV = 2;

    public static Command alignToAngle(double angle, CommandSwerveDrivetrain drive) {

        Rotation2d target = Rotation2d.fromDegrees(angle); //converts our double angle to a rotation2d

        return Commands.run(() -> {
            drive.applyRequest(() -> new SwerveRequest.FieldCentricFacingAngle().withTargetDirection(target));
            System.out.println("working? " + target);
        }, drive);
        //return //slaps the target into a swerverequest
    }

    public static Command alignToTag(
        int ID,
        CommandSwerveDrivetrain drive,
        Vision vision
    ) {
        return Commands.run(
            () -> {
                //Pose2d pose = vision.getPose2d(drive);

                /*
                try { //may have a bad tagpose or no tagpose, this will grab either the valid tag or the last tag that exists on the field
                    Pose3d tagPose3d = StaticUtil.getTagFieldLayoutAM().getTags().get(ID).pose; //grabs list of tags, pulls the ID we have, then gets its pose3d
                } catch (NullPointerException e) { //handles exception
                    List<AprilTag> tags = StaticUtil.getTagFieldLayoutAM().getTags(); //grabs all the tags
                    Pose3d tagPose3d = tags.get(tags.size()-1).pose; //sets heading of specifically the last tag, aka list size minus 1, aka 22 (this could just be the number 22 but doing it like this makes it work for different list sizes)
                    System.out.println(e); //prints error
                }

                

                Rotation2d somepointBS = Rotation2d.fromDegrees(90);

                SwerveRequest.FieldCentricFacingAngle req = new SwerveRequest.FieldCentricFacingAngle()
                .withTargetDirection(somepointBS);

                System.out.println("we're trying to do something");

                req.HeadingController.setPID(0.8, 0.0025, 0.0);

                //Commands.run(drive.applyRequest(() -> req), drive);
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
*/