package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by aedan on 12/2/17.
 */

@TeleOp(name="Marv-Mk8C User Control")
public class MarvMk8CUserControl  extends OpMode {

    MarvMk8CCommon marv;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
    }

    public void loop() {
        double horiz;
        if (gamepad1.left_trigger > 0) {
            horiz = -gamepad1.left_trigger/4;
        }
        else {
            horiz = gamepad1.right_trigger/4;
        }

        marv.drive(gamepad1.left_stick_y/3, gamepad1.right_stick_y/3, horiz, 0);



    }




}
