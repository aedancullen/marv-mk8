package org.firstinspires.ftc.teamcode;


import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.evolutionftc.visionface.VisionProcessor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@TeleOp(name="AutocryptoTest")
public class AutocryptoTest extends OpMode {

    VisionProcessor processor;

    boolean isInAuto = false;

    int rollWindowLength = 5; // frames
    // Roll window 2D array - lower indices are older frames
    double[][] rollWindow = new double[rollWindowLength][3]; // length * x,y,z

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
        // bump roll window
        for (int i=0; i<rollWindowLength - 1; i++) {
            rollWindow[i] = rollWindow[i+1];
        }
        // update roll window x,y,z last frame
        rollWindow[rollWindowLength - 1] = processor.getObjectPosition();

        // averaging
        double[] rollPos = new double[3];
        for (int i=0; i<rollWindowLength; i++) {
            rollPos[0] += rollWindow[i][0];
            rollPos[1] += rollWindow[i][1];
            rollPos[2] += rollWindow[i][2];
        }
        for (int i=0; i<3; i++){
            rollPos[i] /= rollWindowLength;
        }


        if (gamepad1.back && !isInAuto) {
            // Do auto start routine
            isInAuto = true;
        }
        if (!gamepad1.back && isInAuto) {
            // Do auto end routine
            isInAuto = false;
        }

        if (isInAuto) {
            // do nav stuff
            if (/*done driving*/ false) {
                isInAuto = false;
            }
        }
    }

    public void stop() {
        processor.stop();
    }

}
