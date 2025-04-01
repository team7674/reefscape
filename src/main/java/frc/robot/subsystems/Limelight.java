package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.drive.CommandSwerveDrivetrain;
import frc.robot.util.MovingAverage;

public class Limelight extends SubsystemBase {

    //initialize everything

    LimelightHelpers helper = new LimelightHelpers();

    private NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    CommandSwerveDrivetrain drive;
    MovingAverage[] positionSmoother = new MovingAverage[6];
    
    double[] targetpose_botspace = new double[6];
    double[] botpose_targetspace = new double[6];
    double[] botpose_fieldspace = new double[6];
    double[] smoothedPosition = new double[6];
    
    int id;
    double tx;

    public Limelight(CommandSwerveDrivetrain drive) {
        this.drive = drive;

        for (int i = 0; i < 6; i++) {
            positionSmoother[i] = new MovingAverage(50);
        }
    }

    public void init() {
        
    }
    
    //this will correct the robot space axis to match gyro
    public static double[] correctRobotSpaceAxis(double[] input) {
        double[] output = new double[6];
        output[0] = input[0];
        output[1] = input[1];
        output[2] = input[2];
        output[3] = input[3];
        output[4] = input[4];
        output[5] = input[5];
        return output;
    }

    public double[] getTagPos() {
        return targetpose_botspace;
    }

    public Pose3d getBotPos_TagSpace() {

        return LimelightHelpers.getBotPose3d_TargetSpace("limelight");
    }
    
    public double[] getBotPos() {
        //Pose3d botPose3d = LimelightHelpers.getBotPose3d("limelight-belly");

        return botpose_fieldspace;
    }
    
    public int getID() {
        System.out.print(id);
        return id;
    }

    @Override
    public void periodic() {

        id = (int)limelightTable.getEntry("tid").getDouble(0.0); //grabs limelight ID
        targetpose_botspace = correctRobotSpaceAxis(limelightTable.getEntry("targetpose_robotspace").getDoubleArray(new double[6])); //grabs targetspace
        botpose_targetspace = limelightTable.getEntry("botpose_targetspace").getDoubleArray(new double[6]); //grabs currentspace

        var alliance = DriverStation.getAlliance(); //grabs team side-

        if (alliance.isPresent()){ //finds out if we are even fielding around or not
            if (alliance.get() == DriverStation.Alliance.Red){
                botpose_fieldspace = limelightTable.getEntry("botpose_wpired").getDoubleArray(new double[6]);
            } else {
                botpose_fieldspace = limelightTable.getEntry("botpose_wpiblue").getDoubleArray(new double[6]);
            }
        } else {
            botpose_fieldspace = limelightTable.getEntry("botpose_wpiblue").getDoubleArray(new double[6]);
        }

        tx = limelightTable.getEntry("tx").getDouble(0.0); //grabs x coord of the tag

        for (int i = 0; i < 6; i++) { //updates position smoother
            if (id > -1) {
                positionSmoother[i].update(botpose_fieldspace[i]);
            }
            smoothedPosition[i] = positionSmoother[i].getAverage(35);
        }
        //SmartDashboard.putNumber("realAngle", drivePos.getRotation().getDegrees());

        /*
        SmartDashboard.putNumber("TREVOR: drive x", botpose_targetspace[0]);
        SmartDashboard.putNumber("TREVOR: drive y", botpose_targetspace[1]);
        SmartDashboard.putNumber("TREVOR: drive z", botpose_targetspace[2]);

        SmartDashboard.putNumber("LIMELIGHT: drive x",
            LimelightHelpers.getBotPose3d_TargetSpace("limelight").getX());
        SmartDashboard.putNumber("LIMELIGHT: drive y",
            LimelightHelpers.getBotPose3d_TargetSpace("limelight").getY());
        SmartDashboard.putNumber("LIMELIGHT: drive yaw",
            LimelightHelpers.getBotPose3d_TargetSpace("limelight").getRotation().getZ() * 2 * Math.PI);

        SmartDashboard.updateValues();
        */
    }

    //public Pose2d getLimelightPose() {
    //    return limelightPos;
    //}
}