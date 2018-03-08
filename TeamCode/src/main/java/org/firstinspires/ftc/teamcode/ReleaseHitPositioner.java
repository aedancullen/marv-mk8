package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.MarvNavConstants.ticksPerUnit;


public class ReleaseHitPositioner {

    ColorSensor rhpc;

    double edgeLX;
    double edgeRX;

    double zeroY;
    double zeroX;


    double TICKS_VERT_PER_WIDTH = -(ticksPerUnit * 14.0) / 18.25;

    private boolean rhpcHasLine() {
        return Math.abs(rhpc.red() - rhpc.blue()) > 5;
    }

    public void blockUntilRelease() {
        blockUntilHit();
        while (rhpcHasLine()) {}
    }

    public void blockUntilHit() {
        while (!rhpcHasLine()) {}
    }

    public void recordEdgeLXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeLX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    public void recordEdgeRXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeRX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    private double encoderDecomposeMecX(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {

        int ticksFl = fl.getCurrentPosition();
        int ticksFr = fr.getCurrentPosition();
        int ticksBl = bl.getCurrentPosition();
        int ticksBr = br.getCurrentPosition();

        double xvalSubLeft = ((double)(ticksFl)) - ((double)(ticksBl));
        double xvalSubRight = ((double)(ticksBr)) - ((double)(ticksFr));

        double xval = (xvalSubLeft + xvalSubRight) / 4.0;

        return xval - zeroX;
    }

    private double encoderDecomposeMecY(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {

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


    public void compute() {
        zeroX = (edgeRX + edgeLX) / 2.0;
        zeroY = Math.abs(edgeRX - edgeLX) * TICKS_VERT_PER_WIDTH;
    }


    public void resetZeros() {
        zeroX = 0;
        zeroY = 0;
    }

    public double[] driveToPos(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, double inchesX, double inchesY, double inchesTol, double power) {
        double ticksX = inchesX * ticksPerUnit;
        double ticksY = inchesY * ticksPerUnit;

        double powerX = 0;
        double powerY = 0;

        if (Math.abs(ticksX - encoderDecomposeMecX(fl, fr, bl, br)) > ticksPerUnit * inchesTol) {
            if (ticksX - encoderDecomposeMecX(fl, fr, bl, br) > 0) {
                powerX = power;
            }
            else {
                powerX = -power;
            }
        }
        if (Math.abs(ticksY - encoderDecomposeMecY(fl, fr, bl, br)) > ticksPerUnit * inchesTol) {
            if (ticksY - encoderDecomposeMecY(fl, fr, bl, br) > 0) {
                powerY = power;
            }
            else{
                powerY = -power;
            }
        }

        return new double[] {powerX, powerY};
    }

    public boolean posHasBeenReached(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, double inchesX, double inchesY, double inchesTol) {
        double ticksX = inchesX * ticksPerUnit;
        double ticksY = inchesY * ticksPerUnit;

        return (Math.abs(ticksX - encoderDecomposeMecX(fl, fr, bl, br)) < ticksPerUnit * inchesTol)
                &&
                (Math.abs(ticksY - encoderDecomposeMecY(fl, fr, bl, br)) < ticksPerUnit * inchesTol);
    }

}
