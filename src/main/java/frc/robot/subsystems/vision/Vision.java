package frc.robot.subsystems.vision;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.util.VisionUtil;

public class Vision extends SubsystemBase {

    List<PhotonVisionClient> photons;
    List<Pose2d> lastPoses2d;

    public Vision() {
        
    }

    @Override
    public void periodic() {
        
    }

    public void update() {
        for (PhotonVisionClient photonCam : photons) {
            photonCam.update();
        }
    }

    /**
     * averages the Pose2D output of all compliant cameras
     * 
     * @return the Filtered Pose2D of the robot
     */
    public Pose2d getPose2d() {
        lastPoses2d.clear();

        for ( PhotonVisionClient photon : photons ) {
            lastPoses2d.add(photon.estimatePose2d());
        }

        return VisionUtil.averagePoses2D(lastPoses2d);
    }

    public Vision withPhotonClient(String key) {
        this.photons.add(new PhotonVisionClient(key));
        return this;
    }
}
