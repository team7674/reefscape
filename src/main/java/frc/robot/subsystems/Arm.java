package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.FusedMMTalon;
import frc.robot.util.MMTalon;
import frc.robot.util.StaticUtil;

public class Arm extends SubsystemBase {
    MMTalon winchMotor = new MMTalon(15, "rio");
    
    private final double wristEncoderOff = 0;
    CANcoder wristEncoder = new CANcoder(19, "rio");
    FusedMMTalon wristMotor = new FusedMMTalon(21, "rio", wristEncoder);

    public Arm() {
        winchMotor.setEncoderToZero();

        wristMotor.setMotorToZero();
        wristMotor.setEncoderToMotorRat(196.36363636363636);
        wristMotor.correctFusedOffset();
    }

    @Override
    public void periodic() {
        //System.out.println("-=-=-=-=-=-");
        //System.out.println(wristEncoder.getPosition().getValueAsDouble() * 360);
        //System.out.println(wristMotor.getPos());
        //System.out.println(wristMotor.getError());
        //System.out.println("-=-=-=-=-=-");
    }
    
    /**
     * Debug function to run the elevator to any position
     * @param at
     */
    public void setWinch(double at) {
        winchMotor.setPositionMM(at);
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