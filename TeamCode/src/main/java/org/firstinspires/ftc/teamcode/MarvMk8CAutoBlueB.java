package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Marv-Mk8C Auto Blue B")
public class MarvMk8CAutoBlueB extends OpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommon marvAuto;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
        // blah autopilot setup stuffs

        marvAuto = new MarvMk8CAutopilotSystemCommon(/*stuffs*/);
        marvAuto.setMarvCommon(marv);
        marvAuto.beginPathTravel("mk8c-auto-generic-b");
    }

    public void loop() {
        double[] power = marvAuto.systemTickRaw();
        // drive stuffs, marvAuto knows the common routines so that's done
    }
}
