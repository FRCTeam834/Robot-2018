//2018-Comp Bot Code

package org.usfirst.frc.team834.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
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
	WPI_VictorSPX intakeWheels;
	WPI_VictorSPX intakeGrab;
	//Speed Controllers and Intakes Created
	
	
	//Encoders, Gyro, and Limit Switches Created
	Encoder leftEncoder;
	Encoder rightEncoder;
	Encoder elevatorEncoder;
	ADXRS450_Gyro gyro;
	DigitalInput limitElevatorTop; // Photoeye
	DigitalInput limitElevatorBottom; // Limit Switch
	DigitalInput cubeSensor; // Proximity Sensor
	Spark led;
	//Encoders, Gyro, and Limit Switches Created
	
		
	@Override
	public void robotInit() {
		
		//Joysticks Initialized
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		xbox = new Joystick(2);
		//Joysticks Initialized
		
		
		//Drive Trains, Speed Controllers, and More Motors Initialized
		leftDrive = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(2), new WPI_VictorSPX(3));
		rightDrive = new SpeedControllerGroup(new WPI_VictorSPX(4),new WPI_VictorSPX(5), new WPI_VictorSPX(6));
		driveTrain = new DifferentialDrive(leftDrive, rightDrive);
		elevator = new WPI_VictorSPX(7);
		climber = new WPI_VictorSPX(8);
		intakeWheels = new WPI_VictorSPX(9);
		intakeGrab = new WPI_VictorSPX(10);
		//Drive Trains, Speed Controllers, and More Motors Initialized
		
		
		//Invert Motors To Make Logical (1.0 Up, -1.0 Down)
		//elevator.setInverted(true);
		climber.setInverted(true);
		//Invert Motors To Make Logical
		
		
		//Encoders, Gyro, and Limit Switches Initialized
		leftEncoder = new Encoder(0, 1);
		rightEncoder = new Encoder(2, 3);
		elevatorEncoder = new Encoder(4, 5);
		gyro = new ADXRS450_Gyro();
		limitElevatorTop = new DigitalInput(6);
		limitElevatorBottom = new DigitalInput(7);
		cubeSensor = new DigitalInput(8);
		led = new Spark(9);
		//Encoders, Gyro, and Limit Switches Initialized
		
		
		//DistancePerPulse Set
		//Distance per revolution - 256 pulse per revolution
		leftEncoder.setDistancePerPulse(-6.0*Math.PI/256.0);
		rightEncoder.setDistancePerPulse(6.0*Math.PI/256.0);
		//DistancePerPulse Set

		
		//Super Sensors and Motors for BuildAnAuton
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		super.sensors.put("elevatorEncoder", elevatorEncoder);
		super.sensors.put("gyro", gyro);
		super.sensors.put("limitElevatorTop", limitElevatorTop);		
		super.sensors.put("limitElevatorBottom", limitElevatorBottom);
		super.motors.put("elevator", elevator);
		super.motors.put("intakeWheels", intakeWheels);
		super.motors.put("intakeGrab", intakeGrab);
		//Super Sensors and Motors for BuildAnAuton
		
		
		//Disable Safety
		driveTrain.setSafetyEnabled(false);
		//Disable Safety
		
		
		//Sensors Calibrated and Reset
		leftEncoder.reset();
		rightEncoder.reset();
		elevatorEncoder.reset();
		gyro.calibrate();
		//Sensors Calibrated and Reset
	}
	
	
	@Override
	public void autonomous() { 
		
		//Everything below runs and chooses an auton
		
		//Sensor Reset
		leftEncoder.reset(); 
		rightEncoder.reset();
		elevatorEncoder.reset();
		//Sensors Reset
		
		
		
		//Creates ChooseAuton Object
		ChooseAuton c = new ChooseAuton(this);
		//Creates ChooseAuton Object
		
		
		
		//Chooses an auton based on the input on the smart dashboard and game data
		try {
			
			//Gets strings from SmartDashboard
			String robotLocation = SmartDashboard.getString("DB/String 0", ""); //Input is "Center", "Right", or "Left"
			String goal = SmartDashboard.getString("DB/String 1", ""); //Input is "Switch" or "Scale"
				
			//Gets plate location from DS
			String gameData = DriverStation.getInstance().getGameSpecificMessage(); //Gets 3 char string of plate locations

			//This is the auton file name that will run
			String auton = "BaseLine";
			
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
			//Runs a specified auton, DS 0 == "BYPASS", DS 1 == "Specified Auton"
			else if(robotLocation.equalsIgnoreCase("bypass")){
				auton = goal;
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
			//If the priority set is run, but not favorable, lets see if we can do the other goal straight ahead
			else if(goal.equalsIgnoreCase("switchp")) {
				if(robotLocation.charAt(0) == gameData.charAt(0)) {
					auton = robotLocation + "Switch" + gameData.charAt(0);
				}
				else if(robotLocation.charAt(0) == gameData.charAt(1)) {
					auton = robotLocation + "Scale" + gameData.charAt(1);
				}
				else {
					auton = robotLocation + "Scale" + gameData.charAt(1);
				}
			}
			else if(goal.equalsIgnoreCase("scalep")) {
				if(robotLocation.charAt(0) == gameData.charAt(1)) {
					auton = robotLocation + "Scale" + gameData.charAt(1);
				}
				else if(robotLocation.charAt(0) == gameData.charAt(0)) {
					auton = robotLocation + "Switch" + gameData.charAt(0);
				}
				else {
					auton = robotLocation + "Scale" + gameData.charAt(1);
				}
			}
			
			
		
			//Runs BaseLine auton if requested impossible
			//*****|| auton.equalsIgnoreCase("LeftScaleR") || auton.equalsIgnoreCase("RightScaleL")******
			if(auton.equalsIgnoreCase("LeftSwitchR") || auton.equalsIgnoreCase("RightSwitchL")) {
				auton = "BaseLine";
			}
			

			System.out.println(robotLocation);
			System.out.println(goal);
			System.out.println(gameData);
			System.out.println(auton);
			
			
			//Tells BuildAnAuton to play the correct auton
			c.chooseAuton(auton); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
			c.run();				
		}
		catch(Exception e) {
			//Doesn't Work, but could prevent a crash 
			System.out.println(e.getMessage());
			System.out.println("Auton Error in Try Statement, Running Base Line Auton");
			c.chooseAuton("BaseLine"); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
			c.run();
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
		
		
		//The xbox controller's X and Y buttons control the elevator
		if (xbox.getRawButton(4)) { //Y button (Up)
			// Implement Photoelectric Sensor
			//The if statement below checks to see if the elevator is at it's max
			if (limitElevatorTop.get()) { //Black Visible
				led.set(-0.89);
				//Sets the elevator to keep position when the Black Dot is Visible
				elevator.set(0.15);	
				//Run Rumble
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
			}
			else { //Black Not Visible
				led.set(0.87);
				//Sets the elevator to go up
				elevator.set(1.0);
			}			
		}
		else if (xbox.getRawButton(3)) { //X button (Down)
			// Implement Limit Switch 
			//The if statement below checks to see if the elevator is at it's minimum
			if (!limitElevatorBottom.get()) { //If Pressed
				led.set(-0.89);
				//Sets the elevator to keep position when the limit switch is pressed
				elevator.set(0.15);	
				//Run Rumble
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				//Reset Elevator Encoder
				elevatorEncoder.reset();
			}
			else { //If Not Pressed
				led.set(0.93);
				//Sets the elevator to go down
				elevator.set(-0.5);
			}
		} 
		else {
			//Resets Rumble
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);

			//This sets the elevators speed when neither x or y are pressed
			//Value of 0.1 is used to keep the elevator in position and strap taught
			elevator.set(0.15);
		}
		
		
		//These if statements cause the left and right triggers to control the motors to "swallow" and "spit out" the cubes
		if (xbox.getRawAxis(2) >= 0.75) { //This is the Left Trigger
			//When the left trigger is pressed, the wheels spin inward, allowing the robot to swallow some cubes
			intakeWheels.set(1.0);
			led.set(0.81);
		}
		else if (xbox.getRawAxis(3) >= 0.75) { //This is the right trigger
			//When the right trigger is pressed, the wheels spin outward, allowing the robot to spit out the cubes
			intakeWheels.set(-1.0);
			led.set(0.89);
		}
		else {
			//When neither trigger is pressed, the wheels for the intake are kept running to secure the cube.
			intakeWheels.set(0);
		}
		
		
		//This is to open or close the intake
		if (xbox.getRawButton(6)) { //This is the Left Bumper (Open)
			led.set(0.89);
			intakeGrab.set(1.0);	
		}
		else if (xbox.getRawButton(5)) {//Right Shoulder (Close)
			led.set(0.81);
			intakeGrab.set(-1.0);
		}
		else {
			//When neither bumper is pressed, the grabber for the intake is turned off.
			intakeGrab.set(0);
		}
		
		
		//Buttons that make your robot climb up
		if (xbox.getRawButton(2)){ //B (Climb Up)
			led.set(0.63);
			climber.set(1.0);
		}
		else if (xbox.getRawButton(8)) { //A (Climb Down)
			led.set(-0.11);
			climber.set(-1.0);
		}
		else {
			climber.set(0);
		}
		
		
		//Is a cube in? Then turn the leds green!
		if (!cubeSensor.get() && !limitElevatorBottom.get()) {
			led.set(0.77); //Green
		}
		else if (!limitElevatorBottom.get()) {
			led.set(-0.89); //Rainbow
		}
		
		
		//Heartbeat Leds in last 20 seconds
		if(DriverStation.getInstance().getMatchTime() <= 20) {
			led.set(-0.21);
		}
		
		
		//Reset Encoder
		if (xbox.getRawButton(7)){ //Reset Encoder
			leftEncoder.reset(); 
			rightEncoder.reset();
			elevatorEncoder.reset();
		}
		
		
		//Outputs Values to DS
		SmartDashboard.putString("DB/String 2", "Left:" + Double.toString(leftEncoder.getDistance()));
		SmartDashboard.putString("DB/String 3", "Right:" + Double.toString(rightEncoder.getDistance()));
		SmartDashboard.putString("DB/String 4", "Elevator:" + Double.toString(elevatorEncoder.getRaw()));
		SmartDashboard.putString("DB/String 5", "LimitElevatorTop:" + Boolean.toString(limitElevatorTop.get()));
		SmartDashboard.putString("DB/String 6", "LimitElevatorBot:" + Boolean.toString(!limitElevatorBottom.get()));
		SmartDashboard.putString("DB/String 7", "cubeSensor:" + Boolean.toString(!cubeSensor.get()));
	}
}

