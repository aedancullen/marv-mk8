package org.firstinspires.ftc.teamcode;

import android.content.Context;

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

    public MarvMk8CCommon marv;
    LinearOpMode mode;

    public MarvMk8CAutopilotSystemCommonV3(LinearOpMode mode, AutopilotTracker tracker, Telemetry telemetry, Context appContext){
        super(tracker, telemetry, appContext);
        this.mode = mode;
    }

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(STOP_AND_RESET_ENCODER);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void senseAndStartPath() {
        /*if (detectedMark == RelicRecoveryVuMark.RIGHT) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_right");}else {super.beginPathTravel("mk8c_v3_blue_b_right");}
        }
        else if (detectedMark == RelicRecoveryVuMark.CENTER && detectedMark == RelicRecoveryVuMark.UNKNOWN) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_center");}else {super.beginPathTravel("mk8c_v3_blue_b_center");}
        }
        else if (detectedMark == RelicRecoveryVuMark.LEFT) {
            if (marv.isOnRedSide) {super.beginPathTravel("mk8c_v3_red_b_left");}else {super.beginPathTravel("mk8c_v3_blue_b_left");}
        }*/
        super.beginPathTravel("mk8c_v3_blue_a_center");
    }

    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

        if (previous != null && previous.id.startsWith("flip")) {
            marv.drive(0, 0, 0);
            marv.setFlippoPos(1);
            mode.sleep(750);
            marv.setFlippoPos(0);
        }
        else if (next != null && next.id.startsWith("flip")) {
            marv.setConveyGateOpen();
        }
        else if (next != null && next.id.startsWith("collect")) {
            marv.setConveyGateClosed();
        }

    }

}
