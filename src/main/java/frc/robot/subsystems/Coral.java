package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Coral extends SubsystemBase{

    private final Servo flapperServo = new Servo(1); //creates the servo to allow coral in

    public void close() {
        flapperServo.set(0);
    }

    public void half() {
        flapperServo.set(.5);
    }

    public void open() {
        flapperServo.set(1);
    }
}