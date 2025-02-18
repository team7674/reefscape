package frc.robot.util;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.io.IOException;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;

public class StaticUtil {
    public static double getCurrentRioTimestamp() {
        return Timer.getTimestamp();
    }

    public static AprilTagFieldLayout getTagFieldLayoutAM() {
        return AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);
    }

    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);
}
