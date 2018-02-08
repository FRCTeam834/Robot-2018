package org.usfirst.frc.team834.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import visualrobot.*;


public class Robot extends VisualRobot {

	DifferentialDrive driveTrain;
	Joystick leftStick;
	Joystick rightStick;
	Joystick xbox;
	
	Encoder leftEncoder = new Encoder(0, 1);
	Encoder rightEncoder = new Encoder(2, 3);
	//Encoder elevatorEncoder = new Encoder(4,5);
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	SpeedController leftDrive = new MultiSpeedController(new WPI_TalonSRX(1), new WPI_TalonSRX(2), new WPI_TalonSRX(3));
	SpeedController rightDrive = new MultiSpeedController(new WPI_TalonSRX(4),new WPI_TalonSRX(5), new WPI_TalonSRX(6));
	
	SpeedController elevator = new WPI_TalonSRX(7);
	SpeedController climber = new WPI_TalonSRX(8);
	
	//SpeedController intakeLeft = ;//TalonSR PWM0
	//SpeedController intakeRight = ;//TalonSR PWM1
	//SpeedController intakeGrab = ;//TalonSR PWM2
	
	
	
	
	@Override
	public void robotInit() {
		
		driveTrain = new DifferentialDrive(left, right);
		
		//Distance per revolution/256 pulse per revolution
		leftEncoder.setDistancePerPulse(256);
		rightEncoder.setDistancePerPulse(256);
		
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		super.sensors.put("elevatorEncoder", elevatorEncoder);
		super.sensors.put("gyro", gyro);
		
		leftEncoder.reset();
		rightEncoder.reset();
		elevatorEncoder.reset();
		gyro.calibrate();
		gyro.reset();
	}

	@Override
	public void autonomous() {
		
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

	@Override
	public void teleOpInit() {
		
		//Initializes Joystick Ports
		leftStick = new Joystick(1);
		rightStick = new Joystick(0);
		xbox = new Joystick(2);
		
		//Initializes boolean
		boolean intakeEnabled = false;
	}
	
	@Override
	public void teleOpPeriodic() {
		
		// Makes joysticks control drivetrain
		driveTrain.tankDrive(leftStick.getY(), rightStick.getY());
		
		// Makes xbox control elevator
		if (xbox.getRawButton(4)) {
			elevator.set(1.0);
		} else if (xbox.getRawButton(1)) {
			elevator.set(-1.0);
		} else {
			elevator.set(0);
		}
		
		/*
		// Makes xbox control climber
		if (xbox.getRawButton()) {
			climber.set(1.0);
		} else if (xbox.getRawButton()) {
			climber.set(-1.0);
		} else {
			climber.set(0);
		}
		
		// Makes xbox control intake
		if(xbox.getRawButton()){
			intakeEnabled = !intakeEnabled;
		}
		if(intakeEnabled == true){
			intakeLeft.set(1.0);
			intakeRight.set(-1.0);
		} else if (intakeEnabled == true && intakeLimit = 1){
			intakeLeft.set(0);
			intakeRight.set(0);
		} else if (intakeEnabled == true && intakeLimit = 1 && xbox.getRawButton(shoot)){
			intakeLeft.set(-1.0);
			intakeRight.set(1.0);
		} else if (intakeEnabled == false) {
			intakeLeft.set(0);
			intakeRight.set(0);
		}
		
		
		// Makes xbox control grab
		if (intakeLimit = 1) {
			intakeGrab.set(1.0);
		} else if (xbox.getRawButton()) {
			intakeGrab.set(-1.0);
		} else {
			intakeGrab.set(0);
		}
		
		
		
		
		*/
		
		//Outputs Encoder Values to DS
		SmartDashboard.putString("DB/String 6", Double.toString(leftEncoder.getDistance()));
		SmartDashboard.putString("DB/String 7", Double.toString(rightEncoder.getDistance()));
	}
	
	@Override
	public void setLeftSide(double speed) {
		
		//Prevents speed from being set over max of 1
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		left.set(speed);
	}

	@Override
	public void setRightSide(double speed) {
		
		//Prevents speed from being set over max of 1
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		right.set(speed);
	}
	

}
