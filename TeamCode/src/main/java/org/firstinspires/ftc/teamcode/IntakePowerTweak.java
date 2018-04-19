package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Bob on 4/7/2018.
 */

@TeleOp(name="IntakePowerTweak")
public class IntakePowerTweak extends OpMode {

    double colspeed = 0.20;
    double diff = 0;

    MarvMk8CCommon marv;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
    }

    public void loop() {
        telemetry.addData("current speed", colspeed);
        telemetry.addData("difference", diff);
        telemetry.update();

        if (gamepad1.dpad_down) {
            marv.collectorR.setPower(colspeed + diff / 2.0);
            marv.collectorL.setPower(-colspeed + diff / 2.0);
        }
        else if (gamepad1.dpad_up) {
            marv.collect(-colspeed);
        }
        else if (gamepad1.dpad_right) {
            marv.counterR(colspeed);
        }
        else if (gamepad1.dpad_left) {
            marv.counterL(colspeed);
        }
        else if (gamepad1.left_bumper) {
            marv.collectorL.setPower(-1);
            marv.collectorR.setPower(-1);
        }
        else if (gamepad1.right_bumper) {
            marv.collectorR.setPower(1);
            marv.collectorL.setPower(1);
        }
        else {
            marv.collect(0);
        }
        if (gamepad1.a) {
            colspeed += 0.01;
        }
        else if (gamepad1.b) {
            colspeed -= 0.01;
        }
        if (gamepad1.x) {
            diff += 0.005;
        }
        else if (gamepad1.y) {
            diff -= 0.005;
        }
    }


}
