package frc.robot.commands;

import org.opencv.core.Core;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Coral;

public final class ArmCommands {
    private ArmCommands() {}

    public static Command travel(Arm arm) {
        return Commands.sequence(
                Commands.runOnce(() -> arm.runWristTo(0)),
                Commands.waitSeconds(0.1),
                Commands.runOnce(() -> arm.setWinch(0)),
                Commands.runOnce(() -> arm.moveArmTo(0))
        );
    }

    public static Command level1(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(0)),
            Commands.runOnce(() -> arm.moveArmTo((-27.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((0.00 / 360.00)), arm)
        );
    }

    public static Command level2(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(0)),
            Commands.runOnce(() -> arm.moveArmTo((-47.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((10.00 / 360.00)), arm)
        );
    }

    public static Command level3(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-30)),
            Commands.runOnce(() -> arm.moveArmTo((-47.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((30.00 / 360.00)), arm)
        );
    }

    public static Command level4(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-50)),
            Commands.runOnce(() -> arm.moveArmTo((-90.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((40.00 / 360.00)), arm)
        );
    }

    public static Command algaeLevel1(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.moveArmTo((-40.0 / 360.00)), arm),
            Commands.waitSeconds(0.3),
            Commands.runOnce(() -> arm.runWristTo(60.00 / 360.00) , arm)
        );
    }

    public static Command algaeLevel2(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.moveArmTo(-80.00 / 360.00), arm),
            Commands.waitSeconds(0.3),
            Commands.runOnce(() -> arm.runWristTo((60.00 / 360.00)), arm)
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

    public static Command intakeAlgae(Arm arm) {
        return Commands.runOnce(() -> arm.intakeAlgae());
    }

    public static Command outputAlgae(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> {
                arm.outputAlgae();
                System.out.println("Outputttttingg~");
            }, arm),
            Commands.waitSeconds(1),
            Commands.runOnce(() -> arm.stopAlgaeMotor(), arm)
        );
    }
}
