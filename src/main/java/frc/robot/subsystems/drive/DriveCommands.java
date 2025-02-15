package frc.robot.subsystems.drive;

import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.FieldCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Limelight;

public class DriveCommands {

    static final double deadbandLow = -5;
    static final double deadbandHigh = 5;

    public static Command allignTag(double distance, int tagID, CommandSwerveDrivetrain drivetrain, Limelight limelight, SwerveRequest.FieldCentric request) {
        return Commands.run(() -> {
            double[] limelightTagPos = limelight.getTagPos();

            if (limelightTagPos[4] < deadbandLow || limelightTagPos[4] > deadbandHigh) {
                
                System.out.println("outside deadband!!!");

                if (limelightTagPos[4] > 0) { //if rotated positive (right)
                //turn the robot left
                System.out.println("Turning left!!!!!!!!!!!");
                drivetrain.applyRequest(() -> 
                    request.withRotationalRate(0.5));
                } else {
                //turn the robot right
                System.out.println("Turning right!!!!!!!!!!!");
                drivetrain.applyRequest(() -> 
                    request.withRotationalRate(-0.5));
                }
            }
            System.out.println(limelightTagPos[4]);
        });
    }
}
