package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Marv-Mk8C Auto Red B")
public class MarvMk8CAutoRedB extends OpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommon marvAuto;
    
    AutopilotTrackerMbx mbxTracker;

    public void init() {
         marv = new MarvMk8CCommon(hardwareMap);

        mbxTracker = new AutopilotTrackerMbx(marv.sonarL, marv.sonarB, /*actual values*/0, 0);
        
        marvAuto = new MarvMk8CAutopilotSystemCommon(mbxTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c-auto-generic-b"); 
    }

    public void loop() {
        double[] power = marvAuto.systemTickRaw();
        // drive stuffs, marvAuto knows the common routines so that's done
    }
}
