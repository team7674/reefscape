package frc.robot.util;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class StaticUtil {
    public static double getCurrentRioTimestamp() {
        return Timer.getTimestamp();
    }

    public static AprilTagFieldLayout layout;

    public static void staticInit() {
        layout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeAndyMark);
    }

    public static AprilTagFieldLayout getTagFieldLayoutAM() {
        return layout;
    }

    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    public static double RotationsFromDegrees(double deg) {
        return (deg / 360.00);
    }

    public static double clamp(double x, double high, double low) {
        var out = (x > high || x < low) ? (x > high) ? high : low : x;
        System.out.println("Clamped: " + out);
        return out;
    }

    public static Command rumbleController(CommandXboxController joystick, double howLong) {
        return Commands.sequence(
            Commands.runOnce(() -> joystick.setRumble(RumbleType.kBothRumble, 1)),
            Commands.waitSeconds(howLong),
            Commands.runOnce(() -> joystick.setRumble(RumbleType.kBothRumble, 0))
        );
    }

    public static Command pulseController(CommandXboxController ctrl, double width, int length) {
        boolean done = false;
        int times = 0;
        return null;
    }
}