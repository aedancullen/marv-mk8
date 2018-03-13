package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="DropSensors Test")
public class NewDropSensorsFun extends OpMode {

    double servoPos = 0;

    Servo kickski;
    ColorSensor kickskiColor;
    ColorSensor dropskiColor;

    AnalogInput scottySensor;

    public void init() {
        kickski = hardwareMap.servo.get("kickski");
        kickskiColor = hardwareMap.get(ColorSensor.class, "kickskiColor");
        dropskiColor = hardwareMap.colorSensor.get("dropskiColor");
        scottySensor = hardwareMap.analogInput.get("scottySensor");
    }

    public void loop() {
        telemetry.addData("kickski red", kickskiColor.red());
        telemetry.addData("kickski blue", kickskiColor.blue());
        telemetry.addData("dropski red", dropskiColor.red());
        telemetry.addData("dropski blue", dropskiColor.blue());
        telemetry.addData("kickski position", servoPos);

        telemetry.addData("scottysensor:", scottySensor.getVoltage());
        telemetry.update();

        kickski.setPosition(servoPos);

        if (gamepad1.right_bumper) {
            servoPos += 0.01;
        }
        else if (gamepad1.left_bumper) {
            servoPos -= 0.01;
        }

    }
}
