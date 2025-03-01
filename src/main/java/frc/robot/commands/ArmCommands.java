package frc.robot.commands;

import org.opencv.core.Core;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Coral;

public final class ArmCommands {
    private ArmCommands() {}

    public static Command level1(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.moveArmTo((-27.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((0.00 / 360.00)), arm)
        );
    }

    //public static Command level2(Arm arm) {}

    //public static Command level3(Arm arm) {}

    //public static Command level4(Arm arm) {}

    public static Command intakeCoral(Arm arm, Coral coral) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.moveArmTo((0.00 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((0.00 / 360.00)), arm),
            Commands.runOnce(() -> coral.intake(), coral)
        );
    }
}
