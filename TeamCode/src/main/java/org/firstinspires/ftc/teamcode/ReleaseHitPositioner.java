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

    double zeroY;
    double zeroX;


    double TICKS_PER_WIDTH = (ticksPerUnit * 14.0) / 18.25;

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

        int ticksFl = fl.getCurrentPosition();
        int ticksFr = fr.getCurrentPosition();
        int ticksBl = bl.getCurrentPosition();
        int ticksBr = br.getCurrentPosition();

        double xvalSubLeft = ((double)(ticksFl)) - ((double)(ticksBl));
        double xvalSubRight = ((double)(ticksBr)) - ((double)(ticksFr));

        double xval = (xvalSubLeft + xvalSubRight) / 4.0;

        return xval - zeroX;
    }

    double encoderDecomposeMecY(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {

        int ticksFl = fl.getCurrentPosition();
        int ticksFr = fr.getCurrentPosition();
        int ticksBl = bl.getCurrentPosition();
        int ticksBr = br.getCurrentPosition();

        double yval = (
                ((double)(ticksFl)) + ((double)(ticksFr))
                        + ((double)(ticksBl)) + ((double)(ticksBr))
        ) / 4.0;

        return yval - zeroY;
    }


    void computeRobotPosAtRX() {
        zeroX = edgeRX;
        zeroY = Math.abs(edgeRX - edgeLX) * TICKS_PER_WIDTH;
    }

    void computeRobotPosAtLX() {
        zeroX = edgeLX;
        zeroY = Math.abs(edgeRX - edgeLX) * TICKS_PER_WIDTH;
    }

    void resetZeros() {
        zeroX = 0;
        zeroY = 0;
    }

    double[] driveToPos(double inchesX, double inchesY) {
        double ticksX = inchesX * ticksPerUnit;
        double ticksY = inchesY * ticksPerUnit;

        return new double[] {0,0};
    }

}
