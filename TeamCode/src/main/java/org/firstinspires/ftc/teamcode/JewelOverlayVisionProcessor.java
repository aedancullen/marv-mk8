package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.VisionProcessor;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.FILLED;
import static org.opencv.core.Core.mean;

/**
 * Created by aedan on 5/27/17.
 */


class JewelOverlayVisionProcessor extends VisionProcessor {

    public JewelOverlayVisionProcessor(Context context, CameraSpec camera) {
        super(context, camera);
    }

    private boolean isRedBlueInThatOrder; // descriptive name much
    private boolean isConfident;

    public boolean getIsRedBlueInThatOrder() {
        return isRedBlueInThatOrder; // java ridiculousness at its finest
    }

    public boolean getIsConfident() {
        return isConfident;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat output = super.onCameraFrame(inputFrame);

        // now we goof it up with actually fun thingies

        if (getIsSeen()) {
            Rect loc = getRawDetections()[0];

            Rect locLeft = new Rect(new Point(loc.tl().x, loc.tl().y), new Point(loc.br().x - (loc.width / 2.0), loc.br().y));
            Rect locRight = new Rect(new Point(loc.tl().x + (loc.width / 2.0), loc.tl().y), new Point(loc.br().x, loc.br().y));

            Scalar avgLeft;
            Scalar avgRight;

            try {
                avgLeft = mean(lastFrame.submat(locLeft));
                avgRight = mean(lastFrame.submat(locRight));
            }
            catch (CvException e) {
                // locLeft and/or locRight overlap frame border; ignore
                isConfident = false;
                return output;
            }

            double rLeft = avgLeft.val[0];
            double rRight = avgRight.val[0];
            double bLeft = avgLeft.val[2];
            double bRight = avgRight.val[2];

            if (rLeft > rRight && bLeft < bRight) {
                isRedBlueInThatOrder = true;
                isConfident = true;
                Imgproc.rectangle(output, locLeft.tl(), locLeft.br(), new Scalar(255, 0, 0, 128), FILLED);
                Imgproc.rectangle(output, locRight.tl(), locRight.br(), new Scalar(0, 0, 255, 128), FILLED);
            }
            else if (rLeft < rRight && bLeft > bRight){
                isRedBlueInThatOrder = false;
                isConfident = true;
                Imgproc.rectangle(output, locLeft.tl(), locLeft.br(), new Scalar(0, 0, 255, 128), FILLED);
                Imgproc.rectangle(output, locRight.tl(), locRight.br(), new Scalar(255, 0, 0, 128), FILLED);
            }
            else {
                isConfident = false;
                Imgproc.rectangle(output, loc.tl(), loc.br(), new Scalar(255, 255, 0, 128), FILLED);
            }
        }
        else {
            // as the ftc_app devs would say, "PARANOIA!!!!!!"
            isConfident = false;
        }

        return output;

    }

}
