// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.commands.ArmCommands;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.drive.Telemetry;
import frc.robot.util.StaticUtil;
import frc.robot.subsystems.Coral;


public class RobotContainer {

    public static final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final Climber climber = new Climber();

    private final double kWinchRate = 0.5; // Half input

    private boolean manualMode = false;
    private final Trigger manualTrigger = new Trigger(() -> manualMode);

    //private Vision vision = new Vision();
    //private PhotonVisionClient photonVisionClient = new PhotonVisionClient("upper_camera");
    private Limelight limelight = new Limelight(drivetrain);

    //private ElevatorWinch elevatorWinch = new ElevatorWinch();
    private Coral coral = new Coral();
    private Arm arm = new Arm();

    private final Trigger coralTrigger = new Trigger(() -> coral.containsCoral);
    private final Trigger enableTrigger = new Trigger(() -> DriverStation.isEnabled());

    public static double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    private final double slowMode = 2;
    private final double fastMode = 1;    

    /* Setting up bindings for necessary control of the swerve drive platform */
    public static final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    public static final SwerveRequest.RobotCentric driveBot = new SwerveRequest.RobotCentric();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController driverController = new CommandXboxController(0);
    private final CommandXboxController operatorController = new CommandXboxController(1);

    public static Trigger leftSideReef = new Trigger(() -> false);
    public static Trigger rightSideReef = new Trigger(() -> false);

    //SmartDashboard.putNumber("estimated x", robotPose.getX());
    //SmartDashboard.putNumber("estimated y", robotPose.getY());


    //Limelight limelight = new Limelight(drivetrain);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.   
        
        drivetrain.setDefaultCommand(   // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() -> drive
                .withVelocityX(-driverController.getLeftY() * MaxSpeed
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive forward with negative Y (forward)        
                .withVelocityY(-driverController.getLeftX() * MaxSpeed
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive left with negative X (left)
                .withRotationalRate(-driverController.getRightX() * MaxAngularRate
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive counterclockwise with negative X (left)
            )
        );

        // ROBOT RELATIVE
        driverController.y().whileTrue(
            drivetrain.applyRequest(() -> driveBot
                .withVelocityX(-driverController.getLeftY() * MaxSpeed
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive forward with negative Y (forward)        
                .withVelocityY(-driverController.getLeftX() * MaxSpeed
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive left with negative X (left)
                .withRotationalRate(driverController.getRightX() * MaxAngularRate
                    * (fastMode * (driverController.getLeftTriggerAxis() + 1)) / (slowMode * (driverController.getRightTriggerAxis() + 1))) // Drive counterclockwise with negative X (left)
            )
        );

        driverController.rightBumper().whileTrue( // when the right bumper is pressed, use the pids in the drivecommands class to provide drive values
            drivetrain.applyRequest(() -> driveBot //drives in robot relative since its not field position based
                .withVelocityX(DriveCommands.forwardPIDoutput())
                .withVelocityY(DriveCommands.rightPIDoutput())
                .withRotationalRate(DriveCommands.rotationPIDoutput())
        ));

        driverController.b().onTrue(Commands.runOnce(() -> drivetrain.resetRotation(new Rotation2d(Rotations.of(0)))));
     
        coralTrigger.onChange(Commands.parallel(
            StaticUtil.rumbleController(driverController, 0.5),
            StaticUtil.rumbleController(operatorController, 0.5)
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        /* OPERATOR CONTROLS ****************************************************************/
        operatorController.povDown().onTrue(ArmCommands.level1(arm));       // D Pad Down -> Level 1
        operatorController.povLeft().onTrue(ArmCommands.level2(arm));       // D Pad Down -> Level 2
        operatorController.povUp().onTrue(ArmCommands.level3(arm));         // D Pad Down -> Level 3
        operatorController.povRight().onTrue(ArmCommands.level4(arm));      // D Pad Down -> Level 4

        operatorController.a().onTrue(ArmCommands.travel(arm));             // A Button -> Travel Position
        operatorController.b().onTrue(ArmCommands.algaeLevel1(arm));        // B Button -> Algae Intake Level 1
        operatorController.x().onTrue(ArmCommands.intakeCoral(arm, coral)); // X Button -> Intake Coral
        operatorController.y().onTrue(ArmCommands.algaeLevel2(arm));        // Y Button -> Algae Intake Level 2

        operatorController.leftTrigger().onTrue(Commands.runOnce(() -> coral.output()));    // Right Trigger -> Drop Coral
        operatorController.rightTrigger().onTrue(ArmCommands.outputAlgae(arm));               // Left Trigger -> Drop Algae

        operatorController.leftBumper().onChange(Commands.runOnce(() -> manualMode = operatorController.leftBumper().getAsBoolean()));  // Left Bumper -> Manual Mode
        operatorController.rightBumper().whileTrue(ArmCommands.intakeAlgae(arm)); // Right Trigger -> Intake Algae

        operatorController.start().whileTrue(Commands.run(() -> coral.runWheel(-operatorController.getLeftY())));

        /* MANUAL MODE LOGIC */
        manualTrigger.whileTrue(Commands.run(() -> {
            if (Math.abs(operatorController.getRightY()) > 0.1)
                arm.driveArmPositional(operatorController.getRightY());

            if (Math.abs(operatorController.getRightX()) > 0.1)
                arm.driveWristPositional(operatorController.getRightX());

            if (Math.abs(operatorController.getLeftX()) > 0.1)
                arm.driveWinchPositional(operatorController.getLeftX());
        }));

        /* END OPERATOR CONTROLS ********************************************************/

        enableTrigger.onTrue(ArmCommands.travel(arm));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return Commands.sequence(
            Commands.deadline(
                Commands.waitSeconds(1.3),
                drivetrain.applyRequest(() -> driveBot
                    .withVelocityX(1)
                )
            ),
            drivetrain.applyRequest(() -> brake),
            Commands.runOnce(() -> drivetrain.resetRotation(new Rotation2d(Degrees.of(0))))
        );
    }
}