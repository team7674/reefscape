package frc.robot.subsystems.vision;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.util.VisionUtil;
//import frc.robot.util.StaticUtil;

public class PhotonVisionClient implements VisionClient {
    public PhotonVisionClient(String key) {
        camera = new PhotonCamera(key);
    }

    @Override
    public void update() {
        result = camera.getLatestResult();

        if(result.hasTargets()) {
            bestTarget = result.getBestTarget();
        }
    }

    /*
    
    public Pose2d estimateBotPose2d(CommandSwerveDrivetrain drive) { //create pose2d of estimated bot position based off of tag

        try { //could be empty, must try/catch

            Optional<Pose3d> tagPoseOp = StaticUtil.getTagFieldLayoutAM().getTagPose(bestTarget.getFiducialId()); //grabs the tag pose

            Pose3d tagPose = (tagPoseOp.isPresent())?tagPoseOp.get():null; //if the tagpose is present, get it - if not, get null

            System.out.println(tagPose.toString() + ", " + VisionUtil.cameraToRobot.toString());

            Pose2d robotPose2d = PhotonUtils.estimateFieldToRobot( //create a pose2d of where we think we are based on any tag that is visible
                VisionUtil.kCameraHeight, tagPose.getZ(), VisionUtil.kCameraPitch, tagPose.getRotation().getY(), 
                tagPose.toPose2d().getRotation(), drive.getPose2d().getRotation(), tagPose.toPose2d(), VisionUtil.cameraToRobot
            );

            return robotPose2d;

        } catch (NullPointerException e) { //handles if the pose3d is null
            System.out.println(e);
            return null;
        }
    }

    */
        

    public Rotation2d getYawToTag() {
        return null;
    }

    private List<PhotonTrackedTarget> targets;
    private PhotonTrackedTarget bestTarget = new PhotonTrackedTarget();
    private boolean hasTargets = false;
    private PhotonPipelineResult result = new PhotonPipelineResult();

    private PhotonCamera camera;

    private Transform3d cameraToRobot;
}
