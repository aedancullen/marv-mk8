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

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CCommon {

    public boolean isOnRedSide; // used by AutopilotSystemCommon
    public boolean isOnBSide;

    public boolean isDoingExtras = true;

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    AnalogInput sonarL;
    AnalogInput sonarR;
    AnalogInput sonarB;

    AnalogInput scottySensor;

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
    DigitalChannel endstop2;

    Servo dropski;
    ColorSensor dropskiColor;

    Servo kickski;
    ColorSensor kickskiColor;

    Servo gateL;
    Servo gateR;

    Servo relicLift;
    Servo relicGrab;
    DcMotor relicSlide;

    int relicGrabPos;
    int relicLiftPos;

    boolean angleHoldIsEnabled;
    double angleHoldAngle;

    double angleHoldPowerCap = 0.70;
    
    double winchZeroPosition;
    
    int winchMaxPosition = 3189; /*set correctly*/
    int relicSlideMaxPosition = 1136; /*set correctly*/
    int relicSlideMinPosition = 200;
    int relicSlideSlowPosition = 700;
    
    int winchLevel=0;
    int winchTolerance = 100; /*set reasonably*/
    int winchUpl = winchMaxPosition / 3;


    double flippoMax = 0.07; // set properly

    final private boolean MOTORCONTROL_RAW = true;

    BNO055IMU imu;

    DcMotor.ZeroPowerBehavior lastZeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;


    public MarvMk8CCommon(HardwareMap hardwareMap){
        fl = hardwareMap.dcMotor.get("fl");
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hardwareMap.dcMotor.get("fr");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hardwareMap.dcMotor.get("bl");
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hardwareMap.dcMotor.get("br");
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        collectorL = hardwareMap.dcMotor.get("collectorL");
        collectorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        collectorR = hardwareMap.dcMotor.get("collectorR");
        collectorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        conveyorA = hardwareMap.crservo.get("conveyorA");
        conveyorB = hardwareMap.crservo.get("conveyorB");

        flippoA = hardwareMap.servo.get("flippoA");
        flippoB = hardwareMap.servo.get("flippoB");

        relicSlide = hardwareMap.dcMotor.get("relicSlide");
        relicSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //relicSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        relicSlide.setMode(STOP_AND_RESET_ENCODER);
        relicSlide.setMode(RUN_WITHOUT_ENCODER);
        relicLift = hardwareMap.servo.get("relicLift");
        relicGrab = hardwareMap.servo.get("relicGrab");
        
        winch = hardwareMap.dcMotor.get("winch");
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        winch.setDirection(DcMotorSimple.Direction.REVERSE);
        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        sonarL = hardwareMap.analogInput.get("sonarL");
        sonarR = hardwareMap.analogInput.get("sonarR");
        sonarB = hardwareMap.analogInput.get("sonarB");

        scottySensor = hardwareMap.analogInput.get("scottySensor");

        sonarRx = hardwareMap.digitalChannel.get("sonarRx");
        sonarRx.setMode(DigitalChannel.Mode.OUTPUT);

        endstop = hardwareMap.digitalChannel.get("endstop");
        endstop.setMode(DigitalChannel.Mode.INPUT);
        endstop2 = hardwareMap.digitalChannel.get("endstop2");
        endstop2.setMode(DigitalChannel.Mode.INPUT);
        endstopTop = hardwareMap.digitalChannel.get("endstopTop");
        endstopTop.setMode(DigitalChannel.Mode.INPUT);

        dropski = hardwareMap.servo.get("dropski");
        dropskiColor = hardwareMap.colorSensor.get("dropskiColor");

        kickski = hardwareMap.servo.get("kickski");
        kickskiColor = hardwareMap.colorSensor.get("kickskiColor");

        gateL = hardwareMap.servo.get("gateL");
        gateR = hardwareMap.servo.get("gateR");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;

        imu.initialize(parameters);

        setDropskiUp();

        if (!MOTORCONTROL_RAW) {
            setEncoderBehavior(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        setConveyGateClosed();
        setLiftPos(0);
        //setGrabPos(1);

    }

    public double readScotty() {
        return scottySensor.getVoltage();
    }

    public void setSlideSpeed(double speed, boolean overrideIsEnabled) {
        if (speed > 0) {
            if (relicSlide.getCurrentPosition() < relicSlideMaxPosition || overrideIsEnabled) {
                if (relicSlide.getCurrentPosition() < relicSlideSlowPosition) {
                    relicSlide.setPower(speed);
                }
                else {
                    relicSlide.setPower(speed * 0.6);
                }
            }
            else {
                relicSlide.setPower(0);
            }
        }
        else if (speed < 0) {
            if (relicSlide.getCurrentPosition() > relicSlideMinPosition || overrideIsEnabled) {
                relicSlide.setPower(speed);
            }
            else {
                relicSlide.setPower(0);
            }
        }
        else {
            relicSlide.setPower(0);
        }
    }

    public void setGrabPos(double pos) {
        relicGrab.setPosition(pos);
    }

    public void setLiftPos(double pos) {
        relicLift.setPosition(pos);
    }

    public void setGatesPosition(double pos) {
        gateL.setPosition(1 - (pos * 0.6));
        gateR.setPosition(pos * 0.6);
    }

    public void setConveyGateOpen() {
        gateL.setPosition(0.5);
    }

    public void setConveyGateClosed() {
        gateL.setPosition(0);
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

    public void setDropskiMid() {
        dropski.setPosition((0.75+0.18) / 3);
    }

    public void setDropskiSafe() {
        dropski.setPosition((0.75+0.18) / 2);
    }

    public void setDropskiDown() {
        dropski.setPosition(0.18);
    }

    public void setKickskiCenter() {
        kickski.setPosition(0.68);
    }

    public void setKickskiCCW() {
        kickski.setPosition(0.68+0.32);
    }

    public void setKickskiCW() {
        kickski.setPosition(0.68-0.32);
    }

    public void setFlippoPos(double pos) {
        flippoA.setPosition(0.5+ (flippoMax * pos));
        flippoB.setPosition(0.5+ (-flippoMax * pos));
    }

    public boolean flippoIsRaised() {
        return flippoA.getPosition() != 0.5 && flippoB.getPosition() != 0.5;
    }

    public boolean dropskiIsRed() {
        return (kickskiColor.red() > kickskiColor.blue());
    }

    public boolean dropskiIsConfident() {
        return (kickskiColor.red() != 0 || kickskiColor.blue() != 0);
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

    public boolean liftIsRaised() {
        return endstop.getState() && endstop2.getState();
    }
    
    public void homeWinchTick() {
        if (liftIsRaised()) { // if either is triggered, then stop
            winch.setPower(-0.75);
        }
        else {
            winch.setPower(0);
            winchZeroPosition  = winch.getCurrentPosition();
        }
    }

    public void autoConveyTick(double power) {
        if (!liftIsRaised() && !flippoIsRaised()) {
            convey(power);
        }
        else {
            convey(0);
        }
    }

    public void autoCollectTick(double power) {
        if (!liftIsRaised() && !flippoIsRaised()) {
            collect(power);
        }
        else {
            collect(0);
        }
    }

    public void maxWinchTick() {
        if (endstopTop.getState()) { // the rev endstop is not intuitive
            winch.setPower(0.75);
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
            winch.setPower(-0.75);
        }
        else if (winch.getCurrentPosition()-winchZeroPosition - targetPosition < 0) {
            winch.setPower(1.00);
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
            if (Math.abs(degreesError) > 0) {
                rot += 0.03 * degreesError;
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

    public void drivehp(
                        double vertL,
                        double vertR,
                        double horiz) {

        double rot = 0;

        if (angleHoldIsEnabled) {
            // it always comes in handy, dat EssentialHeading
            // much less annoying than the ftc_app "Orientation"
            EssentialHeading heading = EssentialHeading.fromInvertedOrientation(imuGetOrientation());
            double degreesError = new EssentialHeading(angleHoldAngle).subtract(heading).getAngleDegrees();
            if (Math.abs(degreesError) > 0) {
                rot += 0.05 * degreesError;
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

    public void setDriveTargetPositions(double vert, double horiz) {
        double flp = vert + horiz;
        double frp = vert - horiz;
        double blp = vert - horiz;
        double brp = vert + horiz;

        fl.setTargetPosition((int) flp);
        fr.setTargetPosition((int) frp);
        bl.setTargetPosition((int) blp);
        br.setTargetPosition((int) brp);
    }

    public void setDriveTargetPositionsWithRot(double vert, double horiz, double rot) {
        double flp = vert + rot + horiz;
        double frp = vert - rot - horiz;
        double blp = vert + rot - horiz;
        double brp = vert - rot + horiz;

        fl.setTargetPosition((int) flp);
        fr.setTargetPosition((int) frp);
        bl.setTargetPosition((int) blp);
        br.setTargetPosition((int) brp);
    }

    public void setDriveTargetPowers(double power) {
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);
    }

    public boolean encodersAreBusy() {

        boolean diag1busy = fl.isBusy() || br.isBusy();
        boolean diag2busy = br.isBusy() || bl.isBusy();
        boolean straight1busy = fl.isBusy() || fr.isBusy();
        boolean straight2busy = bl.isBusy() || br.isBusy();

        return /*diag1busy && diag2busy &&*/ straight1busy && straight2busy;
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
