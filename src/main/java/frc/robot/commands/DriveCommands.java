package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Limelight;
import frc.robot.util.MovingAverage;

public final class DriveCommands {

    static double driveXP = 2;
    static double driveYP = 2;
    static double rotateP = 2;

    static double driveXI = 0;
    static double driveYI = 0;
    static double rotateI = 0;

    static double driveXD = 0;
    static double driveYD = 0;
    static double rotateD = 0;

    static final double period = 0.2;

    static double forwardTarget = 1;
    static double rightTarget = 0;

    private DriveCommands() {} //makes class uninstantiable

    public static PIDController rotationPID = new PIDController(rotateP, rotateI, rotateD, period);
    public static PIDController forwardPID = new PIDController(driveXP, driveXI, driveXD, period);
    public static PIDController rightPID = new PIDController(driveYP, driveYI, driveYD, period);

    static Limelight limelight = new Limelight(RobotContainer.drivetrain);
    
    static MovingAverage tagYawSmoother = new MovingAverage(50);
    static MovingAverage xSmoother = new MovingAverage(20);
    static MovingAverage ySmoother = new MovingAverage(20);
    
    /* gets some important variables that we need */
    
    public static double forwardError() {
        return forwardTarget - Math.abs(limelight.getBotPos_TagSpace().getZ());
    }

    public static double rightError() {
        return rightTarget - limelight.getBotPos_TagSpace().getX();
    }

    public static double rotationError() { // we could have the option for a target but it will always be 0 degrees from the tag with how we use it so we will just condense 0 - botposetagspace
        return -limelight.getBotPos_TagSpace().getRotation().getZ() * 2 * Math.PI;
    }

    // Updates PIDs after errors are set up
    public static void updatePIDs() {
        rotationPID.calculate(rotationError());
        forwardPID.calculate(forwardError());
        rightPID.calculate(rightError());
    }

    // gets individual pid outputs for easier usage
    public static double forwardPIDoutput() {
        System.out.println(forwardPID.calculate(forwardError()));
        return forwardPID.calculate(forwardError());
    }

    public static double rightPIDoutput() {
        System.out.println(rightPID.calculate(rightError()));
        return rightPID.calculate(rightError());
    }

    public static double rotationPIDoutput() {
        System.out.println(rotationPID.calculate(rotationError()));
        return rotationPID.calculate(rotationError());
    }

    public static void printNumbers() {

        updatePIDs();

        SmartDashboard.putNumber("forward robot position: ", limelight.getBotPos_TagSpace().getZ());
        SmartDashboard.putNumber("right robot position: ", limelight.getBotPos_TagSpace().getX());
        SmartDashboard.putNumber("rotate robot position", limelight.getBotPos_TagSpace().getRotation().getZ() * 2 * Math.PI);

        SmartDashboard.putNumber("forward error: ", forwardError());
        SmartDashboard.putNumber("right error: ", rightError());
        SmartDashboard.putNumber("rotation error: ", rotationError());

        SmartDashboard.putNumber("forward PID output: ", forwardPIDoutput());
        SmartDashboard.putNumber("right PID output: ", rightPIDoutput());
        SmartDashboard.putNumber("rotate PID output: ", rotationPIDoutput());

        SmartDashboard.updateValues();
    }

    /*

    public static void resetPIDs() {
        rotationPID.reset();
        rotationPID.setPID(rotateP, rotateI, rotateD);
        xPID.reset();
        yPID.reset();
        tagYawSmoother.reset();
        xSmoother.reset();
        ySmoother.reset();
    }
    */
}