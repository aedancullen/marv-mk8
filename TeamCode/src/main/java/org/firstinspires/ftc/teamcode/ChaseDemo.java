package org.firstinspires.ftc.teamcode;


import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.evolutionftc.visionface.VisionProcessor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.opencv.core.Rect;

//@Autonomous(name="VisionChaseDemo")
public class ChaseDemo extends OpMode {

    VisionProcessor processor;

    DcMotor left;
    DcMotor right;

    public void init() {
        CameraSpec zteSpeedCameraLandscape = new CameraSpec(0.9799, Math.PI / 2);
        processor = new VisionProcessor(hardwareMap.appContext, zteSpeedCameraLandscape);
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void start() {
        processor.start();
        ObjectSpec beacon = new ObjectSpec(hardwareMap.appContext, "haarcascade_evolution_beacon_2k2k_20st", 6.1);
        processor.loadObject(beacon);
    }

    public void loop() {
        double pLeft = 0;
        double pRight = 0;

        Rect[] locations = processor.getRawDetections();
        if (locations != null && locations.length > 0) {
            double x = (locations[0].tl().x + locations[0].br().x) / 2.0;
            double width = (locations[0].br().x - locations[0].tl().x);
            if (width < processor.capWidth / 4.0) {
                pLeft = 0.2;
                pRight = 0.2;
            }
            if (Math.abs(x - processor.capWidth / 2.0) > 50) {
                if (x - processor.capWidth / 2.0 > 0) {
                    pLeft += 0.075;
                    pRight -= 0.075;
                }
                else {
                    pLeft -= 0.075;
                    pRight += 0.075;
                }
            }
        }

        telemetry.addData("pLeft", pLeft);
        telemetry.addData("pRight", pRight);
        telemetry.update();
        left.setPower(pLeft);
        right.setPower(pRight);
    }

    public void stop() {
        processor.stop();
    }
}
