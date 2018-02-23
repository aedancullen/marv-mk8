package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotTrackerEncMec;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.evolutionftc.autopilot.AutopilotTrackerMsoSimple;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="PYRO Auto Blue B")
public class MarvMk8CAutoBlueBPyro extends LinearOpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommonPyro marvAuto;

    AutopilotTrackerEncMec mecTracker;
    AutopilotTrackerMsoSimple mbxTracker;

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = false;
        marv.isOnBSide = true;

        mecTracker = new AutopilotTrackerEncMec(marv.fl, marv.fr, marv.bl, marv.br, MarvNavConstants.ticksPerUnit, marv.imu, MarvNavConstants.nSubsteps);
        mbxTracker = new AutopilotTrackerMsoSimple(marv.sonarRx, marv.sonarR, marv.sonarB, MarvNavConstants.MbXOffset, MarvNavConstants.MbYOffset);

        marvAuto = new MarvMk8CAutopilotSystemCommonPyro(this, mecTracker, mbxTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c_auto_blue_b_pyro");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.update();
            double[] power = marvAuto.systemTickDifferential();
            marv.drive(power[0], power[1], 0);
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
