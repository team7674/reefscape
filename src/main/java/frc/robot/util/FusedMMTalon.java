package frc.robot.util;

import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

/**
 * Motion magic talon with fused encoder
 */
public class FusedMMTalon extends MMTalon {
    private CANcoder encoder; // < Encoder

    private double encoderToMotor = 0; // < Amount of motor rotations to make 1 encoder rotation 

    public void setEncoderToMotorRat(double ratio) {
        encoderToMotor = ratio;
    }

    /**
     * CONSTRUCTOR
     * @param myID motor ID
     * @param bus motor bus
     * @param encoder encoder
     */
    public FusedMMTalon(int myID, String bus, CANcoder encoder) {
        super(myID, bus);
        
        this.encoder = encoder;

        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        cc_cfg.MagnetSensor.MagnetOffset = -0.143799;
        this.encoder.getConfigurator().apply(cc_cfg);
    }

    /**
     * sets the motor to zero
     */
    public void setMotorToZero() {
        super.setEncoderToZero();
    }

    /**
     * Gets the current position
     * @return the current motor position
     */
    public double position() {
        return super.getPosition().getValueAsDouble();
    }

    /**
     * Gets the current PID error
     * @return the current PID error
     */
    public double pidError() {
        return position() - currentSet;
    }

    /**
     * Gets the position of the fused encoder
     * @return the position of the fused encoder
     */
    public double fusedEncodePos() {
        return encoder.getPosition().getValueAsDouble();
    }

    /**
     * Runs to a specified encoder position
     */
    public void runToPos(double at) {
        System.out.println("FusedMMTalon.runToPos(" + at + ")");
        System.out.println(at);
        System.out.println(at * encoderToMotor);
        super.setPositionMM((at) * encoderToMotor);
    }

    public void correctFusedOffset() {
        super.setPosition(fusedEncodePos() * encoderToMotor);
    }
}
