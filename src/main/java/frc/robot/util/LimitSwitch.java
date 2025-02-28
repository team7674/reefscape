package frc.robot.util;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch extends DigitalInput {
    boolean invert;

    public LimitSwitch(int channel) {
        super(channel);
    }

    public LimitSwitch(int channel, boolean invert) {
        super(channel);
    }

    public boolean getFiltered() {
        return (invert) ? !super.get() : super.get();
    }
}
