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


    public MarvMk8CCommon(HardwareMap hardwareMap){
        fl = hardwareMap.dcMotor.get("fl");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hardwareMap.dcMotor.get("fr");
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hardwareMap.dcMotor.get("bl");
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hardwareMap.dcMotor.get("br");
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        collectorL = hardwareMap.dcMotor.get("collectorL");
        collectorR = hardwareMap.dcMotor.get("collectorR");

        conveyorA = hardwareMap.crservo.get("conveyorA");
        conveyorB = hardwareMap.crservo.get("conveyorB");

    }
    
    public void homeWinchTick() {
        if (!endstop.read()) {
            winch.setPower(-0.5);
        }
        else {
            winch.setPower(0);
            winchZeroPosition  = winch.getPosition();
        }
    }
    
    public void winchToHeightTick(int height, int tolerance){
        int targetPosition = winchZeroPosition + 1/*units per level*/ * height;
        if (Math.abs(winch.getPosition() - targetPosition) < tolerance) {
            winch.setPower(0);
        }
        else if (winch.getPosition() - targetPosition > 0) {
            winch.setPower(-1);
        }
        else if (winch.getPosition() - targetPosition < 0) {
            winch.setPower(1);
        }
    }
    
    public void setAngleHold(double angleRads){}

    public void disableAngleHold() {}

    public void drive(
            double vertL,
            double vertR,
            double horiz) {

        double rot = 0;

        if (angleHoldIsEnabled) {
            // modify rot stuffs
        }

        double flp = vertL + rot - horiz;
        fl.setPower(Math.max(Math.min(flp, 1), -1));
        double frp = vertR - rot + horiz;
        fr.setPower(Math.max(Math.min(frp, 1), -1));
        double blp = vertL + rot + horiz;
        bl.setPower(Math.max(Math.min(blp, 1), -1));
        double brp = vertR - rot - horiz;
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
