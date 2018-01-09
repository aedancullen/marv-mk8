package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotHost;
import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTrackerMso;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by aedan on 12/24/17.
 */

@TeleOp(name="Marv-Mk8C User Red B")
public class MarvMk8CUserRedB extends MarvMk8CUserControl {

    public AutopilotSystem marvAuto;

    AutopilotTrackerMso mbxTracker;

    boolean isNavigating;

    @Override
    public void init() {
        super.init();
        mbxTracker = new AutopilotTrackerMso(marv.sonarRx, marv.sonarL, marv.sonarB, MarvNavConstants.MbXOffset, MarvNavConstants.MbYOffset);

        marvAuto = new AutopilotSystem(mbxTracker, telemetry, hardwareMap.appContext);
    }

    @Override
    public void wheelDriveTick() {
        if (gamepad1.x) {
            marv.setAngleHold(180);
            if (marv.angleHoldHasSettled() && !isNavigating) {
                // Condition to begin navigation
                marvAuto.beginPathTravel("mk8c_cryptassist_generic_bl");
                isNavigating = true;
            }
        }
        else if (gamepad1.a) {
            marv.setAngleHold(180);
            if (marv.angleHoldHasSettled() && !isNavigating) {
                // Condition to begin navigation
                marvAuto.beginPathTravel("mk8c_cryptassist_generic_bc");
                isNavigating = true;
            }
        }
        else if (gamepad1.b) {
            marv.setAngleHold(180);
            if (marv.angleHoldHasSettled() && !isNavigating) {
                // Condition to begin navigation
                marvAuto.beginPathTravel("mk8c_cryptassist_generic_br");
                isNavigating = true;
            }
        }
        else {
            // Condition to stop navigation
            isNavigating = false;
            marvAuto.host.setNavigationStatus(AutopilotHost.NavigationStatus.STOPPED);
            marv.disableAngleHold();
        }

        double[] power = marvAuto.systemTickRaw();

        if (isNavigating) {
            marv.drive(power[1], power[1], power[0]);
        }
        else {
            super.wheelDriveTick();
        }
    }
}
