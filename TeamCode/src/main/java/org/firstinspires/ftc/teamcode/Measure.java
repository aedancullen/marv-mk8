package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name="Measure")
public class Measure extends OpMode {

    DcMotor winch;

    double VperMM = 0.000644531;

    AnalogInput mbR;
    AnalogInput mbL;
    AnalogInput mbB;

    DigitalChannel endstop;

    public void init() {
        winch = hardwareMap.dcMotor.get("winch");
        mbR = hardwareMap.analogInput.get("sonarR");
        mbB = hardwareMap.analogInput.get("sonarB");
        mbL = hardwareMap.analogInput.get("sonarL");

        endstop = hardwareMap.digitalChannel.get("endstop");
        endstop.setMode(DigitalChannel.Mode.INPUT);
    }

    public void loop() {
        telemetry.addData("endstop", endstop.getState());
        telemetry.addData("winch", winch.getCurrentPosition());

        double distMbR = (mbR.getVoltage() / VperMM + 0) / 10.0; // to cm
        double distMbL = (mbL.getVoltage() / VperMM + 0) / 10.0; // to cm
        double distMbB = (mbB.getVoltage() / VperMM + 0) / 10.0; // to cm

        telemetry.addData("distMbR", distMbR);
        telemetry.addData("distMbL", distMbL);
        telemetry.addData("distMbB", distMbB);

        telemetry.update();
    }

}
