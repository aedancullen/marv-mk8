package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTrackerEnc;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Marv-Mk8C Auto Blue B V2")
public class MarvMk8CAutoBlueBv2 extends LinearOpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommonv2 marvAuto;

    AutopilotTrackerEnc encTracker;
    AutopilotTrackerMso mbxTracker;

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = false;
        marv.isOnBSide = true;

        encTracker = new AutopilotTrackerEnc(marv.fl, marv.fr, 100, marv.imu, 1);
        mbxTracker = new AutopilotTrackerMso(marv.sonarR, marv.sonarB, 7, 2);

        marvAuto = new MarvMk8CAutopilotSystemCommonv2(encTracker, telemetry, hardwareMap.appContext, mbxTracker);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c_auto_generic_b");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.update();
            double[] power = marvAuto.systemTickDifferential();
            marv.drive(power[0], power[1], 0);
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
