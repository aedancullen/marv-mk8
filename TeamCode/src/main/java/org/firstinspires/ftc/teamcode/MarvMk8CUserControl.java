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

        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        else{
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        double horiz;
        if (gamepad1.left_trigger > 0) {
            horiz = -gamepad1.left_trigger/2;
        }
        else {
            horiz = gamepad1.right_trigger/2;
        }

        marv.drive(-gamepad1.left_stick_y/2, -gamepad1.right_stick_y/2, horiz);


        if (gamepad2.left_bumper) {
            marv.convey(-1);
        }
        else if (gamepad2.right_bumper) {
            marv.convey(1);
        }
        else {
            marv.convey(0);
        }


        if (gamepad2.dpad_down) {
            marv.collect(1);
        }
        else if (gamepad2.dpad_left) {
            marv.counterL(1);
        }
        else if (gamepad2.dpad_right) {
            marv.counterR(1);
        }
        else if (gamepad2.dpad_up) {
            marv.collect(-1);
        }
        else {
            marv.collect(0);
        }
        
        
        if (gamepad2.a) {
            marv.setWinchLevel(0);
        }
        else if (gamepad2.b) {
            marv.setWinchLevel(1);
        }
        else if (gamepad2.x) {
            marv.setWinchLevel(2);
        }
        else if (gamepad2.y) {
            marv.setWinchLevel(3);
        }
        
        marv.winchTick();
        

    }




}
