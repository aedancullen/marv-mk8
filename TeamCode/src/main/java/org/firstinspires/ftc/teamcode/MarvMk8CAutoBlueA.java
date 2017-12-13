package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Marv-Mk8C Auto Blue A")
public class MarvMk8CAutoBlueA extends LinearOpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommon marvAuto;
    
    AutopilotTrackerMso mbxTracker;

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = false;
        marv.isOnBSide = false;

        mbxTracker = new AutopilotTrackerMso(marv.sonarR, marv.sonarB, 7.25, 2.25);

        marvAuto = new MarvMk8CAutopilotSystemCommon(mbxTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c_auto_generic_a");

        waitForStart();

        while(opModeIsActive()) {
            double[] power = marvAuto.systemTickRaw();
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
