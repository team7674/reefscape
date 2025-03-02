package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import java.util.TooManyListenersException;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.util.FusedMMTalon;
import frc.robot.util.LimitSwitch;
import frc.robot.util.MMTalon;
import frc.robot.util.StaticUtil;

public class Arm extends SubsystemBase {
    boolean properTiltZeroOnStart = false;
    boolean hasAlgae = false;
    boolean intaking = false;
    boolean speedyAlgae = false;
    boolean hasSpeedyAlgaed = false;

    boolean ready = false;

    MMTalon winchMotor = new MMTalon(15, "rio");
    MMTalon tiltMotor = new MMTalon(17, "rio");

    LimitSwitch tiltSwitch = new LimitSwitch(0);
    
    CANcoder wristEncoder = new CANcoder(19, "rio");
    FusedMMTalon wristMotor = new FusedMMTalon(21, "rio", wristEncoder);

    CANcoder armEncoder = new CANcoder(18, "rio");
    FusedMMTalon armMotor = new FusedMMTalon(16, "rio", armEncoder);

    TalonFX algaeIn = new TalonFX(22, "rio");
    private TalonFXConfiguration algaeConfigIn = new TalonFXConfiguration();
    private TalonFXConfiguration algaeConfigOut = new TalonFXConfiguration();
    
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
    
            algaeConfigIn.CurrentLimits.StatorCurrentLimit = 13;
            algaeConfigIn.CurrentLimits.StatorCurrentLimitEnable = true;
                       
            algaeConfigOut.CurrentLimits.StatorCurrentLimit = 80;
            algaeConfigOut.CurrentLimits.StatorCurrentLimitEnable = true;
    
            algaeIn.getConfigurator().apply(algaeConfigIn);
            
            if(!tiltSwitch.get()) {
                properTiltZeroOnStart = true;
            }
        }
    
        @Override
        public void periodic() {
            SmartDashboard.putNumber("Arm angle", armEncoder.getAbsolutePosition().getValueAsDouble() * 360.00);
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
    
            if (intaking && !hasAlgae) {
                algaeIn.set(1);
            }
    
            if (intaking && algaeIn.getRotorVelocity().getValueAsDouble() > 20.0) {
                speedyAlgae = true;
                hasSpeedyAlgaed = true;
            }
    
            if (hasSpeedyAlgaed && intaking) {
                if (algaeIn.getRotorVelocity().getValueAsDouble() < 13.00) {
                    algaeIn.set(0);
                    hasAlgae = true;
                    intaking = false;
                    speedyAlgae = false;
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

        public void driveArmPositional(double speed) {
            armMotor.runToPos(StaticUtil.clamp(
                armMotor.fusedEncodePos() + (speed * 0.03),
                (0.00 / 360.00), (-100.00 / 360.00))
            );
        }

        public void driveWinchPositional(double speed) {
            winchMotor.setPositionMM(StaticUtil.clamp(
                winchMotor.getPos() + (speed * 20),
                0.00, -135.00
            ));
        }

        public void driveWristPositional(double speed) {
            wristMotor.runToPos(StaticUtil.clamp(
                wristMotor.fusedEncodePos() + (speed * 0.03),
                (180.00 / 360), (-35.00 / 360.00))
            );
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
    
    public void intakeAlgae() {
        algaeIn.getConfigurator().apply(algaeConfigIn);
        intaking = true;
        speedyAlgae = false;
        hasSpeedyAlgaed = false;
    }

    public void outputAlgae() {
        intaking = false;
        hasAlgae = false;
        speedyAlgae = false;
        hasSpeedyAlgaed = false;
        algaeIn.getConfigurator().apply(algaeConfigOut);
        algaeIn.set(-1);
    }

    public void stopAlgaeMotor() {
        algaeIn.set(0);
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
        wristMotor.runToPos(StaticUtil.clamp(at , (100 / 360.00), (-20 / 360.00)));
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