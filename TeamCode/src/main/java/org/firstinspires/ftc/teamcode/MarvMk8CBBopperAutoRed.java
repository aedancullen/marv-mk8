package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotTrackerEncMec;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Bob on 4/18/2018.
 */

@Autonomous(name = "BBopper Red")
public class MarvMk8CBBopperAutoRed extends LinearOpMode {

    MarvMk8CCommon marv;
    AutopilotTrackerEncMec tracker;
    MarvMk8CAutopilotSystemCommonV3 marvAuto;

    public void runOpMode() {

        marv = new MarvMk8CCommon(hardwareMap);
        tracker = new AutopilotTrackerEncMec(marv.fl, marv.fr, marv.bl, marv.br, MarvNavConstants.ticksPerUnit, marv.imu, MarvNavConstants.nSubsteps);
        marvAuto = new MarvMk8CAutopilotSystemCommonV3(this, tracker, telemetry, hardwareMap.appContext);
        marvAuto.setMarvCommon(marv);

        marv.isOnRedSide = true;
        marv.isOnBSide = true;

        waitForStart();

        marvAuto.senseAndStartPath();

        while (opModeIsActive()) {
            double[] powers = marvAuto.systemTickDifferential();
            marv.drive(powers[0], powers[1], 0);
            //marv.winchTick();
        }

    }

}
