package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTrackerEncMec;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by aedan on 12/3/17.
 */

@Autonomous(name="Autopilot EncMec Dead-reckoning Test")
public class MarvMk8CAutopilotDeadreckonTest extends LinearOpMode {

    MarvMk8CCommon marv;

    AutopilotTrackerEncMec mecTracker;

    long lastTime;

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);

        mecTracker = new AutopilotTrackerEncMec(marv.fl, marv.fr, marv.bl, marv.br, MarvNavConstants.ticksPerUnit, marv.imu, MarvNavConstants.nSubsteps);

        waitForStart();

        lastTime = System.currentTimeMillis();

        while(opModeIsActive()) {
            long time = System.currentTimeMillis();
            double elapsedS = (time - lastTime) / 1000.0;
            lastTime = time;
            mecTracker.update();
            telemetry.addData("x", mecTracker.getRobotPosition()[0]);
            telemetry.addData("y", mecTracker.getRobotPosition()[1]);
            telemetry.addData("Substeps per second", MarvNavConstants.nSubsteps / elapsedS);
            telemetry.update();
        }
    }
}
