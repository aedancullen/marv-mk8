package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotTrackerEncMec;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="PYRO Auto Red A")
public class MarvMk8CAutoRedAPyro extends LinearOpMode {

    MarvMk8CCommon marv;
    MarvMk8CAutopilotSystemCommonPyro marvAuto;

    AutopilotTrackerEncMec mecTracker;

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = true;
        marv.isOnBSide = false;

        mecTracker = new AutopilotTrackerEncMec(marv.fl, marv.fr, marv.bl, marv.br, MarvNavConstants.ticksPerUnit, marv.imu, MarvNavConstants.nSubsteps);

        marvAuto = new MarvMk8CAutopilotSystemCommonPyro(this, mecTracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);
        // x-axis mirroring done implicitly by switch of R/L x coord sonar
        marvAuto.beginPathTravel("mk8c_auto_red_a_pyro");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.update();
            double[] power = marvAuto.systemTickDifferential();
            marv.drive(power[0], power[1], 0);
            // drive stuffs, marvAuto knows the common routines so that's done
        }
    }
}
