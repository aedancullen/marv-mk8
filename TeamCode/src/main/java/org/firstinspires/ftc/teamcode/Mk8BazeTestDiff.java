package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by aedan on 11/2/17.
 */

@TeleOp(name="Mk8BazeTest - Differential")
public class Mk8BazeTestDiff extends OpMode {

    DcMotor fl;
    DcMotor fr;
    DcMotor bl;
    DcMotor br;

    public void init() {
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

    public void loop() {
        double horiz;
        if (gamepad1.left_trigger > 0) {
            horiz = -gamepad1.left_trigger/2;
        }
        else {
            horiz = gamepad1.right_trigger/2;
        }
        driveBaze(gamepad1.left_stick_y/2, gamepad1.right_stick_y/2, horiz, 0);



    }


    public void driveBaze(
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
