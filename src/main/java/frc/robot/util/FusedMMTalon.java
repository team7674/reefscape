package frc.robot.util;


import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;

/**
 * Motion magic talon with fused encoder
 */
public class FusedMMTalon extends MMTalon {
    private CANcoder encoder; // < Encoder
    public CANcoderConfiguration cc_cfg = new CANcoderConfiguration();

    private double gearing = 1; // < Amount of motor rotations to make 1 encoder rotation 

    // -======== Configurators and constructors ========-
    
    /**
     * CONSTRUCTOR
     * @param myID motor ID
     * @param bus motor bus
     * @param encoder encoder
     */
    public FusedMMTalon(int myID, String bus, CANcoder encoder) {
        super(myID, bus);
        
        this.encoder = encoder;

        this.cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        this.cc_cfg.MagnetSensor.MagnetOffset = 0;
        this.encoder.getConfigurator().apply(cc_cfg);
    }

    /**
     * Set the magnet offset to where your assembly is at it's zero
     * @param off magnet offset
     * @return itself
     */
    public FusedMMTalon withMagnetOffset(double off) {
        this.cc_cfg.MagnetSensor.MagnetOffset = off;
        this.encoder.getConfigurator().apply(this.cc_cfg);
        return this;
    }

    /**
     * Sets the direction of the sensor 
     * (because trust me it is easier than trying to do it with the motor)
     * @param d Direction
     * @return itself
     */
    public FusedMMTalon withDirection(SensorDirectionValue d) {
        this.cc_cfg.MagnetSensor.SensorDirection = d;
        this.encoder.getConfigurator().apply(this.cc_cfg);
        return this;
    }

    /**
     * Sets the mechanism gearing
     * @param gearing the amount of turns the motor must make to do 1 revolution of the mechanism
     * @return itself
     */
    public FusedMMTalon withGearing(double gearing) {
        this.gearing = gearing;
        return this;
    }

    // -================================================-

    // -======== Motor related ========-

    public void setGearing(double gearing) {
        this.gearing = gearing;
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
     * Runs to a specified encoder position
     */
    public void runToPos(double at) {
        System.out.println(at * gearing);
        super.setPositionMM((at) * gearing);
    }

    /**
     * Corrects the motor's encoder offset based on the encoder
     */
    public void correctFusedOffset() {
        super.setPosition(fusedEncodePos() * gearing);
    }

    // -===============================-

    // -======== PID ========-

    /**
     * Gets the current PID error
     * @return the current PID error
     */
    public double pidError() {
        return position() - currentSet;
    }

    // -=====================-

    // -======== Fused encoder ========-

    /**
     * Gets the position of the fused encoder
     * @return the position of the fused encoder
     */
    public double fusedEncodePos() {
        return encoder.getAbsolutePosition().getValueAsDouble();
    }

    public void setMagnetOffset(double off) {
        this.cc_cfg.MagnetSensor.MagnetOffset = off;
        this.encoder.getConfigurator().apply(this.cc_cfg.MagnetSensor);
    }

    public void setDirection(SensorDirectionValue d) {
        this.cc_cfg.MagnetSensor.SensorDirection = d;
        this.encoder.getConfigurator().apply(this.cc_cfg.MagnetSensor);
    }

    // -===============================-
}
