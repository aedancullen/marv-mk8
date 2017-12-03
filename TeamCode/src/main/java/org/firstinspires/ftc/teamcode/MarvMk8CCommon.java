package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CCommon {

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;


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
    }

    public void drive(
            double vertL,
            double vertR,
            double horiz,
            double rot) {

        double flp = vertL + rot - horiz;
        fl.setPower(Math.max(Math.min(flp, 1), -1));
        double frp = vertR - rot + horiz;
        fr.setPower(Math.max(Math.min(frp, 1), -1));
        double blp = vertL + rot + horiz;
        bl.setPower(Math.max(Math.min(blp, 1), -1));
        double brp = vertR - rot - horiz;
        br.setPower(Math.max(Math.min(brp, 1), -1));


    }


}
