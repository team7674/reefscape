// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import javax.sql.rowset.JoinRowSet;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.appendage.ElevatorWinch;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.Telemetry;
import frc.robot.subsystems.vision.Vision;
import frc.robot.util.StaticUtil;

//import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Coral;


public class RobotContainer {

    private Vision vision = new Vision("upper_camera");

    //private ElevatorWinch elevatorWinch = new ElevatorWinch();
    private Coral coral = new Coral();

    private Arm arm = new Arm();

    private final Trigger coralTrigger = new Trigger(() -> coral.containsCoral);

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    Limelight limelight = new Limelight(drivetrain);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.

        // FIXME: UNCOMMENT THIS WHEN WE ARE DONE DEBUGGING
        /*
        
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(MathUtil.applyDeadband(joystick.getLeftY(), 0.15) * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(MathUtil.applyDeadband(joystick.getLeftX(), 0.15) * MaxSpeed) // Drive left with negative X (left)
                  +
                  
                  
                  
                  .withRotationalRate(-MathUtil.applyDeadband(joystick.getRightX(), 0.15) * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );
        
        */

        joystick.b().onTrue(StaticUtil.rumbleController(joystick, 0.5));
        joystick.a().whileTrue(Commands.runOnce(() -> arm.setTilt(50)));
        joystick.x().whileTrue(Commands.runOnce(() -> arm.tiltExtend()));
        joystick.y().whileTrue(Commands.runEnd(() -> coral.output(), () -> coral.output()));
        //joystick.x().whileTrue(Commands.runOnce(() -> coral.intake()));

        coralTrigger.onChange(StaticUtil.rumbleController(joystick, 0.5));

        joystick.rightBumper().whileTrue(Commands.run(() -> arm.runTilt(-joystick.getLeftY())));

        //joystick.a().whileTrue(Commands.run(() -> arm.runWristTo((-20.00 / 360.00))));
        //joystick.b().whileTrue(Commands.run(() -> arm.runWristTo((0.00 / 360.00))));
        //joystick.x().whileTrue(Commands.run(() -> arm.runWristTo((40.00 / 360.00))));


        //joystick.x().whileTrue(Commands.run(() -> arm.driveWrist(-joystick.getLeftY())));
        //joystick.b().whileTrue(Commands.run(() -> elevatorWinch.setPosition(20)));
        //joystick.x().whileTrue(Commands.run(() -> elevatorWinch.setPosition(-20)));
        //joystick.a().whileTrue(Commands.run(() -> elevatorWinch.setPosition(-20)));
        //joystick.y().whileTrue(Commands.run(() -> elevatorWinch.setEncoderToZero()));

        //joystick.b().whileTrue(drivetrain.applyRequest(() ->
        //    point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        //));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        //resets odometry with button press
        joystick.start().and(joystick.back()).whileTrue(Commands.runOnce(() -> 
            drivetrain.addVisionMeasurement(limelight.getLimelightPose(), StaticUtil.getCurrentRioTimestamp())));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        
        //Rotation2d target = Rotation2d.fromDegrees(90);
        //joystick.rightBumper().whileTrue(drivetrain.applyRequest(() -> new SwerveRequest.FieldCentricFacingAngle().withTargetDirection(target)));
            

            //drivetrain.applyRequest(() -> new SwerveRequest.FieldCentricFacingAngle().withTargetDirection(target));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}