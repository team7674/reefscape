// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



package frc.robot;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import frc.robot.subsystems.arm.ElevatorWinch;
//import frc.robot.subsystems.arm.WristMotor;
import frc.robot.util.StaticUtil;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  //ElevatorWinch elevatorWinch = new ElevatorWinch();
  //WristMotor wristMotor = new WristMotor();

  //private final Pigeon2 pidgey = new Pigeon2(13, "rio");

  public Robot() {
    m_robotContainer = new RobotContainer();

    // Init functions for all of our motors
    //elevatorWinch.config();
    //elevatorWinch.setEncoderToZero();

    //wristMotor.configMotor();
    //wristMotor.setMotorToZero();
    //wristMotor.configEncoder();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void robotInit() {
    for (int port = 5700; port <= 5900; port++)
      PortForwarder.add(port, "vision.local", port);

    StaticUtil.staticInit();
  }

  @Override
  public void disabledPeriodic() {

  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}
}