package frc.robot.subsystems.vision;

import java.util.List;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.util.StaticUtil;

public class PhotonVisionClient implements VisionClient {
    public PhotonVisionClient(String key) {
        camera = new PhotonCamera(key);
    }

    @Override
    public void update() {
        result = camera.getLatestResult();

        if(result.hasTargets()) {
            targets = result.getTargets();
            bestTarget = result.getBestTarget();
        }
    }

    public Pose3d estimatePose3d() {
        return PhotonUtils.estimateFieldToRobotAprilTag(
            bestTarget.getBestCameraToTarget(),
            StaticUtil.getTagFieldLayoutAM().getTagPose(bestTarget.getFiducialId()).get(),
            cameraToRobot);
    }

    public Rotation2d getYawToTag() {
        return PhotonUtils.getYawToPose(, )
    }

    private List<PhotonTrackedTarget> targets;
    private PhotonTrackedTarget bestTarget;
    private boolean hasTargets;
    private PhotonPipelineResult result;

    private PhotonCamera camera;

    private Transform3d cameraToRobot;
}
