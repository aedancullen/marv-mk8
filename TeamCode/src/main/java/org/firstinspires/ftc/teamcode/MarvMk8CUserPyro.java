package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by aedan on 12/2/17.
 */

@TeleOp(name="Marv-Mk8C User PYRO")
public class MarvMk8CUserPyro  extends OpMode {

    MarvMk8CCommon marv;

    double collectspeed = 0.5;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
    }

    public void wheelDriveTick() {

        double horiz;
        /*if (gamepad1.left_trigger > 0) {
            horiz = -gamepad1.left_trigger/2;
        }
        else {
            horiz = gamepad1.right_trigger/2;
        }*/
        horiz = (gamepad1.right_trigger / 2) - (gamepad1.left_trigger / 2);

        marv.drive(-gamepad1.left_stick_y / 1.25, -gamepad1.right_stick_y / 1.25, horiz);
    }

    public void loop() {
        telemetry.update();
        wheelDriveTick();

        if (gamepad1.right_bumper) {
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        else{
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }


        if (gamepad2.left_bumper) {
            marv.convey(-1);
        }
        else if (gamepad2.right_bumper) {
            marv.convey(1);
        }
        else {
            marv.convey(0);
        }

        marv.setGatesPosition(gamepad2.left_trigger);

        marv.setFlippoPos(gamepad2.right_trigger);


        if (gamepad2.dpad_down) {
            marv.collect(0);
        }
        else if (gamepad2.dpad_left) {
            marv.counterL(collectspeed);
        }
        else if (gamepad2.dpad_right) {
            marv.counterR(collectspeed);
        }
        else if (gamepad2.dpad_up) {
            marv.collect(-collectspeed);
        }
        else {
            marv.autoCollectTick(collectspeed);
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
