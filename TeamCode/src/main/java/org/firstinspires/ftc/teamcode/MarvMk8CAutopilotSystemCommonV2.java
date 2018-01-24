package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.evolutionftc.autopilot.AutopilotSegment;
import com.evolutionftc.autopilot.AutopilotSystem;
import com.evolutionftc.autopilot.AutopilotTracker;
import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CAutopilotSystemCommonV2 extends AutopilotSystem {

    public MarvMk8CCommon marv;

    LinearOpMode mode;

    VuforiaLocalizer vooforia;
    VuforiaTrackables vooforGarbage;
    VuforiaTrackable vooforRubbish;

    RelicRecoveryVuMark detectedTrashMark;

    JewelOverlayVisionProcessor jewelVisionProcessor;
    ObjectSpec joules;

    public MarvMk8CAutopilotSystemCommonV2(LinearOpMode mode, AutopilotTracker tracker, Telemetry telemetry, Context appContext){
        super(tracker, telemetry, appContext);
        this.mode = mode;

        CameraSpec zteSpeedCameraLandscape = new CameraSpec(0.9799, Math.PI / 2); // nobody cares about positioning stuff
        jewelVisionProcessor = new JewelOverlayVisionProcessor(mode.hardwareMap.appContext, zteSpeedCameraLandscape);

        joules = new ObjectSpec(mode.hardwareMap.appContext, "haarcascade_evolution_jewels_2k3k_20st", 6.1); // again - nobody cares
    }

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

        if (next != null && next.id.equals("__start__")){
            // Do vision sensing, drive off balancing stone and set angle hold

            jewelVisionProcessor.start();
            jewelVisionProcessor.loadObject(joules);

            long time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 5000 && !jewelVisionProcessor.getIsConfident()) {
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            if (jewelVisionProcessor.getIsConfident()) {
                mode.telemetry.addData("Is 'RED - BLUE'", jewelVisionProcessor.getIsRedBlueInThatOrder());
            }
            else {
                mode.telemetry.addData("Is 'RED - BLUE'", "Not sure");
            }
            mode.telemetry.update();

            jewelVisionProcessor.stop();

            marv.setDropskiDown();

            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = "AQaSz0//////AAAAmWK7mLvLWUvPmqojdrAEgctDNGkihSVvah2Cd2y7iGo8Bg6dQUJLVoguaAqG4QYpI/87Ccb0wO4nd+jcVrzX8tF8rS4UPhr3bXkKHYtqwUjlpSvKKJzSsFGIe+MGpmmSK824Ja7JVikVoJO/u5ubCkYjm9Fyi+87T2qdjS/+RdNELgLJSDVS3Hp3nbCII6JGutHNROuLOclZCFARI1djpNJu6YNzlvCr+AJQd4Q+i0ZYv378aWnasQYifKGA8KafQMLMmNZmghljMNPDnlfFqZmn4BhItnyrBS1dbXG7BnU7xOw8DIIRq0VjEzSMiikBaUxWXxQn+K+KWahXDwchjL193WpriOF8ovjcGbeGnzJU";

            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            this.vooforia = ClassFactory.createVuforiaLocalizer(parameters);

            vooforGarbage = this.vooforia.loadTrackablesFromAsset("RelicVuMark");
            vooforRubbish = vooforGarbage.get(0);

            vooforGarbage.activate();

            detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);

            time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 5000 && detectedTrashMark == RelicRecoveryVuMark.UNKNOWN) {
                detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            mode.telemetry.addData("Voofor Say", detectedTrashMark);
            mode.telemetry.update();

            vooforGarbage.deactivate();


            if (jewelVisionProcessor.getIsConfident()) {

                boolean shouldTurnLeft = (
                        (!jewelVisionProcessor.getIsRedBlueInThatOrder() && marv.isOnRedSide)
                                ||
                        (jewelVisionProcessor.getIsRedBlueInThatOrder() && !marv.isOnRedSide)
                );

                if (shouldTurnLeft) {
                    double frZero = (marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0;
                    while (mode.opModeIsActive() && ((marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0 < frZero + 75)) {
                        marv.fr.setPower(0.15);
                        marv.br.setPower(0.15);
                        marv.fl.setPower(-0.15);
                        marv.bl.setPower(-0.15);
                        try{Thread.sleep(1);} catch (Exception e) {}
                    }

                    marv.fr.setPower(0);
                    marv.br.setPower(0);
                    marv.fl.setPower(0);
                    marv.bl.setPower(0);
                }
                else {
                    double frZero = (marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0;
                    while (mode.opModeIsActive() && ((marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0 > frZero - 75)) {
                        marv.fr.setPower(-0.15);
                        marv.br.setPower(-0.15);
                        marv.fl.setPower(0.15);
                        marv.bl.setPower(0.15);
                        try{Thread.sleep(1);} catch (Exception e) {}
                    }

                    marv.fr.setPower(0);
                    marv.br.setPower(0);
                    marv.fl.setPower(0);
                    marv.bl.setPower(0);
                }

            }

            marv.setDropskiUp();


            if (marv.isOnRedSide) {
                marv.fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                while (marv.fr.getCurrentPosition() != 0) {}
                marv.fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                marv.fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                while (marv.fl.getCurrentPosition() != 0) {}
                marv.fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                while (mode.opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1200) {
                    marv.drive(0.15, 0.15, 0);
                    try{Thread.sleep(1);} catch (Exception e) {}
                }

                marv.drive(0, 0, 0);
            }
            else { // marv.isOnBlueSide
                marv.fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                while (marv.fr.getCurrentPosition() != 0) {}
                marv.fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                marv.fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                while (marv.fl.getCurrentPosition() != 0) {}
                marv.fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                while (mode.opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1200) {
                    marv.drive(-0.15, -0.15, 0);
                    try{Thread.sleep(1);} catch (Exception e) {}
                }

                marv.drive(0, 0, 0);
            }

            /*time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 1000) {
                marv.drive(0, 0, 0);
            }*/

            if (marv.isOnRedSide) {
                if (marv.isOnBSide) {
                    marv.setAngleHold(180);
                }
                else { // marv.isOnASide
                    marv.setAngleHold(-90);
                }
            }
            else { // marv.isOnBlueSide
                if (marv.isOnBSide) {
                    marv.setAngleHold(0);
                }
                else { // marv.isOnASide
                    marv.setAngleHold(-90);
                }
            }

            time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 5000 && !marv.angleHoldHasSettled()) {
                marv.drive(0,0,0); // allow angle snapping to run
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            marv.drive(0, 0, 0); // ensure motors off


        }
        else if (previous != null && previous.id.equals("__start__")){
            marv.drive(0, 0, 0);
            // Approach and glyph ejection routine
            // Can use
            //
            // this.host.setNavigationTarget(AutopilotSegment);
            // this.host.setNavigationStatus(NavigationStatus.RUNNING);
            // while (this.host.getNavigationStatus == NavigationStatus.RUNNING) {
            //      this.host.communicate(this.tracker);
            //      double[] power = this.host.navigationTickRaw();
            //      // marv.drive stuff
            // }
            // // marv.drive stop the robot
            //
            // to get to arbitrary coords for glyph ejection
            // Then can drop glyph and proceed
            // The beauty of evolutionftc.autopilot is that we don't have to care which alliance we're on;
            // just use the coordinate system

            long time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 3000) {
                marv.drive(0,0,0);
                marv.convey(1);
                try{Thread.sleep(1);} catch (Exception e) {}
            }
            //marv.convey(0);
        }
    }

}
