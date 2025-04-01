package frc.robot.commands;

import org.opencv.core.Core;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
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
            Commands.runOnce(() -> arm.moveArmTo((-57.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((0.00 / 360.00)), arm)
        );
    }

    public static Command level2(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-10)),
            Commands.runOnce(() -> arm.moveArmTo((-63.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((6.00 / 360.00)), arm)
        );
    }

    public static Command level3(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-85)),
            Commands.runOnce(() -> arm.moveArmTo((-62.0 / 360.00)), arm),
            Commands.runOnce(() -> arm.runWristTo((0.00 / 360.00)), arm)
        );
    }

    public static Command level4(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-135)),
            Commands.runOnce(() -> arm.moveArmTo((-144.0 / 360.00)), arm), // was -125
            Commands.runOnce(() -> arm.runWristTo((94.00 / 360.00)), arm) // was 80
        );
    }

    public static Command algaeLevel1(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.moveArmTo((-83.0 / 360.00)), arm),
            Commands.waitSeconds(0.3),
            Commands.runOnce(() -> arm.runWristTo(80.00 / 360.00) , arm)
        );
    }

    public static Command algaeLevel2(Arm arm) {
        return Commands.sequence(
            Commands.runOnce(() -> arm.setWinch(-97)),
            Commands.runOnce(() -> arm.moveArmTo(-75.00 / 360.00), arm),
            Commands.waitSeconds(0.3),
            Commands.runOnce(() -> arm.runWristTo((80.00 / 360.00)), arm)
        );
    }

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
                //System.out.println("Outputttttingg~");
            }, arm),
            Commands.waitSeconds(1),
            Commands.runOnce(() -> arm.stopAlgaeMotor(), arm)
        );
    }

    public static Command climberOut(Arm arm, Climber climber) {
        if (arm.isLiftAtZero()) { 
            return Commands.none(); 
        } else {
        return Commands.print("Motor running lmao *" + arm.isLiftAtZero());    
        //return Commands.runOnce(() -> climber.swingArmRun(-45), climber);
        }
    }

    public static Command climberIn(Climber climber) {
        return Commands.runOnce(() -> climber.swingArmBack(), climber);
    }
}