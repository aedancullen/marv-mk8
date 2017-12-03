package org.firstinspires.ftc.teamcode;

import com.evolutionftc.autopilot.AutopilotSegment;
import com.evolutionftc.autopilot.AutopilotSystem;

/**
 * Created by aedan on 12/3/17.
 */

public class MarvMk8CAutopilotSystemCommon extends AutopilotSystem {

    public MarvMk8CCommon marv;

    public void setMarvCommon(MarvMk8CCommon marv){
        this.marv = marv;
    }

    public void onSegmentTransition(AutopilotSegment previous, AutopilotSegment next, boolean wasOkayToContinue) {

    }

}
