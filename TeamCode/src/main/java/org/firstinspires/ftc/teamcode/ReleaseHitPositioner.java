package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.MarvNavConstants.ticksPerUnit;

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
        double xvalSubLeft = ((double)(fl.getCurrentPosition()) / ticksPerUnit) - ((double)(bl.getCurrentPosition()) / ticksPerUnit);
        double xvalSubRight = ((double)(br.getCurrentPosition()) / ticksPerUnit) - ((double)(fr.getCurrentPosition()) / ticksPerUnit);

        double xval = (xvalSubLeft + xvalSubRight) / 4.0;

        return xval;
    }


    void computeRobotPos() {
        computedRobotX = (edgeLX + edgeRX) / 2.0;
        computedRobotY = Math.abs(edgeRX - edgeLX) * DISTANCE_PER_UNIT;
    }

}
