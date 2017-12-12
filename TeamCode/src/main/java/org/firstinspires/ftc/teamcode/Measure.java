package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;


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


    public void init() {


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

    public double compRevVoltage(double voltage, double max){
        double half = max / 2;
        double distanceFromHalf = half - voltage;
        double compAmount = distanceFromHalf / half;
        double compVolts = 0.04 * compAmount;

        return voltage + compVolts;
    }

    public void loop() {

        if (gamepad1.a) {
            dropskiPos += 0.05;
        }
        else if (gamepad1.b) {
            dropskiPos -= 0.05;
        }

        dropski.setPosition(dropskiPos);

        telemetry.addData("r", dropskiColor.red());
        telemetry.addData("b", dropskiColor.blue());
        telemetry.addData("dropskiPos", dropskiPos);

        telemetry.addData("enc fr", fr.getCurrentPosition());

        telemetry.addData("endstop", endstop.getState());
        telemetry.addData("winch", winch.getCurrentPosition());

        double distMbR = (compRevVoltage(mbR.getVoltage(), 3.3) / VperMM + 0) / 10.0; // to cm
        double distMbL = (compRevVoltage(mbL.getVoltage(), 3.3) / VperMM + 0) / 10.0; // to cm
        double distMbB = (compRevVoltage(mbB.getVoltage(), 3.3) / VperMM + 0) / 10.0; // to cm

        telemetry.addData("distMbR", distMbR);
        telemetry.addData("distMbL", distMbL);
        telemetry.addData("distMbB", distMbB);

        telemetry.addData("voltMbR", mbR.getMaxVoltage());
        telemetry.addData("voltMbL", mbL.getVoltage());
        telemetry.addData("voltMbB", mbB.getVoltage());

        telemetry.update();
    }

}
