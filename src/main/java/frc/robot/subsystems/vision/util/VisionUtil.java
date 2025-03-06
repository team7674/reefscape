package frc.robot.subsystems.vision.util;

import java.util.List;


import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;


public final class VisionUtil {

    public static final double kCameraHeight = 0.85935;
    public static final double kCameraPitch = 0;
    public static final Transform3d cameraToRobot = new Transform3d(new Translation3d(0.0, 0.2286, 0.89535), new Rotation3d(0,0,0));

    private VisionUtil() {}

    protected static final double kAvgCrazyLow = 0.00;
    protected static final double kAvgCrazyHigh = 28.00;

    public static Pose2d averagePoses2D(List<Pose2d> poses) {
        if ( poses.isEmpty() )
            return new Pose2d();

        double sumX = 0.00;
        double sumY = 0.00;
        double sumT = 0.00;
        int badPoses = 0;

        for ( Pose2d pose : poses ) {
            if ( pose.getX() > kAvgCrazyHigh || pose.getX() < kAvgCrazyLow || pose.getY() > kAvgCrazyHigh || pose.getY() < kAvgCrazyLow ) {
                badPoses++;
                continue;
            }

            sumX += pose.getX();
            sumY += pose.getY();
            sumT += pose.getRotation().getRadians();
        }

        double avgX = (sumX / (poses.size() - badPoses));
        double avgY = (sumY / (poses.size() - badPoses));
        double avgT = (sumT / (poses.size() - badPoses));

        System.out.println(avgX);
        System.out.println(avgY);
        System.out.println(avgT);
        System.out.println(badPoses);
        return new Pose2d(avgX, avgY, new Rotation2d(avgT));
    }
}
