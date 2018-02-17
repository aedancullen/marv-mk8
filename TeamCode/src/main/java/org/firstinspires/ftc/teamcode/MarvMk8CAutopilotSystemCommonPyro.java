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

    public MarvMk8CAutopilotSystemCommonPyro(LinearOpMode mode, AutopilotTracker tracker, Telemetry telemetry, Context appContext){
        super(tracker, telemetry, appContext);
        this.mode = mode;
    }

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
        marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        marv.setEncoderBehavior(RUN_USING_ENCODER);
    }

    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

        if (next != null && next.id.equals("__start__")){



        }
        
        if (next != null && next.id.toLowerCase().contains("tokey")) {
            // modify coords of next accordingly
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("parkL"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("parkR"));
            }
            else {
                next.populateFromOther(this.pathFollower.getSegment("parkC"));
            }
        }
        
        if (next != null && next.id.toLowerCase().contains("toadd1")) {
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go center
                next.populateFromOther(this.pathFollower.getSegment("parkC"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("parkL"));
            }
            else {
                // go left
                next.populateFromOther(this.pathFollower.getSegment("parkL"));
            }
        }
        
        if (next != null && next.id.toLowerCase().contains("toadd2")) {
            if (detectedTrashMark == RelicRecoveryVuMark.LEFT) {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("parkR"));
            }
            else if (detectedTrashMark == RelicRecoveryVuMark.RIGHT) {
                // go center
                next.populateFromOther(this.pathFollower.getSegment("parkC"));
            }
            else {
                // go right
                next.populateFromOther(this.pathFollower.getSegment("parkR"));
            }
        }
        
        if (previous != null && previous.id.toLowerCase().contains("flip")){
            marv.drive(0, 0, 0);

            marv.setFlippoPos(1);
            long time = System.currentTimeMillis();
            while (mode.opModeIsActive() && System.currentTimeMillis() < time + 2000) {
                try{Thread.sleep(1);} catch (Exception e) {}
            }
            marv.setFlippoPos(0);

        }
        if (next != null && next.id.toLowerCase().contains("collectstart")) {
            marv.autoCollectTick(0.5);
            marv.autoConveyTick(1);
        }
        if (previous != null && previous.id.toLowerCase().contains("collectend")) {
            marv.autoCollectTick(0);
            marv.autoConveyTick(0);
        }
    }

}
