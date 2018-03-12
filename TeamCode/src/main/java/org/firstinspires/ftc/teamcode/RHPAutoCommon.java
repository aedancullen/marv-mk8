package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.system.SystemProperties;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.firstinspires.ftc.teamcode.MarvNavConstants.ticksPerUnit;

/**
 * Created by Bob on 3/8/2018.
 */

public class RHPAutoCommon extends LinearOpMode {

    MarvMk8CCommon marv;

    RelicRecoveryVuMark detectedTrashMark;

    VuforiaLocalizer vooforia;
    VuforiaTrackables vooforGarbage;
    VuforiaTrackable vooforRubbish;

    ReleaseHitPositioner rhp;

    public void runOpMode() {

        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
        marv.setEncoderBehavior(RUN_USING_ENCODER);

        rhp = new ReleaseHitPositioner(marv.dropskiColor);

        startVoof();

        waitForStart();

        stopVoof();

        telemetry.addData("Voof say", detectedTrashMark);
        telemetry.update();

        if (detectedTrashMark == RelicRecoveryVuMark.UNKNOWN) {
            detectedTrashMark = RelicRecoveryVuMark.CENTER;
        }


        /*jewelRoutine();
        dismountRoutine();

        setSnap();*/


        marv.setAngleHold(0);

        if (marv.isOnRedSide) {
            //enterFromRight();
            localizeLtoR();
            goCenter();
        }
        else {
            //enterFromLeft();
            localizeRtoL();
            goCenter();
        }

        if (detectedTrashMark == RelicRecoveryVuMark.CENTER) {
            //placeCenterRoutine();
            goCenter();
            flip();
        }
        else if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
            //placeLeftRoutine();
            goLeft();
            flip();
        }
        else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
            //placeRightRoutine();
            goRight();
            flip();
        }

        if (!marv.isOnBSide) {
            goAGlyphing();
            marv.collect(0.5);
            goABashing();
            goAGlyphing();
            marv.collect(0);
        }

        //bashPile();


        /*telemetry.addData("edgeLX", rhp.edgeLX);
        telemetry.addData("edgeRX", rhp.edgeRX);
        telemetry.addData("zeroX", rhp.zeroX);
        telemetry.addData("zeroY", rhp.zeroY);
        telemetry.addData("posX", rhp.encoderDecomposeMecX(marv.fl, marv.fr, marv.bl, marv.br) / MarvNavConstants.ticksPerUnit);
        telemetry.addData("posY", rhp.encoderDecomposeMecY(marv.fl, marv.fr, marv.bl, marv.br) / MarvNavConstants.ticksPerUnit);*/

        while (opModeIsActive()) { marv.drive(0, 0, 0);}

    }

    public void placeCenterRoutine() {
        localizeCenterRL();
        goCenter();
        flip();
    }

    public void placeLeftRoutine() {
        localizeCenterRL();
        goLeft();
        flip();
    }

    public void placeRightRoutine() {
        localizeCenterLR();
        goRight();
        flip();
    }

    public void startVoof() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AQaSz0//////AAAAmWK7mLvLWUvPmqojdrAEgctDNGkihSVvah2Cd2y7iGo8Bg6dQUJLVoguaAqG4QYpI/87Ccb0wO4nd+jcVrzX8tF8rS4UPhr3bXkKHYtqwUjlpSvKKJzSsFGIe+MGpmmSK824Ja7JVikVoJO/u5ubCkYjm9Fyi+87T2qdjS/+RdNELgLJSDVS3Hp3nbCII6JGutHNROuLOclZCFARI1djpNJu6YNzlvCr+AJQd4Q+i0ZYv378aWnasQYifKGA8KafQMLMmNZmghljMNPDnlfFqZmn4BhItnyrBS1dbXG7BnU7xOw8DIIRq0VjEzSMiikBaUxWXxQn+K+KWahXDwchjL193WpriOF8ovjcGbeGnzJU";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vooforia = ClassFactory.createVuforiaLocalizer(parameters);

        vooforGarbage = this.vooforia.loadTrackablesFromAsset("RelicVuMark");
        vooforRubbish = vooforGarbage.get(0);

        vooforGarbage.activate();

        detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);

    }

    public void stopVoof() {
        detectedTrashMark = RelicRecoveryVuMark.from(vooforRubbish);
        vooforGarbage.deactivate();
    }

    public void flip() {
        marv.drive(0, 0, 0);
        marv.setFlippoPos(1);
        long time = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() < time + 750) {
            try{Thread.sleep(1);marv.drivehp(0, 0, 0);} catch (Exception e) {}
        }
        marv.setFlippoPos(0);
    }

    public void jewelRoutine() {

        marv.setDropskiMid();
        marv.setKickskiCenter();

        sleep(500);

        marv.setDropskiDown();

        sleep(500);

        boolean dropskiShouldTurnLeft = ((marv.dropskiIsRed() && marv.isOnRedSide) || (!marv.dropskiIsRed() && !marv.isOnRedSide));

        if (marv.dropskiIsConfident()) {

            if (dropskiShouldTurnLeft) {
                marv.setKickskiCCW();
            } else {
                marv.setKickskiCW();
            }

        }

        marv.setDropskiSafe();

    }

    public void homeJewelArm() {
        marv.setKickskiCenter();
        marv.setDropskiUp();
    }

    public void dismountRoutine() {

        if (marv.isOnRedSide) {
            marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
            marv.setEncoderBehavior(RUN_USING_ENCODER);
            long start = System.currentTimeMillis();
            while (opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition()) + Math.abs(marv.fl.getCurrentPosition()) / 2.0) < 1500) {
                if (!marv.isOnBSide) {
                    marv.drive(0.30, 0.25, 0);
                }
                else {
                    marv.drive(0.25, 0.25, 0);
                }
                if (System.currentTimeMillis() > start + 500) {
                    homeJewelArm();
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }

            marv.drive(0, 0, 0);
        } else { // marv.isOnBlueSide
            marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
            marv.setEncoderBehavior(RUN_USING_ENCODER);
            long start = System.currentTimeMillis();
            while (opModeIsActive() && (Math.abs(marv.fr.getCurrentPosition()) + Math.abs(marv.fl.getCurrentPosition()) / 2.0) < 1500) {
                if (!marv.isOnBSide) {
                    marv.drive(-0.30, -0.25, 0);
                }
                else {
                    marv.drive(-0.25, -0.25, 0);
                }
                if (System.currentTimeMillis() > start + 500) {
                    homeJewelArm();
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }

            marv.drive(0, 0, 0);
        }

    }

    public void setSnap() {

        if (marv.isOnRedSide) {
            if (marv.isOnBSide) {
                marv.setAngleHold(180);
            } else { // marv.isOnASide
                marv.setAngleHold(-90);
            }
        } else { // marv.isOnBlueSide
            if (marv.isOnBSide) {
                marv.setAngleHold(0);
            } else { // marv.isOnASide
                marv.setAngleHold(-90);
            }
        }

        time = System.currentTimeMillis();
        int countsSettled = 0;
        while (opModeIsActive()/* && System.currentTimeMillis() < time + 5000 */ && countsSettled < 3) {
            marv.drive(0, 0, 0); // allow angle snapping to run
            if (marv.angleHoldHasSettled()) {
                countsSettled++;
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }

        marv.fl.setPower(0);
        marv.br.setPower(0);
        marv.bl.setPower(0);
        marv.fr.setPower(0);
        marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
        marv.setEncoderBehavior(RUN_USING_ENCODER);

    }


    public void enterFromRight() {
        marv.drive(0, 0, 0.25);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, 0.25);
            }
        });
        while (opModeIsActive() && rhp.rhpcHasLine()) {
            marv.drive(0, 0, 0.15);
        }
        marv.drive(0, 0, 0);
    }

    public void enterFromLeft() {
        marv.drive(0, 0, -0.25);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, -0.25);
            }
        });
        while (opModeIsActive() && rhp.rhpcHasLine()) {
            marv.drive(0, 0, -0.15);
        }
        marv.drive(0, 0, 0);
    }

    public void exitLtoR() {
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, 0.35);
            }
        });
        while (opModeIsActive() && rhp.rhpcHasLine()) {marv.drivehp(0, 0, 0.15);}
    }

    public void exitRtoL() {
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, -0.35);
            }
        });
        while (opModeIsActive() && rhp.rhpcHasLine()) {marv.drivehp(0, 0, -0.15);}
    }


    public void localizeLtoR() {
        rhp.resetZeros();
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, 0.15);
            }
        });
        marv.drive(0, 0, 0);
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        exitLtoR();
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, -0.15);
            }
        });
        marv.drive(0, 0, 0);
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.compute(marv.fl, marv.fr, marv.bl, marv.br);
    }

    public void localizeCenterLR() {
        rhp.resetZeros();
        marv.drivehp(0, 0, -0.15);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, -0.15);
            }
        });
        marv.drivehp(0, 0, 0);
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        marv.drivehp(0, 0, 0.15);
        sleep(1000);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, 0.15);
            }
        });
        marv.drivehp(0, 0, 0);
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.compute(marv.fl, marv.fr, marv.bl, marv.br);
    }

    public void localizeCenterRL() {
        rhp.resetZeros();
        marv.drivehp(0, 0, 0.15);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, 0.15);
            }
        });
        marv.drivehp(0, 0, 0);
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        marv.drivehp(0, 0, -0.15);
        sleep(1000);
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drivehp(0, 0, -0.15);
            }
        });
        marv.drivehp(0, 0, 0);
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.compute(marv.fl, marv.fr, marv.bl, marv.br);
    }

    public void localizeRtoL() {
        rhp.resetZeros();
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, -0.15);
            }
        });
        marv.drive(0, 0, 0);
        rhp.recordEdgeRXMec(marv.fl, marv.fr, marv.bl, marv.br);
        exitRtoL();
        rhp.blockUntilHit(this, new Runnable() {
            @Override
            public void run() {
                marv.drive(0, 0, 0.15);
            }
        });
        marv.drive(0, 0, 0);
        rhp.recordEdgeLXMec(marv.fl, marv.fr, marv.bl, marv.br);
        rhp.compute(marv.fl, marv.fr, marv.bl, marv.br);
    }

    public void goLeft() {
        double ticksX = rhp.zeroX + (8.25 * ticksPerUnit);
        double ticksY = rhp.zeroY + (MarvNavConstants.CryptYOffset * ticksPerUnit);

        marv.setEncoderBehavior(RUN_TO_POSITION);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setDriveTargetPowers(0.40);
        marv.setDriveTargetPositions(ticksY, ticksX);

        while (opModeIsActive() && marv.encodersAreBusy()) {}

        marv.setDriveTargetPowers(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void goCenter() {
        /*rhp.resetFinishedFlags();
        rhp.yHasFinished = true;
        double inchesX = 0;
        double inchesY = MarvNavConstants.CryptYOffset;
        double tolerance = MarvNavConstants.RHPTolerance;
        while (opModeIsActive() && !rhp.posHasBeenReached()) {
            double[] powers = rhp.driveToPos(marv.fl, marv.fr, marv.bl, marv.br, inchesX, inchesY, tolerance, 0.15);
            marv.drive(powers[1], powers[1], powers[0]);
        }
        marv.drive(0,0,0);*/

        double ticksX = rhp.zeroX + (0 * ticksPerUnit);
        double ticksY = rhp.zeroY + (MarvNavConstants.CryptYOffset * ticksPerUnit);

        marv.setEncoderBehavior(RUN_TO_POSITION);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setDriveTargetPowers(0.40);
        marv.setDriveTargetPositions(ticksY, ticksX);

        while (opModeIsActive() && marv.encodersAreBusy()) {}

        marv.setDriveTargetPowers(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void goRight() {
        double ticksX = rhp.zeroX + (-8.25 * ticksPerUnit);
        double ticksY = rhp.zeroY + (MarvNavConstants.CryptYOffset * ticksPerUnit);

        marv.setEncoderBehavior(RUN_TO_POSITION);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setDriveTargetPowers(0.40);
        marv.setDriveTargetPositions(ticksY, ticksX);

        while (opModeIsActive() && marv.encodersAreBusy()) {}

        marv.setDriveTargetPowers(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void goAGlyphing() {
        double ticksX = rhp.zeroX + (0 * ticksPerUnit);
        double ticksY = rhp.zeroY + (15 * ticksPerUnit);

        marv.setEncoderBehavior(RUN_TO_POSITION);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setDriveTargetPowers(0.80);
        marv.setDriveTargetPositions(ticksY, ticksX);

        while (opModeIsActive() && marv.encodersAreBusy()) {}

        marv.setDriveTargetPowers(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void goABashing() {
        double ticksX = rhp.zeroX + (15 * ticksPerUnit);
        double ticksY = rhp.zeroY + (25 * ticksPerUnit);

        marv.setEncoderBehavior(RUN_TO_POSITION);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setDriveTargetPowers(0.80);
        marv.setDriveTargetPositions(ticksY, ticksX);

        while (opModeIsActive() && marv.encodersAreBusy()) {}

        marv.setDriveTargetPowers(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void bashPile() {
        //marv.setGatesPosition(1);
        marv.collect(0.5);
        marv.setEncoderBehavior(RUN_WITHOUT_ENCODER);
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        long time = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() < time + 1000) {
            try{Thread.sleep(1);marv.drive(0.6, 0.6, -0.6);} catch (Exception e) {}
        }
        time = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() < time + 1000) {
            try{Thread.sleep(1);marv.drive(-0.25, -0.25, 0.25);} catch (Exception e) {}
        }
        marv.drive(0, 0, 0);
        marv.collect(0);
        marv.setGatesPosition(0);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }


}
