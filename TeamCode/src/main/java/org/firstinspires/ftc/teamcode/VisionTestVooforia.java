package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Bob on 1/22/2018.
 */
@Autonomous(name="Voofora Garbage")
public class VisionTestVooforia extends OpMode {

    VuforiaLocalizer vooforia;
    VuforiaTrackables garbage;
    VuforiaTrackable rubbish;



    public void init() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AQaSz0//////AAAAmWK7mLvLWUvPmqojdrAEgctDNGkihSVvah2Cd2y7iGo8Bg6dQUJLVoguaAqG4QYpI/87Ccb0wO4nd+jcVrzX8tF8rS4UPhr3bXkKHYtqwUjlpSvKKJzSsFGIe+MGpmmSK824Ja7JVikVoJO/u5ubCkYjm9Fyi+87T2qdjS/+RdNELgLJSDVS3Hp3nbCII6JGutHNROuLOclZCFARI1djpNJu6YNzlvCr+AJQd4Q+i0ZYv378aWnasQYifKGA8KafQMLMmNZmghljMNPDnlfFqZmn4BhItnyrBS1dbXG7BnU7xOw8DIIRq0VjEzSMiikBaUxWXxQn+K+KWahXDwchjL193WpriOF8ovjcGbeGnzJU";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vooforia = ClassFactory.createVuforiaLocalizer(parameters);

        garbage = this.vooforia.loadTrackablesFromAsset("RelicVuMark");
        rubbish = garbage.get(0);

        garbage.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(rubbish);
    }

    /*public void init_loop() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(rubbish);
    }*/

    public void start() {
        garbage.activate();
    }

    public void loop() {

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(rubbish);

        telemetry.addData("Vooforia Garbage", vuMark);

    }

    public void stop() {
        garbage.deactivate();
    }
}
