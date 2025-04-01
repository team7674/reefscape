package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LimitSwitch;

// TODO: Return to travel after we collect coral

public class Coral extends SubsystemBase {
    public boolean containsCoral = false;
    public boolean outputting = false;

    private final Servo flapperServo = new Servo(0); //creates the servo to allow coral in
    private final TalonSRX wheel = new TalonSRX(14);

    private final LimitSwitch coralSensor = new LimitSwitch(5, true);

    public Coral() {
        wheel.setNeutralMode(NeutralMode.Brake);
        closeDoor();
    }

    @Override 
    public void periodic() {
        if(!coralSensor.getFiltered() && !outputting) {
            runWheel(0);
            closeDoor();
            containsCoral = true;
        }

        if (!coralSensor.getFiltered() && outputting) {
            runWheel(1);
            closeDoor();
        }

        if (coralSensor.getFiltered() && outputting) {
            runWheel(0);
            outputting = false;
            containsCoral = false;
        }
    }

    // -======== The wheel ========-

    public void runWheel(double at) {
        wheel.set(TalonSRXControlMode.PercentOutput, at);
    }

    public void intake() {
        if (coralSensor.getFiltered()) {
            openDoor();
            runWheel(1);
            containsCoral = false;
        }
    }

    public void output() {
            outputting = true;
    }

    // -===========================-

    public void closeDoor() {
        flapperServo.set(1);
    }

    public void openDoor() {
        flapperServo.set(.4);
    }
}