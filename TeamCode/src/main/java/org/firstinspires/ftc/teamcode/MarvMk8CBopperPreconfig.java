package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 4/19/18.
 */

@Autonomous(name="Preconfig Fourths")
public class MarvMk8CBopperPreconfig extends LinearOpMode {

    PreconfigStorage storage;

    public void runOpMode() {
        storage = new PreconfigStorage(hardwareMap.appContext);

        readPositionAtIndex("p1");
        readPositionAtIndex("p2");
        readPositionAtIndex("p3");
        readPositionAtIndex("p4");
        telemetry.update();

        waitForStart();

        setPositionAtIndex("p1");
        setPositionAtIndex("p2");
        setPositionAtIndex("p3");
        setPositionAtIndex("p4");
    }

    public void readPositionAtIndex(String name) {
        telemetry.addData(name + "x", storage.readInt(name + "x") * (4.0/24.0));
        telemetry.addData(name + "y", storage.readInt(name + "y") * (4.0/24.0));
    }

    public void setPositionAtIndex(String index) {
        while (gamepad1.left_bumper && gamepad1.right_bumper) {}
        int nX = (int)(storage.readInt(index + "x") * (4.0/24.0));
        int nY = (int)(storage.readInt(index + "y") * (4.0/24.0));
        boolean aPressedOnLast = false;
        boolean bPressedOnLast = false;
        boolean xPressedOnLast = false;
        boolean yPressedOnLast = false;
        while (!(gamepad1.left_bumper && gamepad1.right_bumper) && opModeIsActive()) {
            telemetry.addData(index + "x", nX);
            telemetry.addData(index + "y", nY);
            if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                nX = 0;
                nY = 0;
            }

            if (gamepad1.a && !aPressedOnLast) {
                aPressedOnLast = true;
                nX--;
            }
            else if (!gamepad1.a) {
                aPressedOnLast = false;
            }
            if (gamepad1.b && !bPressedOnLast) {
                bPressedOnLast = true;
                nY--;
            }
            else if (!gamepad1.b) {
                bPressedOnLast = false;
            }

            // ------------

            if (gamepad1.x && !xPressedOnLast) {
                xPressedOnLast = true;
                nX++;
            }
            else if (!gamepad1.x) {
                xPressedOnLast = false;
            }
            if (gamepad1.y && !yPressedOnLast) {
                yPressedOnLast = true;
                nY++;
            }
            else if (!gamepad1.y) {
                yPressedOnLast = false;
            }

            telemetry.update();
        }

        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            storage.writeInt(index + "x", nX * (int) (24.0 / 4.0));
            storage.writeInt(index + "y", nY * (int) (24.0 / 4.0));
        }
    }
}
