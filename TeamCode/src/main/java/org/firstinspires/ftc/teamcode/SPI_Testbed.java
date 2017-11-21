package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.PWMOutput;


//@Autonomous(name="SPI_Testbed")
public class SPI_Testbed extends LinearOpMode {


    DigitalChannel NCS;
    DigitalChannel MISO;
    DigitalChannel MOSI;
    DigitalChannel SCLK;
    DigitalChannel RST;

    DigitalChannel NCS_VER;
    DigitalChannel MOSI_VER;
    DigitalChannel SCLK_VER;

    PWMOutput clk;


    public void runOpMode() {

        clk = hardwareMap.pwmOutput.get("clk");


        NCS = hardwareMap.digitalChannel.get("NCS");
        NCS.setMode(DigitalChannelController.Mode.OUTPUT);

        MISO = hardwareMap.digitalChannel.get("MISO");
        MISO.setMode(DigitalChannelController.Mode.INPUT);

        MOSI = hardwareMap.digitalChannel.get("MOSI");
        MOSI.setMode(DigitalChannelController.Mode.OUTPUT);

        SCLK = hardwareMap.digitalChannel.get("SCLK");
        SCLK.setMode(DigitalChannelController.Mode.OUTPUT);

        RST = hardwareMap.digitalChannel.get("RST");
        RST.setMode(DigitalChannelController.Mode.OUTPUT);



        NCS_VER = hardwareMap.digitalChannel.get("NCS_VER");
        NCS_VER.setMode(DigitalChannelController.Mode.INPUT);

        MOSI_VER = hardwareMap.digitalChannel.get("MOSI_VER");
        MOSI_VER.setMode(DigitalChannelController.Mode.INPUT);

        SCLK_VER = hardwareMap.digitalChannel.get("SCLK_VER");
        SCLK_VER.setMode(DigitalChannelController.Mode.INPUT);



        telemetry.addData("SPI_Testbed", "Bopper bop bopping bopped, bop BOP to bop MISO/MOSI bopper");
        telemetry.update();

        long start1 = System.currentTimeMillis();
        MOSI.setState(true);
        long elap1 = System.currentTimeMillis() - start1;


        while (MOSI.getState() != true) {}


        long start2 = System.currentTimeMillis();
        MOSI.getState();
        long elap2 = System.currentTimeMillis() - start2;


        long start3 = System.currentTimeMillis();
        long count3 = 0;
        MOSI.setState(false);
        while (MOSI.getState() != false) {count3 += 1;}
        long elap3 = System.currentTimeMillis() - start3;

        telemetry.addData("elapSet", elap1);
        telemetry.addData("elapGet", elap2);
        telemetry.addData("elap3, count3", elap3 + ", " + count3);
        telemetry.addData("clk1, clk2", clk.getPulseWidthPeriod());
        telemetry.update();

        waitForStart();

        telemetry.addData("SPI_Testbed", "Boppin, please bop...");
        telemetry.update();



        long start = System.currentTimeMillis();
        long count = 0;

        while (System.currentTimeMillis() < start + 1000) {

            MOSI.setState(true);
            while (MOSI.getState() != true) {}

            MOSI.setState(false);
            while (MOSI.getState() != false) {}

            count += 2;

        }

        telemetry.addData("SPI_Testbed", "Bop! Bopped " + count + " boppers in 1000ms");
        telemetry.update();

        while (true) {}

    }

}
