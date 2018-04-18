package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by aedan on 3/7/18.
 */

@Autonomous(name = "RHP Auto Red A NO EXTRAS")
public class RHPAutoRedANoExtras extends RHPAutoCommon {

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = true;
        marv.isOnBSide = false;
        marv.isDoingExtras = false;

        super.runOpMode();
    }

}