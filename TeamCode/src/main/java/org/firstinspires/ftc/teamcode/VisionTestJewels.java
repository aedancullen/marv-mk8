package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.evolutionftc.visionface.VisionProcessor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FILLED;
import static org.opencv.core.Core.mean;

@Autonomous(name="Jewel Finder")
public class VisionTestJewels extends OpMode {

    JewelOverlayVisionProcessor processor;

    public void init() {
        CameraSpec zteSpeedCameraLandscape = new CameraSpec(0.9799, Math.PI / 2); // nobody cares about positioning stuff
        processor = new JewelOverlayVisionProcessor(hardwareMap.appContext, zteSpeedCameraLandscape);
    }

    public void start() {
        processor.start();
        ObjectSpec joules = new ObjectSpec(hardwareMap.appContext, "haarcascade_evolution_jewels_2k3k_20st", 6.1); // again - nobody cares
        processor.loadObject(joules);
    }

    public void loop() {
        if (processor.getIsConfident()) {
            telemetry.addData("Detected currently", "YES");
        }
        else {
            telemetry.addData("Detected currently", "NO");
        }
        if (processor.getIsRedBlueInThatOrder()) {
            telemetry.addData("Most recently detected order", "RED - BLUE");
        }
        else {
            telemetry.addData("Most recently detected order", "BLUE - RED");
        }
    }

    public void stop() {
        processor.stop();
    }
}
