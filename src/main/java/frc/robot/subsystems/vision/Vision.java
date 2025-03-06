package frc.robot.subsystems.vision;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.subsystems.vision.util.VisionUtil;

public class Vision extends SubsystemBase {

    Vector<PhotonVisionClient> photons;

    Pose2d lastPose2d;

    public Vision() {
        photons = new Vector<PhotonVisionClient>(10);
        lastPose2d = new Pose2d();
    }

    @Override
    public void periodic() {
        
    }

    public void addPhotonCamera(String key) {
        photons.addElement(new PhotonVisionClient(key));
    }

    public void update() {
        for (PhotonVisionClient photon : photons) {
            photon.update();
        }
    }

    /**
     * averages the Pose2D output of all compliant cameras
     * 
     * @return the Filtered Pose2D of the robot
     *
    public Pose2d getPose2d(CommandSwerveDrivetrain drive) {
        return photon.estimateBotPose2d(drive);
    }
        */
}
