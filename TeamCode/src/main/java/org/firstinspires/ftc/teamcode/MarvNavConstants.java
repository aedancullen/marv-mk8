package org.firstinspires.ftc.teamcode;

/**
 * Created by aedan on 12/24/17.
 */

public class MarvNavConstants {

    public static double MbXOffset = 7;

    public static double MbYOffset = 2;

    public static double CryptXOffset = 7.5;
    public static double CryptXOffsetBlue = CryptXOffset; // set appropriately!
    
    // x axis always reversed on red side (no matter which crypto A/B)
    public static double CryptXOffsetRed =  -CryptXOffsetBlue;


    public static double CryptYOffset = -11.75;

    public static int ticksPerUnit = 42;

    public static int nSubsteps = 10;


    // ReleaseHit-specific stuff

    public static double RHPTolerance = 1.0;

}
