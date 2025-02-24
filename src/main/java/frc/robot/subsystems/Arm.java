package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PIDTalon;

public class Arm extends SubsystemBase {
    private final PIDTalon tiltMotor = new PIDTalon(17, "rio");
    private final PIDTalon elevatorWinch = new PIDTalon(15, "rio");
    private final PIDTalon armDriveMotor = new PIDTalon(16, "rio");
    private final PIDTalon swingArmMotor = new PIDTalon(20, "rio");
    
    private final Servo swingArmLockServo = new Servo(1);

    private final CANcoder armEncoder = new CANcoder(18,"rio");

    @Override
    public void periodic() {
        System.out.println(armEncoder.getAbsolutePosition());
    }

    public void brake() {
        tiltMotor.set(0);
        elevatorWinch.set(0);
        armDriveMotor.set(0);
        swingArmMotor.set(0);
    }

    // -=-=-=-=-=-=-=-= DEBUG FUNCTIONS =-=-=-=-=-=-=-=-
    public void runTiltMotor(double by) {
        tiltMotor.set(by);
    }

    public void runElevatorWinch(double by) {
        elevatorWinch.set(by);
    }

    public void runArmDriveMotor(double by) {
        armDriveMotor.set(by);
    }

    public void runSwingArmMotor(double by) {
        swingArmMotor.set(by);
    }

    public void open() {
        swingArmLockServo.set(1);
    }

    public void half() {
        swingArmLockServo.set(.5);
    }

    public void close() {
        swingArmLockServo.set(0);
    }
}