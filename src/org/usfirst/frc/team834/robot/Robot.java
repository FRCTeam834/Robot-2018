package org.usfirst.frc.team834.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import visualrobot.ChooseAuton;
import visualrobot.VisualRobot;

public class Robot extends VisualRobot {

	
	//Joysticks and DriveTrain Created
	DifferentialDrive driveTrain;
	Joystick leftStick;
	Joystick rightStick;
	Joystick xbox;
	//Joysticks and DriveTrain Created
	
	
	//Speed Controllers and Intakes Created
	SpeedControllerGroup leftDrive;
	SpeedControllerGroup rightDrive;
	SpeedController elevator;
	SpeedController climber;
	Talon intakeLeft;
	Talon intakeRight;
	Talon intakeGrab;
	//Speed Controllers and Intakes Created
	
	
	//Encoders, Gyro, and Limit Switches Created
	Encoder leftEncoder;
	Encoder rightEncoder;
	ADXRS450_Gyro gyro;
	DigitalInput limitIntake;
	DigitalInput limitElevatorTop;
	//Encoders, Gyro, and Limit Switches Created
	
	
	//Other creations that do stuff
	String aUselessVariable;

	//Other creations that do stuff
	
	
	
	@Override
	public void robotInit() {
		
		
		//Joysticks Initialized
		leftStick = new Joystick(1);
		rightStick = new Joystick(0);
		xbox = new Joystick(2);
		//Joysticks Initialized
		
		
		//Drive Trains, Speed Controllers, and More Motors Initialized
		leftDrive = new SpeedControllerGroup(new WPI_TalonSRX(1), new WPI_TalonSRX(2), new WPI_TalonSRX(3));
		rightDrive = new SpeedControllerGroup(new WPI_TalonSRX(4),new WPI_TalonSRX(5), new WPI_TalonSRX(6));
		driveTrain = new DifferentialDrive(leftDrive, rightDrive);
		elevator = new WPI_TalonSRX(7);
		climber = new WPI_TalonSRX(8);
		intakeLeft = new Talon(0);//TalonSR PWM0
		intakeRight = new Talon(1);//TalonSR PWM1
		intakeGrab = new Talon(2);//TalonSR PWM2
		//Drive Trains, Speed Controllers, and More Motors Initialized
		
		
		//Encoders, Gyro, and Limit Switches Initialized
		leftEncoder = new Encoder(0, 1);
		rightEncoder = new Encoder(2, 3);
		gyro = new ADXRS450_Gyro();
		limitIntake = new DigitalInput(5);
		limitElevatorTop = new DigitalInput(4);
		//Encoders, Gyro, and Limit Switches Initialized
		
		
		//DistancePerPulse Set
		//Distance per revolution - 256 pulse per revolution
		//leftEncoder.setDistancePerPulse(2*-6.0*Math.PI/256.0)
		leftEncoder.setDistancePerPulse(-6.0*Math.PI/256.0);
		rightEncoder.setDistancePerPulse(6.0*Math.PI/256.0);
		//DistancePerPulse Set

		
		//Super Sensors and Motors for BuildAnAuton
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		//super.sensors.put("elevatorEncoder", elevatorEncoder);
		super.sensors.put("gyro", gyro);
		//super.sensors.put("limitIntakeClosed", limitIntakeClosed);
		super.sensors.put("limitElevatorHeight", limitElevatorTop);
		super.sensors.put("limitIntakeClosed", limitIntake);
		super.motors.put("elevator", elevator);
		super.motors.put("intakeLeft", intakeLeft);
		super.motors.put("intakeRight", intakeRight);
		super.motors.put("intakeGrab", intakeGrab);
		//Super Sensors and Motors for BuildAnAuton
		
		
		//Disable Safety
		driveTrain.setSafetyEnabled(false);
		//Disable Safety
		
		
		//Sensors Calibrated and Reset
		gyro.calibrate();
		leftEncoder.reset();
		rightEncoder.reset();
		//Sensors Calibrated and Reset
	}
	
	
	
	@Override
	public void autonomous() { 
		
		//Everything below runs and chooses an auton
		
		
		//Sensor Calibrate and Reset
		/*gyro.calibrate();*/ //Takes 5 Seconds to Calibrate
		leftEncoder.reset(); 
		rightEncoder.reset();
		/*elevatorEncoder.reset();*/
		//Sensors Calibrate and Reset
		

		//Chooses an auton based on the input on the smart dashboard and game data
		try {
			//Gets strings from SmartDashboard
			String robotLocation = SmartDashboard.getString("DB/String 0", ""); //Input is "Center", "Right", or "Left"
			String goal = SmartDashboard.getString("DB/String 1", ""); //Input is "Switch" or "Scale"
				
			//Gets plate location from DS
			String gameData = DriverStation.getInstance().getGameSpecificMessage(); //Gets 3 char string of plate locations
				
			//This is the auton file name that will run
			String auton = "";
			
			//This makes sure the correct auton will be selected regardless of inputed letter case
			if(robotLocation.equalsIgnoreCase("left")) {
				robotLocation = "Left";
			}
			else if(robotLocation.equalsIgnoreCase("right")) {
				robotLocation = "Right";
			}
			else if(robotLocation.equalsIgnoreCase("center")) {
				robotLocation = "Center";
			}
			
			//Selects the current plateLocation for the selected goal
			if(goal.equalsIgnoreCase("switch")){
				//Chooses auton based on location of robot, what priority for that round is, and which side the colors on
				auton = robotLocation + "Switch" + gameData.charAt(0);
			}
			else if(goal.equalsIgnoreCase("scale")) {				
				//Chooses auton based on location of robot, what priority for that round is, and which side the colors on
				auton = robotLocation + "Scale" + gameData.charAt(1);
			}
			else {				
				//Chooses auton based on location of robot, what priority for that round is, and which side the colors on
				auton = robotLocation;
			}
			
			//Tells BuildAnAuton to play the correct auton	
			ChooseAuton c = new ChooseAuton(this);
			c.chooseAuton(auton); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
			c.run();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		//Done choosing an auton and running it
		
		
	}
	
	
	
	public void setLeftSide(double speed) {
		
		
		//Used during build an auton to make sure speed doesn't go above 1
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		//Used during build an auton to make sure speed doesn't go above 1
	
		
		leftDrive.set(speed);
		
		
	}

	
	
	public void setRightSide(double speed) {
		
		
		//Used during build an auton to make sure speed doesn't go above 1
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		//Used during build an auton to make sure speed doesn't go above 1
		
		
		rightDrive.set(-speed);
		
		
	}
	

	
	@Override
	public void teleOpInit() {
		
		
		//There is nothing that must happen during teleOp Initialization
		
		
	}
	
	
	
	@Override
	public void teleOpPeriodic() {

		
		//Allows the joysticks to control the robot driving
		//This should be used used instead of independently setting the right and left side
		driveTrain.tankDrive(-leftStick.getY(), -rightStick.getY());
		//Joysticks control robot ^^^
		
		
		
		//The xbox controller's X and Y buttons control the elevator
		if (xbox.getRawButton(3)) { //X button
			
			
			//The X button here causes the elevator to go down when it is pressed
			elevator.set(1.0 /*The 1.0 causes the motors to spin, making the elevator to go down*/);
			
			
		} 
		
		
		else if (xbox.getRawButton(4)) { //Y button
			
			
			//This code runs when the Y button is pressed
			//The if statement below checks to see if the limit switch is pressed
			if (!limitElevatorTop.get()) { //The limit switch displays true at all times but false when pressed
				
				//Sets the elevator to go up when the limit switch is not pressed
				elevator.set(-1.0);	
				
			}
			
			
			//If the elevator limit switch is pressed, then the below code is run
			else if (limitElevatorTop.get()) {
				
				//The elevator is set to -0.1 when the limit switch is pressed, so the elevator stays up at the top
				//but the motor does not keep running at max speed and get damaged
				elevator.set(-0.1); //###THIS IS SET TO -1.0 RIGHT NOW BECAUSE LIMIT SWITCH DOES NOT WORK, SET TO -0.1 WHEN FIXED / FOR REAL ROBOT!!!####
				
			}}
		
		
		else if (xbox.getRawButton(8)) { //Start button
			
			//The start button on the xbox controller sets the motor to -0.1
			//this holds the elevator in place, so it does not drop from gravity.
			elevator.set(-0.1);
		
			
		}
		
		
		else {
			
			//This sets the elevator to stop moving if neither x or y are pressed
			elevator.set(0);
			//Setting the above value to -0.1 could be useful, but keeps the motor running the entire teleOp period.
			
		}
		
		
		
		//These if statements cause the left and right triggers to control the motors to "swallow" and "spit out" the cubes
		if (xbox.getRawAxis(2) >= 0.75) { //This is the Left Trigger
			
			//When the left trigger is pressed, the wheels spin inward, allowing the robot to swallow some cubes
			intakeLeft.set(-1.0);
			intakeRight.set(1.0);
						
		}
		
		
		else if (xbox.getRawAxis(3) >= 0.75) { //This is the right trigger
			
			//When the right trigger is pressed, the wheels spin outward, allowing the robot to spit out the cubes
			intakeLeft.set(1.0);
			intakeRight.set(-1.0);
			
			
		}
		
		
		else {
			
			//When neither trigger is pressed, the wheels for the intake are turned off.
			intakeLeft.set(0);
			intakeRight.set(0);
			
			
		}
		
		
		//This is to control if the intake is open or closed
		if (xbox.getRawButton(5)) { //This is the Left Shoulder Button
			
			//This checks /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (!limitIntake.get()) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);				
			} else {
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);
			}
			intakeGrab.set(1.0);

							
		} else if (xbox.getRawButton(6)) {//Right Shoulder
		
			if (!limitIntake.get()) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);

			} else {
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);

			}
			
			intakeGrab.set(-1.0);

			
		} else {
			
			//Resets if nothing is pressed
			intakeGrab.set(0);
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);
			/*
			if (leftStick.getRawButton(7)) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 0);
				
			}
			
			else if (rightStick.getRawButton(7)){
				
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				
			} else {
		
				xbox.setRumble(kLeftRumble, 0);
				xbox.setRumble(kRightRumble, 0);
			
			}
			*/
		}
		
		//Buttons that make your robot climb up and down
		if(xbox.getRawButton(1)){ //A/Climb Down
			climber.set(1.0); //Extend
		}
		else if(xbox.getRawButton(2 )){ //B/Climb Up
			climber.set(-1.0); //Retract (Robot goes up)
		}
		else {
			climber.set(0);
		}
	/*	
		if (!limitElevatorHeight.get()) {
			
			elevator.set(0);
			
		}
		*/
		

		//Outputs Values to DS
		SmartDashboard.putString("DB/String 5", "Left:" + Double.toString(leftEncoder.getDistance()));
		SmartDashboard.putString("DB/String 6", "Right:" + Double.toString(rightEncoder.getDistance()));
		SmartDashboard.putString("DB/String 7", "LimitIntake:" + Boolean.toString(!limitIntake.get()));
		SmartDashboard.putString("DB/String 8", "LimitElevator:" + Boolean.toString(!limitElevatorTop.get()));

		
		
		
		
		
		
	}
}



/*			
 * Krishna and Dom have some fun plans for the future :D
 * xbox.setRumble(RumbleType.kLeftRumble, 1);
 * xbox.setRumble(RumbleType.kRightRumble, 1);
 */

