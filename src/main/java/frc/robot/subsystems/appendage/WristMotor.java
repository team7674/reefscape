package frc.robot.subsystems.appendage;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

/**
 * @deprecated Please use FusedMMTalon instead
 */
public class WristMotor {

    //creates our objects
    private final TalonFX wristMotor = new TalonFX(21, "rio");
    private final CANcoder wristEncoder = new CANcoder(19, "rio");

    //creates the PID/MM
    private final MotionMagicVoltage wristMotorMM = new MotionMagicVoltage(0);
    
    public static double pidTarget;

    /* CONFIGMOTOR FUNCTION - configures the motor, pretty self descriptive */

    public void configMotor() {

        TalonFXConfiguration wristMotorCfg = new TalonFXConfiguration();        //new config
        //FeedbackConfigs wristMotorFdb = wristMotorCfg.Feedback;               //not used
        MotionMagicConfigs wristMotorMM =  wristMotorCfg.MotionMagic;           //new MM config
        Slot0Configs slot0 = wristMotorCfg.Slot0;                               //new PID config/new slot 0 (same same)
        StatusCode status = StatusCode.StatusCodeNotInitialized;                //code status, used to make sure we apply the config

        /* THESE VALUES ARE TUNED, DO NOT CHANGE */

        // SETS PIDS
        slot0.kP = 60;      // P VALUE
        slot0.kI = 0;       // I VALUE
        slot0.kD = 0;       // D VALUE

        // SETS MM VALUES
        wristMotorMM 
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(200))
            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(100))
            .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(0))
            .withMotionMagicExpo_kV(0.05)                                   //LOWER = FASTER
            .withMotionMagicExpo_kA(0.05);


        // Tries to apply the configs multiple times in case something goes wrong the first, or second, or third time (but not the fourth)
        for (int i = 0; i < 5; i++) {
            status = wristMotor.getConfigurator().apply(wristMotorCfg);
            if (status.isOK()) break;
        }

        //prints out error thing if config dont work
        if (!status.isOK()) {
            System.out.println("Could not configure device. Error: " + status.toString());
        }
    }

    /* END CONFIGMOTOR FUNCTION */

    public void setPosition(double pos) { //sets positions
        pidTarget = pos; //updates value in the class for other functions/utilities to use
        wristMotor.setControl(wristMotorMM.withPosition(pos).withSlot(0)); //does all the magic
    }

    public void setMotorToZero() { //counterintuitively doesnt move the motor, just tells the motor this is the new zero position
        wristMotor.setPosition(0);
    }

    //sets up some useful variables
    public double position() {
        return wristMotor.getPosition().getValueAsDouble();
    } 

    public double pidError() {
        return position() - pidTarget;
    }

    /* CANCoder stuff here */

    public void configEncoder() {
        var encoderConfig = new CANcoderConfiguration(); //new encoder config
        wristEncoder.getConfigurator().apply(encoderConfig); //applies it

        //tells the encoder to send out wristEncoder.getPosition() and wristEncoder.getVelocity() at a frequency of 100Hz
        BaseStatusSignal.setUpdateFrequencyForAll(100, wristEncoder.getPosition(), wristEncoder.getVelocity());
    }

    //grabs encoders position
    public double wristEncoderPos() {
        return wristEncoder.getPosition().getValueAsDouble();
    }
}