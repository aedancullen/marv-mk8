package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.AnalogInput;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CCommon {

    public boolean isOnRedSide; // used by AutopilotSystemCommon
    public boolean isOnBSide;

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    AnalogInput sonarL;
    AnalogInput sonarR;
    AnalogInput sonarB;

    DigitalChannel sonarRx;

    DcMotor collectorL;
    DcMotor collectorR;

    DcMotor winch;

    CRServo conveyorA;
    CRServo conveyorB;

    Servo flippoA;
    Servo flippoB;

    DigitalChannel endstop;
    DigitalChannel endstopTop;

    Servo dropski;
    ColorSensor dropskiColor;

    boolean angleHoldIsEnabled;
    double angleHoldAngle;

    double angleHoldPowerCap = 0.33;
    
    double winchZeroPosition;
    
    int winchMaxPosition = 4350; /*set correctly*/
    
    int winchLevel=0;
    int winchTolerance = 100; /*set reasonably*/
    int winchUpl = winchMaxPosition / 3;


    double flippoMax = 0.07; // set properly

    final private boolean MOTORCONTROL_RAW = true;

    BNO055IMU imu;

    DcMotor.ZeroPowerBehavior lastZeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;


    public MarvMk8CCommon(HardwareMap hardwareMap){
        fl = hardwareMap.dcMotor.get("fl");
        if (!MOTORCONTROL_RAW) { fl.setDirection(DcMotorSimple.Direction.REVERSE);}
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hardwareMap.dcMotor.get("fr");
        if (MOTORCONTROL_RAW) { fr.setDirection(DcMotorSimple.Direction.REVERSE);}
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hardwareMap.dcMotor.get("bl");
        if (!MOTORCONTROL_RAW) { bl.setDirection(DcMotorSimple.Direction.REVERSE);}
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hardwareMap.dcMotor.get("br");
        if (MOTORCONTROL_RAW) { br.setDirection(DcMotorSimple.Direction.REVERSE);}
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        collectorL = hardwareMap.dcMotor.get("collectorL");
        collectorR = hardwareMap.dcMotor.get("collectorR");

        conveyorA = hardwareMap.crservo.get("conveyorA");
        conveyorB = hardwareMap.crservo.get("conveyorB");

        flippoA = hardwareMap.servo.get("flippoA");
        flippoB = hardwareMap.servo.get("flippoB");
        
        winch = hardwareMap.dcMotor.get("winch");
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        winch.setDirection(DcMotorSimple.Direction.REVERSE);
        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        sonarL = hardwareMap.analogInput.get("sonarL");
        sonarR = hardwareMap.analogInput.get("sonarR");
        sonarB = hardwareMap.analogInput.get("sonarB");

        sonarRx = hardwareMap.digitalChannel.get("sonarRx");
        sonarRx.setMode(DigitalChannel.Mode.OUTPUT);

        endstop = hardwareMap.digitalChannel.get("endstop");
        endstop.setMode(DigitalChannel.Mode.INPUT);
        endstopTop = hardwareMap.digitalChannel.get("endstopTop");
        endstopTop.setMode(DigitalChannel.Mode.INPUT);

        dropski = hardwareMap.servo.get("dropski");
        dropskiColor = hardwareMap.colorSensor.get("dropskiColor");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;

        imu.initialize(parameters);

        setDropskiUp();

        if (!MOTORCONTROL_RAW) {
            setEncoderBehavior(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public double imuGetHeadingDegs() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public Orientation imuGetOrientation() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public void setDropskiUp() {
        dropski.setPosition(0.75);
    }

    public void setDropskiDown() {
        dropski.setPosition(0.18);
    }

    public void setFlippoPos(double pos) {
        flippoA.setPosition(0.5+ (flippoMax * pos));
        flippoB.setPosition(0.5+ (-flippoMax * pos));
    }

    public boolean dropskiIsRed() {
        return (dropskiColor.red() > dropskiColor.blue());
    }

    public boolean dropskiIsConfident() {
        return (dropskiColor.red() != 0 || dropskiColor.blue() != 0);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        if (behavior != lastZeroPowerBehavior) {
            lastZeroPowerBehavior = behavior;
            fl.setZeroPowerBehavior(behavior);
            fr.setZeroPowerBehavior(behavior);
            bl.setZeroPowerBehavior(behavior);
            br.setZeroPowerBehavior(behavior);
        }
    }

    public void setEncoderBehavior(DcMotor.RunMode behavior) {
        fl.setMode(behavior);
        fr.setMode(behavior);
        bl.setMode(behavior);
        br.setMode(behavior);
    }
    
    public void homeWinchTick() {
        if (endstop.getState()) { // the rev endstop is not intuitive
            winch.setPower(-0.25);
        }
        else {
            winch.setPower(0);
            winchZeroPosition  = winch.getCurrentPosition();
        }
    }

    public void maxWinchTick() {
        if (endstopTop.getState()) { // the rev endstop is not intuitive
            winch.setPower(0.25);
        }
        else {
            winch.setPower(0);
        }
    }
    
    public void winchToHeightTick(int height){
        int targetPosition = winchUpl * height;
        if (Math.abs(winch.getCurrentPosition()-winchZeroPosition - targetPosition) < winchTolerance) {
            winch.setPower(0);
        }
        else if (winch.getCurrentPosition()-winchZeroPosition - targetPosition > 0) {
            winch.setPower(-0.25);
        }
        else if (winch.getCurrentPosition()-winchZeroPosition - targetPosition < 0) {
            winch.setPower(0.50);
        }
    }
    
    public void winchTick() {

        if (winchLevel == 0) {
            homeWinchTick();
        }
        /*else if (winchLevel == 3) {
            maxWinchTick();
        }*/
        else {
            winchToHeightTick(winchLevel);
        }
    }
    
    public void setWinchLevel(int level) {
        this.winchLevel = level;
    }
    
    public void setAngleHold(double angleDegs){
        angleHoldIsEnabled = true;
        angleHoldAngle = angleDegs;
    }

    public void setAngleHoldPowerCap(double powerCap) {
        angleHoldPowerCap = powerCap;
    }

    public void disableAngleHold() {
        angleHoldIsEnabled = false;
    }

    public boolean angleHoldHasSettled() {
        EssentialHeading heading = EssentialHeading.fromInvertedOrientation(imuGetOrientation());
        double degreesError = new EssentialHeading(angleHoldAngle).subtract(heading).getAngleDegrees();
        return (Math.abs(degreesError) <= 1);
    }

    public void drive(
            double vertL,
            double vertR,
            double horiz) {

        double rot = 0;

        if (angleHoldIsEnabled) {
            // it always comes in handy, dat EssentialHeading
            // much less annoying than the ftc_app "Orientation"
            EssentialHeading heading = EssentialHeading.fromInvertedOrientation(imuGetOrientation());
            double degreesError = new EssentialHeading(angleHoldAngle).subtract(heading).getAngleDegrees();
            if (Math.abs(degreesError) > 1) {
                rot += angleHoldPowerCap;
                rot = Math.max(Math.min(rot, angleHoldPowerCap), -angleHoldPowerCap);
            }
        }

        double flp = vertL + rot + horiz;
        fl.setPower(Math.max(Math.min(flp, 1), -1));
        double frp = vertR - rot - horiz;
        fr.setPower(Math.max(Math.min(frp, 1), -1));
        double blp = vertL + rot - horiz;
        bl.setPower(Math.max(Math.min(blp, 1), -1));
        double brp = vertR - rot + horiz;
        br.setPower(Math.max(Math.min(brp, 1), -1));


    }

    public void convey(double speed) {
        conveyorA.setPower(speed);
        conveyorB.setPower(-speed);
    }

    public void collect(double speed) {
        collectorL.setPower(-speed);
        collectorR.setPower(speed);
    }

    public void counterR(double speed) {
        collectorL.setPower(speed);
        collectorR.setPower(speed);
    }

    public void counterL(double speed) {
        collectorL.setPower(-speed);
        collectorR.setPower(-speed);
    }


}
