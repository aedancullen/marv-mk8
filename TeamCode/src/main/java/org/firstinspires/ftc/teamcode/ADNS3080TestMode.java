package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.IOException;


//@Autonomous(name="ADNS3080TestMode")
public class ADNS3080TestMode extends LinearOpMode {

    ADNS3080 adns3080;

    public void runOpMode() {
        try {
            adns3080 = new ADNS3080(hardwareMap);
        }
        catch (IOException e) {
            telemetry.addData("Darn","It didn't work");
            telemetry.update();
            while (true) {}
        }

        telemetry.addData("YAAAAY","It's boppin'!");
        telemetry.update();


        waitForStart();

        while (opModeIsActive()) {

            adns3080.updateSensor();
            telemetry.addData("", "dx:" + adns3080.getDx() + " dy:" + adns3080.getDy() + " squal:" + adns3080.getSqual());

        }

    }

}
