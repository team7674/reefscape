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

/**
 * Motion magic TalonFX
 */
public class MMTalon extends TalonFX {
    // MotionMagic Voltage request
    private final MotionMagicVoltage mm_voltage = new MotionMagicVoltage(0);
    public TalonFXConfiguration cfg;

    public double currentSet; // < Current setpoint
    public double position = super.getPosition().getValueAsDouble(); // < position
    public double currentErr = position - currentSet; // < current error


    /**
     * Constructor
     * @param ID ID
     * @param bus can bus
     */
    public MMTalon(int ID, String bus) {
        super(ID, bus);
        cfg = new TalonFXConfiguration();
        MotionMagicConfigs mmCfg = cfg.MotionMagic;
        Slot0Configs slot0 = cfg.Slot0;
        StatusCode status = StatusCode.StatusCodeNotInitialized;

        slot0.kS = 0;
        slot0.kV = 0;
        slot0.kA = 0;
        slot0.kP = 20;
        slot0.kI = 0;
        slot0.kD = 0;
        

        mmCfg
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(120))
            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(500))
            .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(1500));

        for (byte i = 0; i < 5; i++) {
            status = super.getConfigurator().apply(cfg);
            if (status.isOK()) break;
        }

        if (!status.isOK()) System.out.println("Device could not be configured: " + status);
    }

    /**
     * Sets the motor position using LOCOMOTIVE WIZARDRY!
     * @param pos the position
     */
    public void setPositionMM(double pos) {
        System.out.println(pos);
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
}