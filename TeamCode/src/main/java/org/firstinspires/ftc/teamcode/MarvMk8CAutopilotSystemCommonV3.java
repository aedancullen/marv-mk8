package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.evolutionftc.autopilot.AutopilotHost;
import com.evolutionftc.autopilot.AutopilotSegment;
import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTracker;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

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
    long timeAtCollectStart = 0;

    public MarvMk8CCommon marv;
    LinearOpMode mode;

    PreconfigStorage storage;

    VuforiaLocalizer vooforia;
    VuforiaTrackables vooforGarbage;
    VuforiaTrackable vooforRubbish;
    RelicRecoveryVuMark detectedMark;

    public void startVoof() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AQaSz0//////AAAAmWK7mLvLWUvPmqojdrAEgctDNGkihSVvah2Cd2y7iGo8Bg6dQUJLVoguaAqG4QYpI/87Ccb0wO4nd+jcVrzX8tF8rS4UPhr3bXkKHYtqwUjlpSvKKJzSsFGIe+MGpmmSK824Ja7JVikVoJO/u5ubCkYjm9Fyi+87T2qdjS/+RdNELgLJSDVS3Hp3nbCII6JGutHNROuLOclZCFARI1djpNJu6YNzlvCr+AJQd4Q+i0ZYv378aWnasQYifKGA8KafQMLMmNZmghljMNPDnlfFqZmn4BhItnyrBS1dbXG7BnU7xOw8DIIRq0VjEzSMiikBaUxWXxQn+K+KWahXDwchjL193WpriOF8ovjcGbeGnzJU";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vooforia = ClassFactory.createVuforiaLocalizer(parameters);

        vooforGarbage = this.vooforia.loadTrackablesFromAsset("RelicVuMark");
        vooforRubbish = vooforGarbage.get(0);

        vooforGarbage.activate();

        detectedMark = RelicRecoveryVuMark.from(vooforRubbish);

    }

    public void stopVoof() {
        detectedMark = RelicRecoveryVuMark.from(vooforRubbish);
        vooforGarbage.deactivate();
    }

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

        startVoof();
    }

    public void senseAndStartPath() {
        stopVoof();

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

        if (!marv.isOnBSide) {
            if (detectedMark == RelicRecoveryVuMark.RIGHT) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_a_right");}else {super.beginPathTravel("mk8c_v3_blue_a_right");}
            }
            else if (detectedMark == RelicRecoveryVuMark.CENTER || detectedMark == RelicRecoveryVuMark.UNKNOWN) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_a_center");}else {super.beginPathTravel("mk8c_v3_blue_a_center");}
            }
            else if (detectedMark == RelicRecoveryVuMark.LEFT) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_a_left");}else {super.beginPathTravel("mk8c_v3_blue_a_left");}
            }
        }
        else {
            if (detectedMark == RelicRecoveryVuMark.RIGHT) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_right");}else {super.beginPathTravel("mk8c_v3_blue_b_right");}
            }
            else if (detectedMark == RelicRecoveryVuMark.CENTER || detectedMark == RelicRecoveryVuMark.UNKNOWN) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_center");}else {super.beginPathTravel("mk8c_v3_blue_b_center");}
            }
            else if (detectedMark == RelicRecoveryVuMark.LEFT) {
                if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_left");}else {super.beginPathTravel("mk8c_v3_blue_b_left");}
            }
        }

        //super.beginPathTravel("mk8c_v3_blue_a_right");
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
            if (System.currentTimeMillis()  - timeAtCollectStart > 5000) {
                return false;
            }
        }
        else if (segment.id.startsWith("bash")) {
            if (System.currentTimeMillis() - timeAtBashStart > 1500) {
                return false;
            }
            marv.collectorL.setPower(0);
            marv.collectorR.setPower(0.5);
        }
        else if (segment.id.startsWith("flip") && host.getNavigationStatus() == AutopilotHost.NavigationStatus.ORIENTING) {
            marv.setConveyGateOpen();
            marv.convey(0);
            if (!segment.id.contains("key")) {
                marv.collectorR.setPower(-collectspeed - collectdiff / 2.0);
                marv.collectorL.setPower(collectspeed - collectdiff / 2.0);
            }
            else {
                marv.collectorL.setPower(-0.5);
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

            timeAtCollectStart = System.currentTimeMillis();

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
