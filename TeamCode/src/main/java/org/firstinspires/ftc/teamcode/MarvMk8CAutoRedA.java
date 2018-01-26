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
    MarvMk8CAutopilotSystemCommonV2 marvAuto;
    
    AutopilotTrackerMso mbxTracker;

    public void runOpMode() {
         marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = true;
        marv.isOnBSide = false;

        mbxTracker = new AutopilotTrackerMso(marv.sonarRx, marv.sonarL, marv.sonarB, MarvNavConstants.MbXOffset, MarvNavConstants.MbYOffset);
        
        marvAuto = new MarvMk8CAutopilotSystemCommonV2(this, mbxTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c_auto_generic_a_v2");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.update();
            double[] power = marvAuto.systemTickRaw();
            marv.drive(power[1], power[1], power[0]);
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
