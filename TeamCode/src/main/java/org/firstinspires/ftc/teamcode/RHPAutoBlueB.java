package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by aedan on 3/7/18.
 */

@Autonomous(name = "RHP Auto Blue B")
public class RHPAutoBlueB extends RHPAutoCommon {

    public void runOpMode() {
        marv = new MarvMk8CCommon(hardwareMap);
        marv.isOnRedSide = false;
        marv.isOnBSide = true;

        super.runOpMode();
    }

}
