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
	RumbleType kRightRumble;
	RumbleType kLeftRumble;
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
		if (xbox.getRawButton(3)) { //X/Elevator Down
			
			elevator.set(1.0);
			//elevator.set(1.0);
			
		} else if (xbox.getRawButton(4)) { //Y/Elevator Up
			
			if (limitElevatorTop.get()) {
				
				elevator.set(-1.0);
				
			} else if (!limitElevatorTop.get()) {
				
				elevator.set(-1.0); //SET TO (-.1)
				
			}
			
			//elevator.set(-1.0);
			
		} else if (xbox.getRawButton(8)) { //  Start button stops elevator and holds in place from da gravityies
			
			elevator.set(-.1);
			
		}
		
		else {
			elevator.set(0);
			
		}

		
		//Button to pull in and push out the cube (2 is intakeIn)
		if (xbox.getRawAxis(2) >= 0.75) { //Left Trigger
			intakeLeft.set(-1.0);
			intakeRight.set(-1.0);

			
			
		} else if (xbox.getRawAxis(3) >= 0.75) { //Right Trigger
			intakeLeft.set(1.0);
			intakeRight.set(1.0);
			
			
		} else {
			intakeLeft.set(0);
			intakeRight.set(0);
		}
		
		//Testing the little spinny thingies on the xbox controller!
		if (xbox.getRawAxis(0) >= 0.75) { //Left Trigger
			xbox.setRumble(RumbleType.kLeftRumble, 1);
			xbox.setRumble(RumbleType.kRightRumble, 1);
			
			
		} else if (xbox.getRawAxis(1) >= 0.75) { //Right Trigger
			xbox.setRumble(RumbleType.kLeftRumble, 1);
			xbox.setRumble(RumbleType.kRightRumble, 1);

			
		} else {
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);
			
			
		}
		

		
		
		
		
		
		
		
		
		
		
		//Button to close or open the intake (5 is closed)
		if (xbox.getRawButton(5)) { //Left Shoulder
			
			if (!limitIntake.get()) {
				
				xbox.setRumble(kLeftRumble, 1);
				xbox.setRumble(kRightRumble, 1);
				
			} else {
				xbox.setRumble(kLeftRumble, 0);
				xbox.setRumble(kRightRumble, 0);
				
				if (xbox.getRawAxis(5) >= 75) {
					
					xbox.setRumble(kLeftRumble, 1);
					xbox.setRumble(kRightRumble, 1);
					
				} else { 
					
					xbox.setRumble(kLeftRumble, 0);
					xbox.setRumble(kRightRumble, 0);
				}
			}
			
			intakeGrab.set(1.0);
			
		} else if (xbox.getRawButton(6)) {//Right Shoulder
		
			
			
			if (!limitIntake.get()) {
				
				xbox.setRumble(RumbleType.kLeftRumble, 1);
				xbox.setRumble(RumbleType.kRightRumble, 1);
				
				intakeGrab.set(-1.0);
				
			} else {
				xbox.setRumble(RumbleType.kLeftRumble, 0);
				xbox.setRumble(RumbleType.kRightRumble, 0);
				
				intakeGrab.set(-1.0);
			}
			
			//intakeGrab.set(-1.0);
			
			
		} else {
			
			//Resets if nothing is pressed
			intakeGrab.set(0);
			xbox.setRumble(RumbleType.kLeftRumble, 0);
			xbox.setRumble(RumbleType.kRightRumble, 0);
			
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
		}
		
		//Buttons that make your robot climb up and down
		if(xbox.getRawButton(1)){ //A/Climb Down
			climber.set(1.0); //Extend
		}
		else if(xbox.getRawButton(2 )){ //B/Climb Up
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
		SmartDashboard.putString("DB/String 7", "LimitIntake:" + Boolean.toString(!limitIntake.get()));
		SmartDashboard.putString("DB/String 8", "LimitElevator:" + Boolean.toString(!limitElevatorTop.get()));

		
		
		
		
		
		
	}
}



/*			
 * Krishna and Dom have some fun plans for the future :D
 * xbox.setRumble(RumbleType.kLeftRumble, 1);
 * xbox.setRumble(RumbleType.kRightRumble, 1);
 */

