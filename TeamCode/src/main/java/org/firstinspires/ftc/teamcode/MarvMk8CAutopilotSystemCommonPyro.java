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

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CAutopilotSystemCommonPyro extends AutopilotSystem {

    public MarvMk8CCommon marv;

    LinearOpMode mode;

    RelicRecoveryVuMark detectedTrashMark;

    VuforiaLocalizer vooforia;
    VuforiaTrackables vooforGarbage;
    VuforiaTrackable vooforRubbish;

    AutopilotTracker secondaryTracker;


    public MarvMk8CAutopilotSystemCommonPyro(LinearOpMode mode, AutopilotTracker tracker, AutopilotTracker secondaryTracker, Telemetry telemetry, Context appContext){
        super(tracker, telemetry, appContext);
        this.mode = mode;
        this.secondaryTracker = secondaryTracker;
    }

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
        marv.setEncoderBehavior(RUN_USING_ENCODER);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AQaSz0//////AAAAmWK7mLvLWUvPmqojdrAEgctDNGkihSVvah2Cd2y7iGo8Bg6dQUJLVoguaAqG4QYpI/87Ccb0wO4nd+jcVrzX8tF8rS4UPhr3bXkKHYtqwUjlpSvKKJzSsFGIe+MGpmmSK824Ja7JVikVoJO/u5ubCkYjm9Fyi+87T2qdjS/+RdNELgLJSDVS3Hp3nbCII6JGutHNROuLOclZCFARI1djpNJu6YNzlvCr+AJQd4Q+i0ZYv378aWnasQYifKGA8KafQMLMmNZmghljMNPDnlfFqZmn4BhItnyrBS1dbXG7BnU7xOw8DIIRq0VjEzSMiikBaUxWXxQn+K+KWahXDwchjL193WpriOF8ovjcGbeGnzJU";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vooforia = ClassFactory.createVuforiaLocalizer(parameters);

        vooforGarbage = this.vooforia.loadTrackablesFromAsset("RelicVuMark");
        vooforRubbish = vooforGarbage.get(0);

        vooforGarbage.activate();

        detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
    }

    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

        if (next != null && next.id.equals("__start__")){


            marv.setDropskiDown();


            long time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 2000) {
                detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            mode.telemetry.addData("Voofor Say", detectedTrashMark);
            mode.telemetry.update();


            boolean dropskiShouldTurnLeft = ((marv.dropskiIsRed() && marv.isOnRedSide) || (!marv.dropskiIsRed() && !marv.isOnRedSide));

            if (marv.dropskiIsConfident()) {

                if (dropskiShouldTurnLeft) {
                    double frZero = (marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0;
                    while (mode.opModeIsActive() && ((marv.fr.getCurrentPosition()+-marv.fl.getCurrentPosition())/2.0 < frZero + 75)) {
                        marv.fr.setPower(0.20);
                        marv.br.setPower(0.20);
                        marv.fl.setPower(-0.20);
                        marv.bl.setPower(-0.20);
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
                        marv.fr.setPower(-0.20);
                        marv.br.setPower(-0.20);
                        marv.fl.setPower(0.20);
                        marv.bl.setPower(0.20);
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
                marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
                marv.setEncoderBehavior(RUN_USING_ENCODER);
                while (mode.opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1500) {
                    marv.drive(0.25, 0.25, 0);
                    try{Thread.sleep(1);} catch (Exception e) {}
                }

                marv.drive(0, 0, 0);
            }
            else { // marv.isOnBlueSide
                marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
                marv.setEncoderBehavior(RUN_USING_ENCODER);
                while (mode.opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1500) {
                    marv.drive(-0.25, -0.25, 0);
                    try{Thread.sleep(1);} catch (Exception e) {}
                }

                marv.drive(0, 0, 0);
            }

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

            vooforGarbage.deactivate();
            detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);


            time = System.currentTimeMillis();
            while (mode.opModeIsActive()/* && System.currentTimeMillis() < time + 5000 */&& !marv.angleHoldHasSettled()) {
                marv.drive(0,0,0); // allow angle snapping to run
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            secondaryTracker.update();
            double[] secondaryRobotPosition = secondaryTracker.getRobotPosition();

            time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 1000) {
                marv.drive(0,0,0); // pause
                secondaryTracker.update();
                double[] newSecondaryRobotPosition = secondaryTracker.getRobotPosition();
                for (int i=0;i<3;i++) {
                    secondaryRobotPosition[i] = (secondaryRobotPosition[i] + newSecondaryRobotPosition[i]) / 2.0;
                }
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            if (marv.isOnRedSide && !marv.isOnBSide) {
                secondaryRobotPosition[0] = -secondaryRobotPosition[0];
            }
            if (marv.isOnRedSide && marv.isOnBSide) {
                secondaryRobotPosition[0] = -secondaryRobotPosition[0];
                secondaryRobotPosition[1] = -secondaryRobotPosition[1];
            }
            if (!marv.isOnRedSide && !marv.isOnBSide) {
                secondaryRobotPosition[0] = -secondaryRobotPosition[0];
                secondaryRobotPosition[1] = -secondaryRobotPosition[1];
            }
            if (!marv.isOnRedSide && marv.isOnBSide) {
                secondaryRobotPosition[0] = -secondaryRobotPosition[0];
            }

            marv.disableAngleHold();
            marv.drive(0,0,0);

            marv.setGatesPosition(1);
            marv.collect(0.5);

            marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
            marv.setEncoderBehavior(RUN_USING_ENCODER);
            host.setRobotPosition(secondaryRobotPosition);
            host.communicate(tracker);

            //while (true) {host.telemetryUpdate();}

        }
        
        if (next != null && next.id.toLowerCase().contains("tokey")) {
            // modify coords of next accordingly
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("flipL"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("flipR"));
            }
            else {
                next.populateFromOther(this.pathFollower.getSegment("flipC"));
            }
        }
        
        if (next != null && next.id.toLowerCase().contains("toadd1")) {
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go center
                next.populateFromOther(this.pathFollower.getSegment("flipC"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("flipL"));
            }
            else {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("flipL"));
            }
        }
        
        if (next != null && next.id.toLowerCase().contains("toadd2")) {
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("flipR"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go center
                next.populateFromOther(this.pathFollower.getSegment("flipC"));
            }
            else {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("flipR"));
            }
        }
        
        if (previous != null && previous.id.toLowerCase().contains("to")){
            marv.drive(0, 0, 0);

            marv.setFlippoPos(1);
            long time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 500) {
                try{Thread.sleep(1);} catch (Exception e) {}
            }
            marv.setFlippoPos(0);

        }
    }

}
