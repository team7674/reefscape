package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LimitSwitch;
import frc.robot.util.MMTalon;

public class Climber extends SubsystemBase {
    Servo swingArmLocker = new Servo(1);
    MMTalon swingArmDriver = new MMTalon(20, "rio");
    LimitSwitch stowSwitch = new LimitSwitch(1);

    TalonFXConfiguration notZeroConfig;
    TalonFXConfiguration normalConfig;

    boolean startedOffKilter = false;
    boolean intentToDrive = false;

    public Climber() {
        notZeroConfig = normalConfig = new TalonFXConfiguration();

        MotionMagicConfigs mmcfg = new MotionMagicConfigs();
        mmcfg.MotionMagicCruiseVelocity = 10;
        mmcfg.MotionMagicAcceleration = 100;
        mmcfg.MotionMagicJerk = 1000;
        
        normalConfig.withMotionMagic(mmcfg);
        normalConfig.withSlot0(
            new Slot0Configs()
            .withKP(50)
        );

        normalConfig.withMotorOutput(
            new MotorOutputConfigs()
            .withNeutralMode(NeutralModeValue.Brake)
        );


        notZeroConfig.withCurrentLimits(
            new CurrentLimitsConfigs()
            .withSupplyCurrentLimit(Amps.of(5))
        );


        if (!stowSwitch.get()) {
            swingArmDriver.getConfigurator().apply(normalConfig);
        } else {
            swingArmDriver.getConfigurator().apply(notZeroConfig);
            startedOffKilter = true;
        }
    }

    //@Override
    //public void periodic() {
    //
    //    if (startedOffKilter && !intentToDrive) {
    //        if (!stowSwitch.get()) {
    //            startedOffKilter = false;
    //            swingArmDriver.getConfigurator().apply(normalConfig);
    //            swingArmDriver.set(0);
    //            swingArmDriver.setEncoderToZero();
    //        } else {
    //            swingArmDriver.set(0.1);
    //        }
    //    }
    //
    //    if (stowSwitch.get() && !intentToDrive) {
    //        swingArmDriver.getConfigurator().apply(notZeroConfig);
    //        startedOffKilter = true;
    //    }
    //}

    public void dbg_switchIntent() {
        intentToDrive = !intentToDrive;
    }

    public void lock() {
        swingArmLocker.set(0.025);
    }

    public void unlock() {
        swingArmLocker.set(0.41);
    }

    // Run out the swing arm
    public void swingArmRun(double pos) {
        intentToDrive = true;
        swingArmDriver.setPositionMM(pos);
    }

    public void swingArmBack() {
        intentToDrive = false;
    }
}
