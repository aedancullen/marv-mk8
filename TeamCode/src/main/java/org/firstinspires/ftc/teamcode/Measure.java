package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@TeleOp(name="Measure")
public class Measure extends OpMode {

    DcMotor winch;

    double VperMM = 0.000644531; // HRLV series with 3.3V supply

    AnalogInput mbR;
    AnalogInput mbL;
    AnalogInput mbB;

    DigitalChannel endstop;

    Servo dropski;

    ColorSensor dropskiColor;

    double dropskiPos = 0;

    DcMotor fr;

    BNO055IMU imu;


    public void init() {

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;

        imu.initialize(parameters);

        fr = hardwareMap.dcMotor.get("fr");
        winch = hardwareMap.dcMotor.get("winch");
        mbR = hardwareMap.analogInput.get("sonarR");
        mbB = hardwareMap.analogInput.get("sonarB");
        mbL = hardwareMap.analogInput.get("sonarL");

        dropski = hardwareMap.servo.get("dropski");
        dropskiColor = hardwareMap.colorSensor.get("dropskiColor");

        endstop = hardwareMap.digitalChannel.get("endstop");
        endstop.setMode(DigitalChannel.Mode.INPUT);
    }

    double inchesPerVolt = 73.123;

    private double voltageToInches(double voltage){
        return voltage * inchesPerVolt;
    }

    public void loop() {

        if (gamepad1.a) {
            dropskiPos += 0.05;
        }
        else if (gamepad1.b) {
            dropskiPos -= 0.05;
        }


        dropski.setPosition(dropskiPos);

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        telemetry.addData("angle", angles.firstAngle);

        telemetry.addData("r", dropskiColor.red());
        telemetry.addData("b", dropskiColor.blue());
        telemetry.addData("dropskiPos", dropskiPos);

        telemetry.addData("enc fr", fr.getCurrentPosition());

        telemetry.addData("endstop", endstop.getState());
        telemetry.addData("winch", winch.getCurrentPosition());

        double distMbR = voltageToInches(mbR.getVoltage());
        double distMbL = voltageToInches(mbL.getVoltage());
        double distMbB = voltageToInches(mbB.getVoltage());

        telemetry.addData("distMbR", distMbR);
        telemetry.addData("distMbL", distMbL);
        telemetry.addData("distMbB", distMbB);

        telemetry.addData("voltMbR", mbR.getMaxVoltage());
        telemetry.addData("voltMbL", mbL.getVoltage());
        telemetry.addData("voltMbB", mbB.getVoltage());

        telemetry.update();
    }

}
