package org.usfirst.frc.team834.robot;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import basicCommand.DelayCommand;

import visualrobot.ChooseAuton;
import visualrobot.Command;
import visualrobot.MoveStraightCommand;
import visualrobot.VisualRobot;


public class Robot extends VisualRobot {

	//Joysticks and DriveTrain Created
	DifferentialDrive driveTrain;
	Joystick leftStick;
	Joystick rightStick;
	Joystick xbox;
	
	//Speed Controllers Created
	SpeedControllerGroup leftDrive;
	SpeedControllerGroup rightDrive;
	SpeedController elevator;
	SpeedController climber;
	Talon intakeLeft;
	Talon intakeRight;
	Talon intakeGrab;
	
	//Encoders, Gyros, Sensors Created
	Encoder leftEncoder;
	Encoder rightEncoder;
	/**Encoder elevatorEncoder;*/
	ADXRS450_Gyro gyro;
	/**DigitalInput limitIntakeClosed;*/
	
	
	@Override
	public void robotInit() {
		
		//Joysticks Initialized
		leftStick = new Joystick(1);
		rightStick = new Joystick(0);
		xbox = new Joystick(2);
		
		//Speed Controllers and driveTrain Initialized
		leftDrive = new SpeedControllerGroup(new WPI_TalonSRX(1), new WPI_TalonSRX(2), new WPI_TalonSRX(3));
		rightDrive = new SpeedControllerGroup(new WPI_TalonSRX(4),new WPI_TalonSRX(5), new WPI_TalonSRX(6));
		driveTrain = new DifferentialDrive(leftDrive, rightDrive);
		elevator = new WPI_TalonSRX(7);
		climber = new WPI_TalonSRX(8);
		intakeLeft = new Talon(0);//TalonSR PWM0
		intakeRight = new Talon(1);//TalonSR PWM1
		intakeGrab = new Talon(2);//TalonSR PWM2
		
		//Encoders, Gyros, Sensors Initialized
		leftEncoder = new Encoder(0, 1);
		rightEncoder = new Encoder(2, 3);
		/**elevatorEncoder = new Encoder(4,5);*/
		gyro = new ADXRS450_Gyro();
		/**limitIntakeClosed = new DigitalInput(4);*/
		
		//Distance per revolution - 256 pulse per revolution
		leftEncoder.setDistancePerPulse(-6.0*Math.PI/256.0);
		rightEncoder.setDistancePerPulse(6.0*Math.PI/256.0);

		//Super Sensors and Motors
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		/**super.sensors.put("elevatorEncoder", elevatorEncoder);*/
		super.sensors.put("gyro", gyro);
		/**super.sensors.put("limitIntakeClosed", limitIntakeClosed);*/
		
		super.motors.put("elevator", elevator);
		super.motors.put("intakeLeft", intakeLeft);
		super.motors.put("intakeRight", intakeRight);
		super.motors.put("intakeGrab", intakeGrab);
		
		//Disable Safety
		driveTrain.setSafetyEnabled(false);
	}
	
	
	@Override
	public void autonomous() {

		//Sensor Calibrate and Reset
		gyro.calibrate();
		leftEncoder.reset();
		rightEncoder.reset();
		/**elevatorEncoder.reset();*/

		try {
			//Gets strings from SmartDashboard
			String robotLocation = SmartDashboard.getString("DB/String 0", "default"); //Input is "Center", "Right", or "Left"
			String goal = SmartDashboard.getString("DB/String 1", "default"); //Input is "Switch" or "Scale"
				
			//Gets plate location from DS
			String gameData = DriverStation.getInstance().getGameSpecificMessage(); //Gets 3 char string of plate locations
				
			//Initializes plateLocation for future use
			String plateLocation = ""; 
				
			//Selects the current plateLocation for the selected goal
			if(priority.equalsIgnoreCase('switch')){
				plateLocation = gameData.charAt(0);
			}
			else {
				plateLocation = gameData.charAt(1);
			}
				
			//Tells BuildAnAuton to play the corrrect auton	
			ChooseAuton c = new ChooseAuton(this);
			c.chooseAuton(robotLocation + goal + plateLocation); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	public void setLeftSide(double speed) {
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		leftDrive.set(-speed);
	}

	public void setRightSide(double speed) {
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		rightDrive.set(speed);
	}
	

	@Override
	public void teleOpInit() {

	}


	@Override
	public void teleOpPeriodic() {

		// Makes joysticks control drivetrain
		/**driveTrain.tankDrive(leftStick, rightStick);*/
		/**driveTrain.arcadeDrive(rightStick);*/
		setRightSide(-rightJoystick.getY());
		setLeftSide(-leftJoystick.getY());

		// Makes xbox control elevator
		if (xbox.getRawButton(3)) {
			elevator.set(1.0);
		} else if (xbox.getRawButton(4)) {
			elevator.set(-1.0);
		} else {
			elevator.set(0);
		}

		//Button to pull in and push out the cube
		if (xbox.getRawAxis(2) >= 0.75 /*xbox.getRawButton(2)*/) {
			intakeLeft.set(-1.0);
			intakeRight.set(-1.0);
		} else if (xbox.getRawAxis(3) >= 0.75 /*xbox.getRawButton(3)*/) {
			intakeLeft.set(1.0);
			intakeRight.set(1.0);
		} else {
			intakeLeft.set(0);
			intakeRight.set(0);
		}
		
		//Button to close or open the intake
		if (xbox.getRawButton(5)) {   
			intakeGrab.set(1.0);
		} else if (xbox.getRawButton(6)) {
			intakeGrab.set(-1.0);
		} else {
			intakeGrab.set(0);
		}
		
		//Outputs Encoder Values to DS
		SmartDashboard.putString("DB/String 5", Double.toString("L01:"+leftEncoder.getDistance()));
		SmartDashboard.putString("DB/String 6", Double.toString("R23:"+rightEncoder.getDistance()));
	}
}