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
	/*Encoder elevatorEncoder;*/
	ADXRS450_Gyro gyro;
	DigitalInput limitIntakeClosed;
	DigitalInput limitElevatorHeight;
		
	String aUselessVariable;
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
		/*elevatorEncoder = new Encoder(4,5);*/
		gyro = new ADXRS450_Gyro();
		limitIntakeClosed = new DigitalInput(5);
		limitElevatorHeight = new DigitalInput(4);
		
		//Distance per revolution - 256 pulse per revolution
		leftEncoder.setDistancePerPulse(-6.0*Math.PI/256.0);
		rightEncoder.setDistancePerPulse(6.0*Math.PI/256.0);

		//Super Sensors and Motors
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("rightEncoder", rightEncoder);
		/*super.sensors.put("elevatorEncoder", elevatorEncoder);*/
		super.sensors.put("gyro", gyro);
		/*super.sensors.put("limitIntakeClosed", limitIntakeClosed);*/
		
		super.motors.put("elevator", elevator);
		super.motors.put("intakeLeft", intakeLeft);
		super.motors.put("intakeRight", intakeRight);
		super.motors.put("intakeGrab", intakeGrab);
		
		//Disable Safety
		driveTrain.setSafetyEnabled(false);
		
		//Sensor Calibrate and Reset
				gyro.calibrate();
				leftEncoder.reset();
				rightEncoder.reset();
				/*elevatorEncoder.reset();*/
	}
	
	
	@Override
	public void autonomous() {

		//Sensor Calibrate and Reset
		/*gyro.calibrate();*/ //Takes 5 Seconds to Calibrate
		leftEncoder.reset();
		rightEncoder.reset();
		/*elevatorEncoder.reset();*/

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
			
			//Checks if using overrided file
			if (!SmartDashboard.getString("DB/String 4", "").isEmpty()) {
				auton = SmartDashboard.getString("DB/String 4", "");
			}
			
			//Tells BuildAnAuton to play the correct auton	
			ChooseAuton c = new ChooseAuton(this);
			c.chooseAuton(auton); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
			c.run();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setLeftSide(double speed) {
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		leftDrive.set(speed);
	}

	public void setRightSide(double speed) {
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		rightDrive.set(-speed);
	}
	

	@Override
	public void teleOpInit() {

	}


	@Override
	public void teleOpPeriodic() {

		// Makes joysticks control drivetrain
		//This should be used used instead of independently setting the right and left side
		driveTrain.tankDrive(-leftStick.getY(), -rightStick.getY());

		// Makes xbox control elevator (-1 is up)
		if (xbox.getRawButton(3)) {
			
			elevator.set(1.0);
			//elevator.set(1.0);
			
		} else if (xbox.getRawButton(4)) {
			
			if (limitElevatorHeight.get()) {
				
				elevator.set(-1.0);
				
			} else if (!limitElevatorHeight.get()) {
				
				elevator.set(-.1);
				
			}
			
			//elevator.set(-1.0);
			
		} else if (xbox.getRawButton(8)) { //  Start button stops elevator and holds in place from da gravityies
			
			elevator.set(-.1);
			
		}
		
		else {
			elevator.set(0);
			
		}

		
		//Button to pull in and push out the cube (2 is intakeIn)
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
		
		//Button to close or open the intake (5 is closed)
		if (xbox.getRawButton(5)) { 
			
			if (!limitIntakeClosed.get()) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				
			} else {
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);
			}
			
			intakeGrab.set(1.0);
			
		} else if (xbox.getRawButton(6)) {
			
			if (!limitIntakeClosed.get()) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				
				intakeGrab.set(-.1);
				
			} else {
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);
				
				intakeGrab.set(-1.0);
			}
			
			//intakeGrab.set(-1.0);
			
			
		} else {
			
			intakeGrab.set(0);
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);
			
			if (leftStick.getRawButton(1)) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				
			}
			
			else {
				
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);
				
			}
		}
		
		//Buttons that make your robot climb up and down
		if(xbox.getRawButton(1)){ //Back Button
			climber.set(1.0); //Extend
		}
		else if(xbox.getRawButton(2 )){ //Start Button
			climber.set(-1.0); //Retract (Robot goes up)
		}
		else{
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
		SmartDashboard.putString("DB/String 7", "LimitIntake:" + Boolean.toString(!limitIntakeClosed.get()));
		SmartDashboard.putString("DB/String 8", "LimitElevator:" + Boolean.toString(!limitElevatorHeight.get()));


		
		
		

		
		
		
		
		
		
		
		
		
		
		
	}
}



/*			
 * Krishna and Dom have some fun plans for the future :D
 * xbox.setRumble(RumbleType.kLeftRumble, 1);
 * xbox.setRumble(RumbleType.kRightRumble, 1);
 */

