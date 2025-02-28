package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.MMTalon;

public class Climber extends SubsystemBase {
    Servo swingArmLocker = new Servo(1);
    MMTalon swingArmDriver = new MMTalon(20, "rio");

    public Climber() {

    }
}
