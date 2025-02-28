package frc.robot.util;

import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

/**
 * Motion magic TalonFX
 */
public class MMTalon extends TalonFX {
    // MotionMagic Voltage request
    private final MotionMagicVoltage mm_voltage = new MotionMagicVoltage(0);
    public TalonFXConfiguration cfg = new TalonFXConfiguration();

    public double currentSet; // < Current setpoint
    public double position = super.getPosition().getValueAsDouble(); // < position
    public double currentErr = position - currentSet; // < current error

    // -======== Configurators and constructors ========-

    /**
     * Constructor
     * @param ID ID
     * @param bus can bus
     */
    public MMTalon(int ID, String bus) {
        super(ID, bus);
        StatusCode status = StatusCode.StatusCodeNotInitialized;

        cfg.Slot0.kS = 0;
        cfg.Slot0.kV = 0;
        cfg.Slot0.kA = 0;
        cfg.Slot0.kP = 0;
        cfg.Slot0.kI = 0;
        cfg.Slot0.kD = 0;
        

        cfg.MotionMagic
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(120))
            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(500))
            .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(1500));

        for (byte i = 0; i < 5; i++) {
            status = super.getConfigurator().apply(cfg);
            if (status.isOK()) break;
        }

        if (!status.isOK()) System.err.println("Device could not be configured: " + status);
    }

    public MMTalon withPID(double kP, double kI, double kD) {
        cfg.Slot0.kP = kP;
        cfg.Slot0.kI = kI;
        cfg.Slot0.kD = kD;
        super.getConfigurator().apply(cfg.Slot0); // Only apply at the depth with which you are configuring!
        return this;
    }

    // -================================================-

    // -======== Motor ========-

    /**
     * Sets the motor position using LOCOMOTIVE WIZARDRY!
     * @param pos the position
     */
    public void setPositionMM(double pos) {
        currentSet = pos;
        super.setControl(mm_voltage.withPosition(pos).withSlot(0));
    }

    /**
     * Zeroes the encoder
     */
    public void setEncoderToZero() {
        super.setPosition(0);
    }

    /**
     * Gets the current position
     * @return the current position
     */
    public double getPos() {
        position = super.getPosition().getValueAsDouble();
        return position;
    }

    /**
     * Gets (and refreshes) the PID error value
     * @return current error
     */
    public double getError() {
        currentErr = getPos() - currentSet; // Ensures member values stay current
        return currentErr;
    }

    /**
     * Refresh all members
     */
    @SuppressWarnings("unused")
    private void refresh() {
        getPos();
        getError();
    }
    // -=======================-

    // -======== PID ========-

    /**
     * Sets the PID controller's *basic* gains
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derrivative gain
     */
    public void setPID(double kP, double kI, double kD) {
        cfg.Slot0.kP = kP;
        cfg.Slot0.kI = kI;
        cfg.Slot0.kD = kD;
        super.getConfigurator().apply(cfg.Slot0);
    }

    /**
     * Sets all PID controller gains
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derrivative gain
     * @param kV Velocity feed forward gain
     * @param kS Static feed forward gain
     * @param kG Gravity feed forward gain
     * @param kA Acceleration gain
     * @param grav Gravity type (linear or rotational)
     */
    public void setPIDPedantic(
        double kP,
        double kI,
        double kD,
        double kV,
        double kS,
        double kG,
        double kA,
        GravityTypeValue grav
    ) {
        cfg.Slot0.kP = kP;
        cfg.Slot0.kI = kI;
        cfg.Slot0.kD = kD;
        cfg.Slot0.kV = kV;
        cfg.Slot0.kS = kS;
        cfg.Slot0.kG = kG;
        cfg.Slot0.kA = kA;
        cfg.Slot0.GravityType = grav;
        super.getConfigurator().apply(cfg.Slot0);
    }

    // -=====================-
}