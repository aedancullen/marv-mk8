package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by aedan on 12/2/17.
 */

@TeleOp(name="Marv-Mk8C User PYRO")
public class MarvMk8CUserPyro  extends OpMode {

    MarvMk8CCommon marv;

    double collectspeed = 0.32;
    double collectdiff = 0.0;

    double lastLiftPos = 0;
    double lastGrabPos = 0;

    public void init() {
        marv = new MarvMk8CCommon(hardwareMap);
        //marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void start() {
        marv.setLiftPos(0.17);
        marv.setKickskiCenter();
    }

    public void wheelDriveTick() {

        if (!gamepad1.left_bumper) {
            double horiz;
            horiz = (gamepad1.right_trigger / 1.50) - (gamepad1.left_trigger / 1.50);
            marv.drive(-gamepad1.left_stick_y / 1.25, -gamepad1.right_stick_y / 1.25, horiz);
        }
        else {
            double horiz;
            horiz = (gamepad1.right_trigger / 2.25) - (gamepad1.left_trigger / 2.25);
            marv.drive(-gamepad1.left_stick_y / 2.50, -gamepad1.right_stick_y / 2.50, horiz);
        }
    }

    public void loop() {
        marv.setDropskiUp();
        marv.setKickskiCenter();

        telemetry.update();
        wheelDriveTick();

        if (gamepad1.right_bumper) {
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        else{
            marv.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }


        if (!gamepad2.back) {

            if (gamepad2.left_bumper) {
                marv.convey(-1);
            } else if (gamepad2.right_bumper) {
                marv.convey(0);
            } else {
                marv.autoConveyTick(1);
            }

            marv.setGatesPosition(gamepad2.left_trigger);

            if (marv.winchLevel > 0) {
                marv.setFlippoPos(gamepad2.right_trigger);
            }
            else {
                marv.setFlippoPos(0);
            }


            if (gamepad2.dpad_down) {
                marv.collect(0);
            } else if (gamepad2.dpad_left) {
                marv.counterL(collectspeed);
            } else if (gamepad2.dpad_right) {
                marv.counterR(collectspeed);
            } else if (gamepad2.dpad_up) {
                marv.collect(-collectspeed);
            } else {
                //marv.autoCollectTick(collectspeed);
                if (!marv.liftIsRaised()) {
                    marv.collectorR.setPower(collectspeed + collectdiff / 2.0);
                    marv.collectorL.setPower(-collectspeed + collectdiff / 2.0);
                }
                else {
                    marv.collect(0);
                }
            }


            if (gamepad2.a) {
                marv.setWinchLevel(0);
            } else if (gamepad2.b) {
                marv.setWinchLevel(1);
            } else if (gamepad2.x) {
                marv.setWinchLevel(2);
            } else if (gamepad2.y) {
                marv.setWinchLevel(3);
            }

        }

        else /*if (gamepad2.back)*/ {
            marv.collect(0);

            if (gamepad2.x) {
                if (lastGrabPos < 1) {
                    lastGrabPos += 0.02;
                }
                marv.setGrabPos(lastGrabPos);
            }
            else if (gamepad2.b) {
                if (lastGrabPos > 0) {
                    lastGrabPos -= 0.02;
                }
                marv.setGrabPos(lastGrabPos);
            }
            else {
                marv.setSlideSpeed(0, false);
            }
            if (gamepad2.y) {
                if (lastLiftPos < 1) {
                    lastLiftPos += 0.01;
                }
                marv.setLiftPos(lastLiftPos);
            }
            else if (gamepad2.a) {
                if (lastLiftPos > 0) {
                    lastLiftPos -= 0.01;
                }
                marv.setLiftPos(lastLiftPos);
            }

            boolean overrideIsEnabled = gamepad2.dpad_up || gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_right;

            if (gamepad2.right_trigger > 0) {
                marv.setSlideSpeed(gamepad2.right_trigger, overrideIsEnabled);
            }
            else if (gamepad2.right_bumper) {
                marv.setSlideSpeed(-1, overrideIsEnabled);
            }

            if (gamepad2.left_trigger > 0.75) {
                if (lastGrabPos > 0.25) {
                    lastLiftPos = 0.5;
                }
                else {
                    lastLiftPos = 0.56;
                }
                marv.setLiftPos(lastLiftPos);
            }
            else if (gamepad2.left_bumper) {
                lastLiftPos = 1;
                marv.setLiftPos(lastLiftPos);
            }

            telemetry.addData("slide pos", marv.relicSlide.getCurrentPosition());
            telemetry.addData("lift pos", lastLiftPos);
            telemetry.addData("grab pos", lastGrabPos);

            telemetry.update();
        }

        marv.winchTick();


    }




}
