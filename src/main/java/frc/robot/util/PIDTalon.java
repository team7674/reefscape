package frc.robot.util;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.compound.Diff_MotionMagicTorqueCurrentFOC_Position;
import com.ctre.phoenix6.hardware.TalonFX;

public class PIDTalon extends TalonFX {
    public PIDTalon(int ID, String canbus) {
        super(ID, canbus);
        m_configs = new Slot0Configs();
    }

    public PIDTalon(int ID, CANBus canbus) {
        super(ID, canbus);
        m_configs = new Slot0Configs();
    }

    public PIDTalon(int ID) {
        super(ID, "rio");
        m_configs = new Slot0Configs();
    }

    public PIDTalon withKP(double kP) {
        m_configs.kP = kP;
        return this;
    }

    public PIDTalon withKI(double kI) {
        m_configs.kI = kI;
        return this;
    }

    public PIDTalon withKD(double kD) {
        m_configs.kD = kD;
        return this;
    }

    public void configure() {
        super.getConfigurator().apply(m_configs);
    }

    public void run(double set) {
    }

    private Slot0Configs m_configs;
}
