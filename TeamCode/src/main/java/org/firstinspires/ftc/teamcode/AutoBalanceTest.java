package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="AutoBalanceTest")
public class AutoBalanceTest extends OpMode {

    MarvMk8CCommon marv;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void loop() {
        Orientation angles = marv.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("y", angles.secondAngle);
        telemetry.addData("x", angles.thirdAngle);

        double powerY=0;
        double powerX=0;

       // marv.drive();
    }

}
