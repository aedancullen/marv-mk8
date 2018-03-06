package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Bob on 3/6/2018.
 */

public class ReleaseHitPositioner {

    ColorSensor rhpc;

    double edgeLX;
    double edgeRX;

    double computedRobotY;
    double computedRobotX;

    double targetY;
    double targetX;

    boolean rhpcHasLine() {
        return Math.abs(rhpc.red() - rhpc.blue()) > 6;
    }

    void blockUntilRelease() {
        blockUntilHit();
        while (rhpcHasLine()) {}
    }

    void blockUntilHit() {
        while (!rhpcHasLine()) {}
    }

    void recordEdgeLXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeLX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    void recordEdgeRXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeRX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    double encoderDecomposeMecX(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        double xvalSubLeft = ((double)(fl.getCurrentPosition())) - ((double)(bl.getCurrentPosition()));
        double xvalSubRight = ((double)(br.getCurrentPosition())) - ((double)(fr.getCurrentPosition()));

        double xval = (xvalSubLeft + xvalSubRight) / 4.0;

        return xval;
    }

    double computeRobotPos() {
        computedRobotX = (edgeLX + edgeRX) / 2.0;
        computedRobotY = Math.abs(edgeRX - edgeLX);
        return 0;
    }

}
