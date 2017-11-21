package org.firstinspires.ftc.teamcode;

import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.evolutionftc.visionface.VisionProcessor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 5/27/17.
 */

@Autonomous(name="VisionTest - Supercrypto")
public class VisionTestSupercrypto extends OpMode {

    VisionProcessor processor;

    public void init() {
        CameraSpec zteSpeedCameraLandscape = new CameraSpec(0.9799, Math.PI / 2);
        processor = new VisionProcessor(hardwareMap.appContext, zteSpeedCameraLandscape);
    }

    public void start() {
        processor.start();
        ObjectSpec beacon = new ObjectSpec(hardwareMap.appContext, "haarcascade_evolution_supercrypto_2k3k_20st", 6.1);
        processor.loadObject(beacon);
    }

    public void loop() {
        //Nothing
    }

    public void stop() {
        processor.stop();
    }
}
