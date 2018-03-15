package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.MarvNavConstants.ticksPerUnit;


public class ReleaseHitPositioner {

    ColorSensor rhpc;

    double edgeLX;
    double edgeRX;

    double zeroY;
    double zeroX;

    public ReleaseHitPositioner(ColorSensor rhpc) {
        this.rhpc = rhpc;
    }

    double TICKS_VERT_PER_WIDTH = 14.0 / 18.25;

    public boolean rhpcHasLine() {
        return Math.abs(rhpc.red() - rhpc.blue()) > 5;
    }

    public void blockUntilRelease(LinearOpMode mode, Runnable task) {
        blockUntilHit(mode, task);
        while (rhpcHasLine() && mode.opModeIsActive()) {task.run();}
    }

    public void blockUntilHit(LinearOpMode mode, Runnable task) {
        while (rhpcHasLine() && mode.opModeIsActive()) {task.run();}
        while (!rhpcHasLine() && mode.opModeIsActive()) {task.run();}
    }

    public void recordEdgeLXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeLX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    public void recordEdgeRXMec(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        edgeRX = encoderDecomposeMecX(fl, fr, bl, br);
    }

    public double encoderDecomposeMecX(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {

        int ticksFl = fl.getCurrentPosition();
        int ticksFr = fr.getCurrentPosition();
        int ticksBl = bl.getCurrentPosition();
        int ticksBr = br.getCurrentPosition();

        double xvalSubLeft = ((double)(ticksFl)) - ((double)(ticksBl));
        double xvalSubRight = ((double)(ticksBr)) - ((double)(ticksFr));

        double xval = (xvalSubLeft + xvalSubRight) / 4.0;

        return xval - zeroX;
    }

    public double encoderDecomposeMecRot(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        int ticksFl = fl.getCurrentPosition();
        int ticksFr = fr.getCurrentPosition();
        int ticksBl = bl.getCurrentPosition();
        int ticksBr = br.getCurrentPosition();

        double rotation = (((double)ticksFl) + ((double)ticksBl)) - (((double)ticksFr) + ((double)ticksBr));

        return rotation / 4.0;
    }

    public double encoderDecomposeMecY(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {

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


    public void compute(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br) {
        zeroX = (edgeRX + edgeLX) / 2.0;
        double encoderLocY = encoderDecomposeMecY(fl, fr, bl, br);
        zeroY = encoderLocY + Math.abs(edgeRX - edgeLX) * TICKS_VERT_PER_WIDTH;
    }


    public void resetZeros() {
        zeroX = 0;
        zeroY = 0;
    }

    public boolean xHasFinished;
    public boolean yHasFinished;

    public void resetFinishedFlags() {
        xHasFinished = false;
        yHasFinished = false;
    }


    public double[] driveToPos(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, double inchesX, double inchesY, double inchesTol, double power) {
        double ticksX = inchesX * ticksPerUnit;
        double ticksY = inchesY * ticksPerUnit;

        double powerX = 0;
        double powerY = 0;

        if (!xHasFinished && Math.abs(ticksX - encoderDecomposeMecX(fl, fr, bl, br)) > ticksPerUnit * inchesTol) {
            if (ticksX - encoderDecomposeMecX(fl, fr, bl, br) > 0) {
                powerX = power;
            }
            else {
                powerX = -power;
            }
        }
        else {
            xHasFinished = true;
        }
        if (!yHasFinished && Math.abs(ticksY - encoderDecomposeMecY(fl, fr, bl, br)) > ticksPerUnit * inchesTol) {
            if (ticksY - encoderDecomposeMecY(fl, fr, bl, br) > 0) {
                powerY = power;
            }
            else{
                powerY = -power;
            }
        }
        else {
            yHasFinished = true;
        }

        return new double[] {powerX, powerY};
    }

    public boolean posHasBeenReached() {

        return (xHasFinished && yHasFinished);
    }

}
