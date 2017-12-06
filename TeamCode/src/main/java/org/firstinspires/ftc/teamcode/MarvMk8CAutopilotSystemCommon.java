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
        
        if (next.id.equals("__start__")){
            // Do vision sensing, drive off balancing stone and set angle hold
        }
        else if (previous.id.equals("approach_crypto_a")){
            // Approach and glyph ejection routine
            // Can use 
            //
            // this.host.setNavigationTarget(AutopilotSegment);
            // this.host.setNavigationStatus(NavigationStatus.RUNNING);
            // while (this.host.getNavigationStatus == NavigationStatus.RUNNING) {
            //      this.host.communicate(this.tracker);
            //      double[2] power = this.host.navigationTickRaw();
            //      // marv.drive stuff
            // }
            // // marv.drive stop the robot
            //
            // to get to arbitrary coords for glyph ejection
            // Then can drop glyph and proceed
            // The beauty of evolutionftc.autopilot is that we don't have to care which alliance we're on;
            // just use the coordinate system
        }
        else if (previous.id.equals("approach_crypto_b")){
            // Same as above - but the approach coordinates ARE different if we're doing the "B" cryptobox!
        }
    }

}
