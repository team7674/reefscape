package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
    private final TalonFX liftDriver 
        = new TalonFX(0, "rio");

    private final Slot0Configs liftConf
        = new Slot0Configs()
            .withKP(0)
            .withKI(0)
            .withKD(0);

    @Override
    public void periodic() {
        
    }
}