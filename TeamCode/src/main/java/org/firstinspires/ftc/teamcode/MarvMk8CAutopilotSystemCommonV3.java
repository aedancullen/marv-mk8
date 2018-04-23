package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.evolutionftc.autopilot.AutopilotHost;
import com.evolutionftc.autopilot.AutopilotSegment;
import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTracker;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CAutopilotSystemCommonV3 extends AutopilotSystem {

    double collectspeed = 0.40;
    double collectdiff = 0.20;

    long timeAtBashStart = 0;
    long timeAtDismountStart = 0;

    public MarvMk8CCommon marv;
    LinearOpMode mode;

    PreconfigStorage storage;

    public MarvMk8CAutopilotSystemCommonV3(LinearOpMode mode, AutopilotTracker tracker, Telemetry telemetry, Context appContext){
        super(tracker, telemetry, appContext);
        this.mode = mode;
        storage = new PreconfigStorage(appContext);
    }

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void senseAndStartPath() {
        marv.setDropskiMid();
        marv.setKickskiCenter();

        mode.sleep(500);

        marv.setDropskiDown();

        mode.sleep(500);

        boolean dropskiShouldTurnLeft = ((marv.dropskiIsRed() && marv.isOnRedSide) || (!marv.dropskiIsRed() && !marv.isOnRedSide));

        if (marv.dropskiIsConfident()) {

            if (dropskiShouldTurnLeft) {
                marv.setKickskiCCW();
            } else {
                marv.setKickskiCW();
            }

        }

        marv.setDropskiSafe();

        timeAtDismountStart = System.currentTimeMillis();

        /*if (detectedMark == RelicRecoveryVuMark.RIGHT) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_right");}else {super.beginPathTravel("mk8c_v3_blue_b_right");}
        }
        else if (detectedMark == RelicRecoveryVuMark.CENTER && detectedMark == RelicRecoveryVuMark.UNKNOWN) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_center");}else {super.beginPathTravel("mk8c_v3_blue_b_center");}
        }
        else if (detectedMark == RelicRecoveryVuMark.LEFT) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_left");}else {super.beginPathTravel("mk8c_v3_blue_b_left");}
        }*/
        super.beginPathTravel("mk8c_v3_blue_a_right");
    }

    public boolean shouldContinue(AutopilotSegment segment, double[] robotPosition, double[] robotAttitude) {

        if (segment.id.startsWith("__start__")) {
            if (System.currentTimeMillis() - timeAtDismountStart > 500) {
                marv.setKickskiCenter();
                marv.setDropskiUp();
            }
        }

        else if (segment.id.startsWith("collect")) {
            if (marv.readScotty() > 1.5) {
                return false;
            }
        }
        else if (segment.id.startsWith("bash")) {
            if (System.currentTimeMillis() - timeAtBashStart > 1500) {
                return false;
            }
        }
        else if (segment.id.startsWith("flip") && host.getNavigationStatus() == AutopilotHost.NavigationStatus.ORIENTING) {
            marv.setConveyGateOpen();
            marv.convey(0);
            if (!segment.id.contains("key")) {
                marv.collectorR.setPower(-collectspeed - collectdiff / 2.0);
                marv.collectorL.setPower(collectspeed - collectdiff / 2.0);
            }
            else {
                marv.collectorL.setPower(-0.25);
            }
        }


        return true;
    }


    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

        if (previous != null && previous.id.endsWith("nopid")) {
            marv.setEncoderBehavior(RUN_USING_ENCODER);
        }
        if (next != null && next.id.endsWith("nopid")) {
            marv.setEncoderBehavior(RUN_WITHOUT_ENCODER);
        }

        if (previous != null && previous.id.startsWith("flip")) {
            marv.drive(0, 0, 0);
            /*marv.setConveyGateOpen();
            marv.convey(0);
            marv.collectorR.setPower(-collectspeed - collectdiff / 2.0);
            marv.collectorL.setPower(collectspeed - collectdiff / 2.0);
            mode.sleep(500);*/
            marv.setFlippoPos(1);
            mode.sleep(750);
            marv.setFlippoPos(0);

            timeAtBashStart = System.currentTimeMillis();
        }

        if (next != null && next.id.startsWith("collect")) {
            marv.convey(1);
            marv.collectorR.setPower(collectspeed + collectdiff / 2.0);
            marv.collectorL.setPower(-collectspeed + collectdiff / 2.0);

            if (next.id.startsWith("collect1")) {
                next.navigationTarget = new double[]{storage.readInt("p1x"), storage.readInt("p1y"), 0};
            }
            else if (next.id.startsWith("collect2")) {
                next.navigationTarget = new double[]{storage.readInt("p2x"), storage.readInt("p2y"), 0};
            }
            else if (next.id.startsWith("collect3")) {
                next.navigationTarget = new double[]{storage.readInt("p3x"), storage.readInt("p3y"), 0};
            }
            else if (next.id.startsWith("collect4")) {
                next.navigationTarget = new double[]{storage.readInt("p4x"), storage.readInt("p4y"), 0};
            }
        }

        if (previous != null && previous.id.startsWith("collect")) {
            marv.setConveyGateClosed();
        }

    }

}
