package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Marv-Mk8C Auto Red A")
public class MarvMk8CAutoRedA extends LinearOpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommon marvAuto;
    
    AutopilotTrackerMso mbxTracker;

    public void runOpMode() {
         marv = new MarvMk8CCommon(hardwareMap);

        mbxTracker = new AutopilotTrackerMso(marv.sonarL, marv.sonarB, /*actual values*/0, 0);
        
        marvAuto = new MarvMk8CAutopilotSystemCommon(mbxTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c-auto-generic-a");

        waitForStart();

        while(opModeIsActive()) {
            double[] power = marvAuto.systemTickRaw();
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
