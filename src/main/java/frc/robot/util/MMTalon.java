package frc.robot.util;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

public class MMTalon extends TalonFX {

    private final MotionMagicExpoVoltage request = new MotionMagicExpoVoltage(0); //creates a motion magic request
    private TalonFXConfiguration m_conf = new TalonFXConfiguration(); //creates new talonfx config

    public MMTalon(int ID) {
        super(ID);
    }

    public MMTalon(int ID, CANBus bus) {
        super(ID, bus);
    }

    public MMTalon(int ID, String bus) {
        super(ID, bus);
    }

    /**
     * Sets the motor PID
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derrivative gain
     */

    public void setPID(double kP, double kI, double kD) {
        m_conf.Slot0.kP = kP;
        m_conf.Slot0.kI = kI;
        m_conf.Slot0.kD = kD;
        configure();
    }

    /**
     * Sets the ENTIRE PID with pedantic values
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derrivative gain
     * @param kG Gravity FF
     * @param kS Static FF
     * @param kV Velocity FF
     * @param kA Acceleration FF
     */

    public void setPIDPedantics(
        double kP,
        double kI,
        double kD,
        double kG,
        double kS,
        double kV,
        double kA 
    ) {
        m_conf.Slot0.kP = kP;
        m_conf.Slot0.kI = kI;
        m_conf.Slot0.kD = kD;
        m_conf.Slot0.kG = kG;
        m_conf.Slot0.kS = kS;
        m_conf.Slot0.kV = kV;
        m_conf.Slot0.kA = kA;
        configure();
    }

    /**
     * Sets the motion magic profile
     * @param accel acceleration
     * @param jerk velocity to target
     * @param cruise max velocity at any given time
     */

    public void setMotionMagicProfile(double accel, double cruise, double jerk) {
        m_conf.MotionMagic.MotionMagicAcceleration = accel;
        m_conf.MotionMagic.MotionMagicCruiseVelocity = cruise;
        m_conf.MotionMagic.MotionMagicJerk = jerk; // hehe jerk lmao
        configure();
    }

    /**
     * Set the motion magic pedantics (things we dont fully need)
     * @param accel
     * @param cruise
     * @param jerk
     * @param kV
     * @param kA
     */

    public void setMotionMagicProfilePedantics(
        double accel,
        double cruise,
        double jerk,
        double kV,
        double kA
    ) {
        m_conf.MotionMagic.MotionMagicAcceleration = accel;
        m_conf.MotionMagic.MotionMagicCruiseVelocity = cruise;
        m_conf.MotionMagic.MotionMagicJerk = jerk; // hehe jerk lmao
        m_conf.MotionMagic.MotionMagicExpo_kV = kV;
        m_conf.MotionMagic.MotionMagicExpo_kA = kA;
        configure();
    }

    /**
     * Runs the motor at a position
     * @param pos position in rotations
     */

    public void run(double pos) {
        super.setControl(request.withPosition(pos));
        super.setPosition(pos);
    }

    //function to configure the motors
    public void configure() {
        // DEBUG

        m_conf.Voltage.withPeakForwardVoltage(Volts.of(8)).withPeakReverseVoltage(Volts.of(-8));
        m_conf.TorqueCurrent.withPeakForwardTorqueCurrent(Amps.of(120)).withPeakReverseTorqueCurrent(Amps.of(-120));

        // -=-=-
        System.out.println(super.getConfigurator().apply(m_conf));
    }
}
