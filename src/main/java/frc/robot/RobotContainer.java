// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import javax.sql.rowset.JoinRowSet;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.ArmCommands;
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

    private final double kWinchRate = 0.5; // Half input

    private boolean manualMode = false;
    private final Trigger manualTrigger = new Trigger(() -> manualMode);

    private Vision vision = new Vision("upper_camera");

    //private ElevatorWinch elevatorWinch = new ElevatorWinch();
    private Coral coral = new Coral();

    private Arm arm = new Arm();

    private final Trigger coralTrigger = new Trigger(() -> coral.containsCoral);
    private final Trigger enableTrigger = new Trigger(() -> DriverStation.isEnabled());

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final SwerveRequest.RobotCentric driveBot = new SwerveRequest.RobotCentric();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController driverController = new CommandXboxController(0);
    private final CommandXboxController operatorController = new CommandXboxController(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    //Limelight limelight = new Limelight(drivetrain);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.

        // FIXME: UNCOMMENT THIS WHEN WE ARE DONE DEBUGGING
        
        
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(MathUtil.applyDeadband(driverController.getLeftY(), 0.15) * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(MathUtil.applyDeadband(driverController.getLeftX(), 0.15) * MaxSpeed) // Drive left with negative X (left)
                  .withRotationalRate(-MathUtil.applyDeadband(driverController.getRightX(), 0.15) * MaxAngularRate) // Drive counterclockwise with negative X (left)
                )
        );

        driverController.leftBumper().onTrue(Commands.runOnce(() -> manualMode = true));
        driverController.leftBumper().onFalse(Commands.runOnce(() -> manualMode = false));

        manualTrigger.onTrue(Commands.run(() -> {}));
        manualTrigger.whileTrue(Commands.run(() -> {
            if (driverController.getLeftX() > 0.1 || driverController.getLeftX() < -0.1)
                arm.driveArmPositional(driverController.getLeftX());

            if (driverController.getRightX() > 0.1 || driverController.getRightX() < -0.1)
                arm.driveWristPositional(driverController.getRightX());
        }));

        //driverController.b().onTrue(drivetrain.applyRequest(() -> brake));
        //driverController.x().whileTrue(Commands.run(() -> arm.runTilt(driverController.getLeftX())));

        driverController.povDown().onTrue(ArmCommands.level1(arm));
        driverController.povLeft().onTrue(ArmCommands.level2(arm));
        driverController.povUp().onTrue(ArmCommands.level3(arm));
        driverController.povRight().onTrue(ArmCommands.level4(arm));

        driverController.a().onTrue(ArmCommands.travel(arm));
        driverController.b().onTrue(ArmCommands.algaeLevel1(arm));
        driverController.x().onTrue(ArmCommands.intakeCoral(arm, coral));
        driverController.y().onTrue(ArmCommands.algaeLevel2(arm));

        //driverController.y().whileTrue(Commands.runEnd(() -> coral.output(), () -> coral.output()));
        //joystick.x().whileTrue(Commands.runOnce(() -> coral.intake()));

        coralTrigger.onChange(StaticUtil.rumbleController(driverController, 0.5));

        //driverController.rightBumper().onTrue(ArmCommands.travel(arm));

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
        driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        //resets odometry with button press
        //driverController.start().and(driverController.back()).whileTrue(Commands.runOnce(() -> 
        //    drivetrain.addVisionMeasurement(limelight.getLimelightPose(), StaticUtil.getCurrentRioTimestamp())));

        // reset the field-centric heading on left bumper press
        //driverController.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));
        
        //Rotation2d target = Rotation2d.fromDegrees(90);
        //joystick.rightBumper().whileTrue(drivetrain.applyRequest(() -> new SwerveRequest.FieldCentricFacingAngle().withTargetDirection(target)));
            

            //drivetrain.applyRequest(() -> new SwerveRequest.FieldCentricFacingAngle().withTargetDirection(target));

        drivetrain.registerTelemetry(logger::telemeterize);

        enableTrigger.onTrue(ArmCommands.travel(arm));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No Such auto");
    }
}