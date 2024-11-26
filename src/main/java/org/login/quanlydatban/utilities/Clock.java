package org.login.quanlydatban.utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock {
    public void startClock(Label newLabel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        newLabel.setText(LocalDateTime.now().format(formatter));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            newLabel.setText(LocalDateTime.now().format(formatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
