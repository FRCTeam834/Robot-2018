package org.usfirst.frc.team834.robot;



import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import visualrobot.ChooseAuton;
import visualrobot.Command;
import visualrobot.MoveStraightCommand;
import visualrobot.VisualRobot;





public class Robot extends VisualRobot {



	DifferentialDrive driveTrain;
	Joystick leftStick;
	Joystick rightStick;
	Joystick xbox;
	
	
	//Speed Controllers
	SpeedControllerGroup leftDrive = new SpeedControllerGroup(new WPI_TalonSRX(1), new WPI_TalonSRX(2), new WPI_TalonSRX(3));
	SpeedControllerGroup rightDrive = new SpeedControllerGroup(new WPI_TalonSRX(4),new WPI_TalonSRX(5), new WPI_TalonSRX(6));
	SpeedController elevator = new WPI_TalonSRX(7);
	SpeedController climber = new WPI_TalonSRX(8);
	Talon intakeLeft = new Talon(0);//TalonSR PWM0
	Talon intakeRight = new Talon(1);//TalonSR PWM1
	Talon intakeGrab = new Talon(2);//TalonSR PWM2
	
	
	//Encoders
	Encoder leftEncoder = new Encoder(0, 1);
	Encoder rightEncoder = new Encoder(2, 3);
	Encoder elevatorEncoder = new Encoder(4,5);
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	//DigitalInput limitGrab = new DigitalInput(4);
	
	String gameData; //To be used later
	
	
	@Override
	public void robotInit() {
		driveTrain = new DifferentialDrive(leftDrive, rightDrive);

		//Distance per revolution/256 pulse per revolution
		leftEncoder.setDistancePerPulse(-6.0*Math.PI/256.0);
		rightEncoder.setDistancePerPulse(6.0*Math.PI/256.0);

		//gameData = removeCharAt(DriverStation.getInstance().getGameSpecificMessage(), 3); //Gets the game data and removes the third character
		
		
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		super.sensors.put("elevatorEncoder", elevatorEncoder);
		super.sensors.put("gyro", gyro);
		

		leftEncoder.reset();

		rightEncoder.reset();

		elevatorEncoder.reset();

		gyro.calibrate();

		gyro.reset();
		
		gyro.calibrate();
		
		driveTrain.setSafetyEnabled(false);

	}

	@Override
	public void autonomous() {

		rightEncoder.reset();
		leftEncoder.reset();

		//Gets strings from SmartDashboard

		String robotLocation = SmartDashboard.getString("DB/String 0", "default"); //Input is "Center", "Right", or "Left"
		String priority = SmartDashboard.getString("DB/String 1", "default"); //Input is "Switch" or "Scale"

		//Gets plate location from DS
		//String gameData = DriverStation.getInstance().getGameSpecificMessage(); //Gets 3 char string of plate locations

		
		
		//Tells BuildAnAuton to use the correct auton	
		//ChooseAuton chooseAnAuton = new ChooseAuton(this);
		//chooseAnAuton.chooseAuton(robotLocation + priority + gameData); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on

		
		
		System.out.println("Auton Started");
			ChooseAuton c = new ChooseAuton(this);

			c.chooseAuton(robotLocation);
			/*
			ArrayList<Command> t = c.cmdSet.getMain();
			System.out.println("Before Loop");
			for(Command com : t) {
System.out.println("In Loop");
				if(com.getClass().equals(visualrobot.MoveStraightCommand.class)) {
System.out.println("In MoveStraight");
					((MoveStraightCommand) com).distance -=  6;
System.out.println("Running MoveStraight");
				}

			}
			*/
			
			
			
			System.out.println("Done with Auton!");
	}



	@Override
	public void teleOpInit() {

		

		//Initializes Joystick Ports

		leftStick = new Joystick(1);

		rightStick = new Joystick(0);

		xbox = new Joystick(2);

		


	}

	

	@Override
	public void teleOpPeriodic() {

		

		// Makes joysticks control drivetrain

		driveTrain.tankDrive(-leftStick.getY(), -rightStick.getY());

		

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
		
		
		
		
		
		
		
		//All of the below is brandon's sudo code and will be dealt with at a later time.
		
		

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
		
		//SmartDashboard.putString("DB/String 8", !limitGrab.get()? "Open" : "Closed");

	}

	

	@Override
	public void setLeftSide(double speed) {

		System.out.println("Accessing setLeftSide");

		//Prevents speed from being set over max of 1

		speed = speed > 1 ? 1 : speed;

		speed = speed < -1 ? -1 : speed;

		

		leftDrive.set(speed);
		System.out.println("Accessing setLeftSide");

	}



	@Override
	public void setRightSide(double speed) {

		
		System.out.println("Accessing setRightSide");
		//Prevents speed from being set over max of 1

		speed = speed > 1 ? 1 : speed;

		speed = speed < -1 ? -1 : speed;

		

		rightDrive.set(speed);
		System.out.println("Accessing setRightSide");

	}

	
	public static String removeCharAt(String s, int pos) {
		
		return s.substring(0, pos - 1) + s.substring(pos);
	}



}