package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by aedan on 3/7/18.
 */

@Autonomous(name = "RHP Auto Blue A")
public class RHPAutoBlueA extends LinearOpMode {

    MarvMk8CCommon marv;

    RelicRecoveryVuMark detectedTrashMark;

    VuforiaLocalizer vooforia;
    VuforiaTrackables vooforGarbage;
    VuforiaTrackable vooforRubbish;

    ReleaseHitPositioner rhp;

    public void runOpMode() {

        rhp = new ReleaseHitPositioner(marv.dropskiColor);

        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = false;
        marv.isOnBSide = false;

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

        waitForStart();

        marv.setDropskiDown();


        long time = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() < time + 2000) {
            detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
            try{Thread.sleep(1);} catch (Exception e) {}
        }

        telemetry.addData("Voofor Say", detectedTrashMark);
        telemetry.update();


        boolean dropskiShouldTurnLeft = ((marv.dropskiIsRed() && marv.isOnRedSide) || (!marv.dropskiIsRed() && !marv.isOnRedSide));

        if (marv.dropskiIsConfident()) {

            if (dropskiShouldTurnLeft) {

            }
            else {

            }

        }

        marv.setDropskiUp();


        if (marv.isOnRedSide) {
            marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
            marv.setEncoderBehavior(RUN_USING_ENCODER);
            while (opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1500) {
                marv.drive(0.25, 0.25, 0);
                try{Thread.sleep(1);} catch (Exception e) {}
            }

            marv.drive(0, 0, 0);
        }
        else { // marv.isOnBlueSide
            marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
            marv.setEncoderBehavior(RUN_USING_ENCODER);
            while (opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition())+Math.abs(marv.fl.getCurrentPosition())/2.0) < 1500) {
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

        detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
        vooforGarbage.deactivate();

        time = System.currentTimeMillis();
        while (opModeIsActive()/* && System.currentTimeMillis() < time + 5000 */&& !marv.angleHoldHasSettled()) {
            marv.drive(0,0,0); // allow angle snapping to run
            try{Thread.sleep(1);} catch (Exception e) {}
        }

    }


    public void localizeLtoR() {
        marv.drive(0, 0, 0.1);
        rhp.blockUntilRelease();
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.blockUntilHit();
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        //marv.drive(0, 0, 0);
    }

    public void localizeRtoL() {
        marv.drive(0, 0, 0.1);
        rhp.blockUntilRelease();
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.blockUntilHit();
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        //marv.drive(0, 0, 0);
    }

    public void goLeft() {
        double inchesX = MarvNavConstants.CryptXOffset;
        double inchesY = MarvNavConstants.CryptYOffset;
        double tolerance = MarvNavConstants.RHPTolerance;
        while (!rhp.posHasBeenReached(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance)) {
            double[] powers = rhp.driveToPos(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance, 0.1);
            marv.drive(powers[1], powers[1], powers[0]);
        }
        marv.drive(0,0,0);
    }

    public void goCenter() {
        double inchesX = 0;
        double inchesY = MarvNavConstants.CryptYOffset;
        double tolerance = MarvNavConstants.RHPTolerance;
        while (!rhp.posHasBeenReached(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance)) {
            double[] powers = rhp.driveToPos(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance, 0.1);
            marv.drive(powers[1], powers[1], powers[0]);
        }
        marv.drive(0,0,0);
    }

    public void goRight() {
        double inchesX = -MarvNavConstants.CryptXOffset;
        double inchesY = MarvNavConstants.CryptYOffset;
        double tolerance = MarvNavConstants.RHPTolerance;
        while (!rhp.posHasBeenReached(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance)) {
            double[] powers = rhp.driveToPos(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance, 0.1);
            marv.drive(powers[1], powers[1], powers[0]);
        }
        marv.drive(0,0,0);
    }


}
