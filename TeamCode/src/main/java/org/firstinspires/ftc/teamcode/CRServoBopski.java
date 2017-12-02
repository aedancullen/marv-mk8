package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by aedan on 10/21/17.
 */

//@TeleOp(name="CRServoBopski")
public class CRServoBopski extends OpMode {

    CRServo bopski1;
    CRServo bopski2;
    public void init() {
        bopski1 = hardwareMap.crservo.get("bopski1");
        bopski2 = hardwareMap.crservo.get("bopski2");
    }

    public void loop() {
        bopski1.setPower(gamepad1.left_stick_y);
        bopski2.setPower(-gamepad1.left_stick_y);
    }
}
