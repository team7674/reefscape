package frc.robot.subsystems.vision;

import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Optional;

//import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

//import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

//import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
//import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
//import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.util.VisionUtil;
//import frc.robot.util.StaticUtil;

public class PhotonVisionClient implements VisionClient {

    private AprilTagFieldLayout layout;
    private List<PhotonTrackedTarget> targets;
    private PhotonTrackedTarget bestTarget = new PhotonTrackedTarget();
    private boolean hasTargets = false;
    private PhotonPipelineResult result = new PhotonPipelineResult();
    private PhotonCamera camera;

    //DEBUG STUFF
    public double estimatedX;
    public double estimatedY;
    public double estimatedYaw;

    public PhotonVisionClient(String key) {
        camera = new PhotonCamera(key);
        layout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeAndyMark);
    }

    @Override
    public void update() {

        result = camera.getLatestResult();

        if(result.hasTargets()) {

            bestTarget = result.getBestTarget();

            estimateBotPose2d();

            SmartDashboard.putNumber("Estimated X", estimatedX);
            SmartDashboard.putNumber("Estimated Y", estimatedY);
            SmartDashboard.putNumber("Estimated Yaw", estimatedYaw);
            SmartDashboard.putNumber("Tag ID", (int) bestTarget.getFiducialId());
            SmartDashboard.updateValues();

        }
    }

    //TODO: TEST FUNCTION
    public Pose2d estimateBotPose2d() { //gets pose2d of the bot based off of vision

        Pose2d robotPose;

        if (bestTarget != null) { //makes sure we have a tag

            //use try/catch in case things are null
            try {

                robotPose = PhotonUtils.estimateFieldToRobotAprilTag(bestTarget.getBestCameraToTarget(),
                    layout.getTagPose(bestTarget.getFiducialId()).get(), VisionUtil.cameraToRobot).toPose2d(); //gets a bunch of things we need to estimate
            
            } catch (NoSuchElementException e) {
                robotPose = null;
                e.printStackTrace();
            }
        
        } else { //TODO: make sure null won't break stuff
            robotPose = null; //if no tag, just leave it null
        }

        try {
            estimatedX = robotPose.getX();
            estimatedY = robotPose.getY();
            estimatedYaw = robotPose.getRotation().getDegrees();
            //System.out.println(robotPose.getX() + ", " + robotPose.getY());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return robotPose; //return it
    }

    public Rotation2d getYawToTag() {
        return null;
    }
}
