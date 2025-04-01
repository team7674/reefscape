package frc.robot.subsystems.appendage;

//import static edu.wpi.first.units.Units.*;
//
//import com.ctre.phoenix6.StatusCode;
//import com.ctre.phoenix6.configs.MotionMagicConfigs;
//import com.ctre.phoenix6.configs.Slot0Configs;
//import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.controls.MotionMagicVoltage;
//import com.ctre.phoenix6.hardware.TalonFX;
//
///**
// * @deprecated please use MMTalon instead
// */
//public class ElevatorWinch {
//
//    private final TalonFX elevatorWinch = new TalonFX(15, "rio");
//
//    private final MotionMagicVoltage elevatorWinchMM = new MotionMagicVoltage(0);
//    
//    public double pidTarget;
//    public double position = elevatorWinch.getPosition().getValueAsDouble();
//    public double pidError = position - pidTarget;
//    
//
//    public void config() {
//        TalonFXConfiguration elevatorWinchCfg = new TalonFXConfiguration();
//        //FeedbackConfigs elevatorWinchFdb = elevatorWinchCfg.Feedback;             //not used
//        MotionMagicConfigs elevatorWinchMM =  elevatorWinchCfg.MotionMagic;
//        Slot0Configs slot0 = elevatorWinchCfg.Slot0;
//        StatusCode status = StatusCode.StatusCodeNotInitialized;
//
//        slot0.kS = 0; // Add 0.25 V output to overcome static friction (.25)
//        slot0.kV = 0; // A velocity target of 1 rps results in 0.12 V output (.12)
//        slot0.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output (.2)
//        slot0.kP = 60; // P VALUE
//        slot0.kI = 0; // I VALUE
//        slot0.kD = 0; // D VALUE
//
//        elevatorWinchMM
//            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(100))
//            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(50))
//            .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(0));
//
//
//        // I'm not really sure what this does, but it's in the example and seems mildly important
//        // Tries applying configs 5 times. logs the error upon failure
//        for (int i = 0; i < 5; i++) {
//            status = elevatorWinch.getConfigurator().apply(elevatorWinchCfg);
//            if (status.isOK()) break;
//        }
//
//        if (!status.isOK()) {
//            System.out.println("Could not configure device. Error: " + status.toString());
//        }
//    }
//
//    public void setPosition(double pos) {
//        pidTarget = pos;
//        elevatorWinch.setControl(elevatorWinchMM.withPosition(pos).withSlot(0));
//    }
//
//    public void setEncoderToZero() {
//        elevatorWinch.setPosition(0);
//    }
//}