package org.usfirst.frc.team834.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import visualrobot.*;


public class Robot extends VisualRobot {

	private DifferentialDrive driveTrain;
	private Joystick leftStick;
	private Joystick rightStick;
	private Joystick xbox;
	
	private SpeedController elevator = new WPI_TalonSRX(7);
	
	SpeedController left = new MultiSpeedController(new WPI_TalonSRX(0), new WPI_TalonSRX(1), new WPI_TalonSRX(2));
	SpeedController right = new MultiSpeedController(new WPI_TalonSRX(3),new WPI_TalonSRX(4), new WPI_TalonSRX(5));
	
	private Encoder rightEncoder = new Encoder(0, 1);
	private Encoder leftEncoder = new Encoder(2, 3);
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	//Climber
	//Intake (2) (L Bumper Intake, R Bumper Outtake)
	
	String gameData;
	
	@Override
	public void robotInit() {
		
		driveTrain = new DifferentialDrive(left, right);
		
		//Distance per revolution/256 pulse per revolution
		rightEncoder.setDistancePerPulse(256);
		
		//Gets randomized colors and removes last letter because not needed for auton ("LL", "LR")
		gameData = removeCharAt(DriverStation.getInstance().getGameSpecificMessage(), 3);
		
		super.sensors.put("rightEncoder", rightEncoder);
		super.sensors.put("leftEncoder", leftEncoder);
		super.sensors.put("gyro", gyro);
		
		rightEncoder.reset();
		leftEncoder.reset();
		gyro.calibrate();
		gyro.reset();
	}

	@Override
	public void autonomous() {
		
		//Gets string from SmartDashboard
		String robotLocation = SmartDashboard.getString("DB/String 0", "default"); //Input is "Center", "Right", or "Left"
		String priority = SmartDashboard.getString("DB/String 1", "default"); //Input is "Switch" or "Scale"
		
		ChooseAuton c = new ChooseAuton(this);
		c.chooseAuton(robotLocation + priority + gameData); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
	}

	@Override
	public void teleOpInit() {
		
		leftStick = new Joystick(1);
		rightStick = new Joystick(0);
		xbox = new Joystick(2);
	}
	
	@Override
	public void teleOpPeriodic() {
		
		driveTrain.tankDrive(leftStick.getY(), rightStick.getY());
		if (xbox.getRawButton(4)) {
			elevator.set(1.0);
		} else if (xbox.getRawButton(1)) {
			elevator.set(-1.0);
		} else {
			elevator.set(0);
		}
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
	
	public static String removeCharAt(String s, int pos) {
		
		return s.substring(0, pos - 1) + s.substring(pos);
	}
}
