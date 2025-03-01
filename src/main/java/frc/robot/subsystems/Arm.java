package frc.robot.subsystems;

import java.util.TooManyListenersException;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.util.FusedMMTalon;
import frc.robot.util.LimitSwitch;
import frc.robot.util.MMTalon;
import frc.robot.util.StaticUtil;

public class Arm extends SubsystemBase {
    boolean properTiltZeroOnStart = false;

    boolean ready = false;

    MMTalon winchMotor = new MMTalon(15, "rio");
    MMTalon tiltMotor = new MMTalon(17, "rio");

    LimitSwitch tiltSwitch = new LimitSwitch(0);
    
    CANcoder wristEncoder = new CANcoder(19, "rio");
    FusedMMTalon wristMotor = new FusedMMTalon(21, "rio", wristEncoder);

    CANcoder armEncoder = new CANcoder(18, "rio");
    FusedMMTalon armMotor = new FusedMMTalon(16, "rio", armEncoder);

    public Arm() {
        winchMotor.setEncoderToZero();
        winchMotor.setNeutralMode(NeutralModeValue.Brake);
        winchMotor.setPID(30, 0, 0);

        tiltMotor.setPID(10, 0, 0);
        tiltMotor.setNeutralMode(NeutralModeValue.Brake);

        armMotor.setMotorToZero();
        armMotor.setPID(10, 0, 0);
        armMotor.setGearing(110.76923077);
        armMotor.setDirection(SensorDirectionValue.Clockwise_Positive);
        armMotor.setMagnetOffset(0.12036160938);
        armMotor.setNeutralMode(NeutralModeValue.Brake);

        wristMotor.setMotorToZero();
        wristMotor.setGearing(196.36363636363636);
        wristMotor.setPID(10, 0, 0);
        wristMotor.setMagnetOffset(-0.14233399984);
        wristMotor.setDirection(SensorDirectionValue.Clockwise_Positive);
        wristMotor.correctFusedOffset();
        wristMotor.setNeutralMode(NeutralModeValue.Brake);

        tiltMotor.setEncoderToZero();

        if(!tiltSwitch.get()) {
            properTiltZeroOnStart = true;
        }
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Wrist angle", readWristPosition() * 360.00);

        if (!properTiltZeroOnStart) {
            if(tiltSwitch.get()) {
                tiltMotor.set(-0.1);
            }

            if(!tiltSwitch.get()) {
                tiltMotor.set(0);
                tiltMotor.setEncoderToZero();
                properTiltZeroOnStart = true;
            }
        }

        if (!ready) {
            if (properTiltZeroOnStart) {
                ready = true;
            }
        }
    }
    
    /**
     * Debug function to run the elevator to any position
     * @param at
     */
    public void setWinch(double at) {
        winchMotor.setPositionMM(at);
    }

    public double getWinch() {
        return winchMotor.getPos();
    }

    public void runTilt(double speed) {
        System.out.println(speed);
        tiltMotor.set(speed);
    }

    public void runWinch(double speed) {
        winchMotor.set(speed);
    }

    public void setTilt(double at) {
        tiltMotor.setPositionMM(at);
    }

    public void tiltExtend() {
        tiltMotor.setPositionMM(0);
    }

    public void ensureTiltZeroed() {
        if(tiltSwitch.get()) {
            tiltMotor.set(-0.2);
        } else {
            tiltMotor.set(0);
            tiltMotor.setEncoderToZero();
        }
    }
    
    /**
     * Debug function to run the wrist to any position
     * @param at
     */
    public void setWrist(double at) {
        wristMotor.setPositionMM(at);
    }

    public void runWristTo(double at) {
        System.out.println("arm.runWristTo(" + at + ")");
        wristMotor.runToPos(StaticUtil.clamp(at , (40 / 360.00), (-20 / 360.00)));
    }

    public void driveWrist(double speed) {
        wristMotor.set(speed);
    }

    public void driveArm(double speed) {
        System.out.println("-=-=-=-=-=-");
        System.out.println(armEncoder.getPosition().getValueAsDouble() * 360);
        System.out.println(armMotor.getPos());
        System.out.println(armMotor.getError());
        System.out.println("-=-=-=-=-=-");
        armMotor.set(speed);
    }

    public void zeroWrist() {
        var pos = wristEncoder.getPosition().getValueAsDouble();
        var target = 0;
        var finTgt = pos * 196.36363636363636;
        System.out.println(finTgt);
        setWrist(finTgt);
    }

    /**
     * @return current position of the wrist
     */
    public double readWristPosition() {
        return wristMotor.fusedEncodePos();
    }

    public void moveArmTo(double to) {
        armMotor.runToPos(to);
    }
}