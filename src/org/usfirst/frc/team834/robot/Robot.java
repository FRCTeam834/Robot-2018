package org.usfirst.frc.team834.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import visualrobot.*;


public class Robot extends VisualRobot {

	private DifferentialDrive driveTrain;
	private Joystick leftStick;
	private Joystick rightStick;
	
	/*
	 * 0-2 Right Drive
	 * 3-5 Left Drive
	 */
	TalonSRX[] motors = new TalonSRX[5];
	
	SpeedController left = new MultiSpeedController(new WPI_TalonSRX(0), new WPI_TalonSRX(1), new WPI_TalonSRX(2));
	SpeedController right = new MultiSpeedController(new WPI_TalonSRX(3),new WPI_TalonSRX(4), new WPI_TalonSRX(5));
	
	private Encoder rightEncoder = new Encoder(0, 1);
	private Encoder leftEncoder = new Encoder(2, 3);
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	String gameData;
	
	@Override
	public void robotInit() {
		
		driveTrain = new DifferentialDrive(left, right);
		
		//Gets randomized colors ("LLL", "LRL")
		gameData = removeCharAt(DriverStation.getInstance().getGameSpecificMessage(),3);
		
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
		
		//Gets string from Smart Dashboard
		String robotLocation = SmartDashboard.getString("DB/String 0", "default"); //Input is "Center", "Right", or "Left"
		String priority = SmartDashboard.getString("DB/String 1", "default"); //Input is "Switch" or "Scale"
		
		ChooseAuton c = new ChooseAuton(this);
		c.chooseAuton(robotLocation + priority + gameData); //Chooses auton based on location of robot, what priority for that round is, and which side the colors on
	}

	@Override
	public void teleOpInit() {
		
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
	}
	
	@Override
	public void teleOpPeriodic() {
		
		driveTrain.tankDrive(leftStick.getY(), rightStick.getY());
	}
	
	@Override
	public void setLeftSide(double speed) {
		
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		left.set(speed);
	}

	@Override
	public void setRightSide(double speed) {
		
		speed = speed > 1 ? 1 : speed;
		speed = speed < -1 ? -1 : speed;
		
		right.set(speed);
	}
	
	public static String removeCharAt(String s, int pos) {
		
		return s.substring(0, pos) + s.substring(pos + 1);
	}
}
