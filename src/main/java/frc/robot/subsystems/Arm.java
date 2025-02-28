package frc.robot.subsystems;

import java.util.TooManyListenersException;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.util.FusedMMTalon;
import frc.robot.util.LimitSwitch;
import frc.robot.util.MMTalon;
import frc.robot.util.StaticUtil;

public class Arm extends SubsystemBase {
    boolean properZeroOnStart = false;
    boolean zeroCorrected = false;

    MMTalon winchMotor = new MMTalon(15, "rio");
    MMTalon tiltMotor = new MMTalon(17, "rio");

    LimitSwitch tiltSwitch = new LimitSwitch(0);
    
    CANcoder wristEncoder = new CANcoder(19, "rio");
    FusedMMTalon wristMotor = new FusedMMTalon(21, "rio", wristEncoder);

    public Arm() {
        winchMotor.setEncoderToZero();

        tiltMotor.setPID(10, 0, 0);

        wristMotor.setMotorToZero();
        wristMotor.setGearing(196.36363636363636);
        wristMotor.correctFusedOffset();
        tiltMotor.setEncoderToZero();

        if(!tiltSwitch.get()) {
            properZeroOnStart = true;
        }
    }

    @Override
    public void periodic() {
        //System.out.println("-=-=-=-=-=-");
        //System.out.println(wristEncoder.getPosition().getValueAsDouble() * 360);
        //System.out.println(wristMotor.getPos());
        //System.out.println(wristMotor.getError());
        //System.out.println("-=-=-=-=-=-");
        System.out.println(properZeroOnStart);

        if (!properZeroOnStart) {
            System.out.println("BAD ZERO");
            if(tiltSwitch.get()) {
                System.out.println("CORRECTING");
                tiltMotor.set(-0.1);
            }

            if(!tiltSwitch.get()) {
                System.out.println("GOOD");
                tiltMotor.set(0);
                tiltMotor.setEncoderToZero();
                properZeroOnStart = true;
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

    public void runTilt(double speed) {
        System.out.println(speed);
        tiltMotor.set(speed);
    }

    public void setTilt(double at) {
        tiltMotor.setPositionMM(at);
    }

    public void tiltExtend() {
        tiltMotor.setPositionMM(0);
    }

    public void ensureTiltZeroed() {
        if(tiltSwitch.get()) {
            tiltMotor.set(-0.1);
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
}