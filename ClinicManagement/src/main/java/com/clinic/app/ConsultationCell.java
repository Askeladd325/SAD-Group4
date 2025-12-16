package com.clinic.app;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;

public class ConsultationCell extends ListCell<ConsultationItem> {

    private final HBox root = new HBox(15); 
    private final HBox leftBox = new HBox(10); 
    private final VBox nameTypeBox = new VBox(2); 
    private final VBox centerBox = new VBox(5);
    private final VBox rightBox = new VBox(5);

    private final ImageView avatarView = new ImageView();
    private final Label nameLabel = new Label();
    private final Label typeLabel = new Label();
    private final Label dateLabel = new Label();
    private final Label notesLabel = new Label();
    private final Label statusLabel = new Label();

    public ConsultationCell() {
        super();

        // Root HBox style
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(10, 10, 10, 0)); 

        

        // Avatar setup
        avatarView.setFitWidth(40);
        avatarView.setFitHeight(40);
        avatarView.setPreserveRatio(true);
        avatarView.setClip(new javafx.scene.shape.Circle(20, 20, 20));

        // Left box: avatar + name/type
        nameTypeBox.getChildren().addAll(nameLabel, typeLabel);
        nameTypeBox.setAlignment(Pos.CENTER_LEFT);

        leftBox.getChildren().addAll(avatarView, nameTypeBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // Center box: date + notes
        centerBox.getChildren().addAll(dateLabel, notesLabel);
        centerBox.setAlignment(Pos.CENTER_LEFT);

        notesLabel.setWrapText(true);
        notesLabel.setMaxWidth(400);

        // Right box: status
        rightBox.getChildren().add(statusLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(centerBox, Priority.ALWAYS);

        root.getChildren().addAll(leftBox, centerBox, rightBox);
    }

    @Override
    protected void updateItem(ConsultationItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        // Avatar
        if ("female".equalsIgnoreCase(item.getGender())) {
            avatarView.setImage(new Image(getClass().getResourceAsStream("/images/female.png")));
        } else {
            avatarView.setImage(new Image(getClass().getResourceAsStream("/images/male.png")));
        }

        // Name
        nameLabel.setText(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #000; -fx-font-size: 14;");

        // Type & Status
        typeLabel.setText(item.getType());
        statusLabel.setText(item.getStatus());

        animateBadgeBackground(typeLabel, isSelected());
        animateBadgeBackground(statusLabel, isSelected());

        // Date & Notes
        dateLabel.setText(item.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));
        dateLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #333;");

        notesLabel.setText(item.getNotes());
        notesLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #555;");

        centerBox.setAlignment(Pos.CENTER);

        // Add status back to rightBox if missing
        if (!root.getChildren().contains(rightBox)) {
            rightBox.getChildren().clear();
            rightBox.getChildren().add(statusLabel);
            root.getChildren().add(rightBox);
        }

        // Set graphic
        setGraphic(root);

        // Update root background based on selection
        updateRootBackground(isSelected());

        setMinHeight(80);
    }

    // -------------------------
    // ANIMATION HELPER
    // -------------------------
    private void animateBadgeBackground(Label badge, boolean selected) {
        String startColor = selected ? "#F0F8FF" : "#86C5D8";
        String endColor = selected ? "#86C5D8" : "#F0F8FF";

        badge.setStyle("-fx-background-color: " + startColor + ";" +
                       "-fx-text-fill: #000;" +
                       "-fx-padding: 2 8;" +
                       "-fx-font-weight: bold;" +
                       "-fx-font-size: 13;");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(badge.backgroundProperty(),
                                new Background(new BackgroundFill(Color.web(startColor), new CornerRadii(0), Insets.EMPTY)))),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(badge.backgroundProperty(),
                                new Background(new BackgroundFill(Color.web(endColor), new CornerRadii(0), Insets.EMPTY))))
        );
        timeline.play();
    }

    // -------------------------
    // ROOT BACKGROUND HELPER
    // -------------------------
    private void updateRootBackground(boolean selected) {
        if (selected) {
            root.setStyle("-fx-background-color: #F0F8FF;");
        } else {
            root.setStyle("-fx-background-color: white;");
        }
    }

}
