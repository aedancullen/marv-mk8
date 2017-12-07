package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CCommon {

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;
    AnalogInput sonarL;
    AnalogInput sonarR;
    AnalogInput sonarB;

    DcMotor collectorL;
    DcMotor collectorR;

    DcMotor winch;

    CRServo conveyorA;
    CRServo conveyorB;

    DigitalChannel endstop;

    boolean angleHoldIsEnabled;
    double angleHoldAngle;
    
    double winchZeroPosition;
    
    int winchMaxPosition = 400; /*set correctly*/
    
    int winchLevel=0;
    int winchTolerance = 10; /*set reasonably*/
    int winchUpl = 100; /*set correctly*/


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
        collectorR = hardwareMap.dcMotor.get("collectorR");

        conveyorA = hardwareMap.crservo.get("conveyorA");
        conveyorB = hardwareMap.crservo.get("conveyorB");
        
        winch = hardwareMap.dcMotor.get("winch");
        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        sonarL = hardwareMap.analogInput.get("sonarL");
        sonarR = hardwareMap.analogInput.get("sonarR");
        sonarB = hardwareMap.analogInput.get("sonarB");

        endstop = hardwareMap.digitalChannel.get("endstop");
        endstop.setMode(DigitalChannel.Mode.INPUT);

    }
    
    public void homeWinchTick() {
        if (endstop.getState()) { // the rev endstop is not intuitive
            winch.setPower(-0.5);
        }
        else {
            winch.setPower(0);
            winchZeroPosition  = winch.getCurrentPosition();
        }
    }
    
    public void winchToHeightTick(int height){
        int targetPosition = winchUpl * height;
        if (Math.abs(winch.getCurrentPosition()-winchZeroPosition - targetPosition) < winchTolerance) {
            winch.setPower(0);
        }
        else if (winch.getCurrentPosition()-winchZeroPosition - targetPosition > 0) {
            winch.setPower(-0.5);
        }
        else if (winch.getCurrentPosition()-winchZeroPosition - targetPosition < 0) {
            winch.setPower(1);
        }
    }
    
    public void winchTick() {
        if (winch.getCurrentPosition()-winchZeroPosition > winchMaxPosition) {
            // Bailout for mechanical safety (protect the lift mechanism from damage)
            winch.setPower(-0.5); // unique behavior for diagnostic detection
            return;
        }
        
        
        if (winchLevel == 0) {
            homeWinchTick();
        }
        else {
            winchToHeightTick(winchLevel);
        }
    }
    
    public void setWinchLevel(int level) {
        this.winchLevel = level;
    }
    
    public void setAngleHold(double angleRads){
        angleHoldIsEnabled = true;
        angleHoldAngle = angleRads; 
    }

    public void disableAngleHold() {
        angleHoldIsEnabled = false;
    }

    public void drive(
            double vertL,
            double vertR,
            double horiz) {

        double rot = 0;

        if (angleHoldIsEnabled) {
            // modify rot stuffs
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
        collectorL.setPower(speed);
        collectorR.setPower(-speed);
    }

    public void counterR(double speed) {
        collectorL.setPower(-speed);
        collectorR.setPower(-speed);
    }

    public void counterL(double speed) {
        collectorL.setPower(speed);
        collectorR.setPower(speed);
    }


}
