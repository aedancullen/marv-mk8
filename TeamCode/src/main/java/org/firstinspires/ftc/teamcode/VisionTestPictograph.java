package org.firstinspires.ftc.teamcode;

import com.evolutionftc.visionface.CameraSpec;
import com.evolutionftc.visionface.ObjectSpec;
import com.evolutionftc.visionface.VisionProcessor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by aedan on 5/27/17.
 */

//@Autonomous(name="VisionTest - Pictograph")
public class VisionTestPictograph extends OpMode {

    VisionProcessor processor;

    Mat picto1;
    Mat picto2;
    Mat picto3;
    Mat pictomask;
    Mat pictomask_neg;

    public void init() {
        CameraSpec zteSpeedCameraLandscape = new CameraSpec(0.9799, Math.PI / 2);
        processor = new VisionProcessor(hardwareMap.appContext, zteSpeedCameraLandscape);

        try {
            picto1 = Utils.loadResource(hardwareMap.appContext, R.drawable.picto1);
            picto2 = Utils.loadResource(hardwareMap.appContext, R.drawable.picto2);
            picto3 = Utils.loadResource(hardwareMap.appContext, R.drawable.picto3);
            Mat pictomask_grey = Utils.loadResource(hardwareMap.appContext, R.drawable.pictomask);
            Imgproc.cvtColor(picto1, picto1, Imgproc.COLOR_BGRA2BGR);
            Imgproc.cvtColor(picto2, picto2, Imgproc.COLOR_BGRA2BGR);
            Imgproc.cvtColor(picto3, picto3, Imgproc.COLOR_BGRA2BGR);
            Imgproc.cvtColor(pictomask_grey, pictomask_grey, Imgproc.COLOR_BGRA2BGR);
            pictomask = new Mat(pictomask_grey.size(), CvType.CV_8UC3);
            pictomask_neg = new Mat(pictomask_grey.size(), CvType.CV_8UC3);
            Imgproc.threshold(pictomask_grey, pictomask, 100, 255, Imgproc.THRESH_BINARY);
            Imgproc.threshold(pictomask_grey, pictomask_neg, 100, 255, Imgproc.THRESH_BINARY_INV);
        }
        catch (Exception e){
            throw new UnsupportedOperationException(e);
        }
    }

    public void start() {
        processor.start();
        ObjectSpec beacon = new ObjectSpec(hardwareMap.appContext, "haarcascade_evolution_pictograph_2k3k_18st", 6.1);
        processor.loadObject(beacon);
    }

    public void loop() {

        if (processor.lastFrame != null) {
            Rect[] locs = processor.getRawDetections();
            if (locs == null || locs.length == 0) {
                telemetry.addData("Decision:", "not visible");
                telemetry.update();
                return;
            }

            Mat object = new Mat();
            Mat input = processor.lastFrame.submat(locs[0]);
            Imgproc.cvtColor(input, object, Imgproc.COLOR_RGBA2BGR);

            double fx = (double)object.width() / this.picto2.width()*0.6;
            double fy = (double)object.height() / this.picto2.height()*0.6;

            Mat picto1 = new Mat();
            Mat picto2 = new Mat();
            Mat picto3 = new Mat();
            Mat pictomask = new Mat(this.pictomask.size(), this.pictomask.type());
            Mat pictomask_neg = new Mat(this.pictomask_neg.size(), this.pictomask_neg.type());

            Imgproc.resize(this.picto1, picto1, new Size(), fx, fy, Imgproc.INTER_LINEAR);
            Imgproc.resize(this.picto2, picto2, new Size(), fx, fy, Imgproc.INTER_LINEAR);
            Imgproc.resize(this.picto3, picto3, new Size(), fx, fy, Imgproc.INTER_LINEAR);
            Imgproc.resize(this.pictomask, pictomask, new Size(), fx, fy, Imgproc.INTER_LINEAR);
            Imgproc.resize(this.pictomask_neg, pictomask_neg, new Size(), fx, fy, Imgproc.INTER_LINEAR);

            Mat localizeResult = new Mat();
            Imgproc.matchTemplate(object, picto1, localizeResult, Imgproc.TM_SQDIFF, pictomask_neg);
            Core.MinMaxLocResult locality = Core.minMaxLoc( localizeResult );

            Point tl = new Point(locality.minLoc.x-0.5*picto1.width(), locality.minLoc.y-0.5*picto1.height());
            Point br = new Point(locality.minLoc.x+0.5*picto1.width(), locality.minLoc.y+0.5*picto1.height());
            object = object.submat(new Rect(tl, br));

            telemetry.addData("loca1",locality.minLoc);

            Mat result1 = new Mat();
            Imgproc.matchTemplate(object, picto1, result1, Imgproc.TM_SQDIFF, pictomask);
            Core.MinMaxLocResult minmax1 = Core.minMaxLoc( result1 );

            Mat result2 = new Mat();
            Imgproc.matchTemplate(object, picto2, result2, Imgproc.TM_SQDIFF, pictomask);
            Core.MinMaxLocResult minmax2 = Core.minMaxLoc( result2 );

            Mat result3 = new Mat();
            Imgproc.matchTemplate(object, picto3, result3, Imgproc.TM_SQDIFF, pictomask);
            Core.MinMaxLocResult minmax3 = Core.minMaxLoc( result3 );

            telemetry.addData("loc", locality.minLoc);

            telemetry.addData("1", minmax1.minVal);
            telemetry.addData("2", minmax2.minVal);
            telemetry.addData("3", minmax3.minVal);

            if (minmax1.minVal < minmax2.minVal && minmax1.minVal < minmax3.minVal) {
                telemetry.addData("Decision:", "1");
            }
            else if (minmax2.minVal < minmax1.minVal && minmax2.minVal < minmax3.minVal){
                telemetry.addData("Decision:", "2");
            }
            else if (minmax3.minVal < minmax1.minVal && minmax3.minVal < minmax2.minVal){
                telemetry.addData("Decision:", "3");
            }
            else{
                telemetry.addData("Decision:", "Tie");
            }

        }

        telemetry.update();


    }

    public void stop() {
        processor.stop();
    }
}
