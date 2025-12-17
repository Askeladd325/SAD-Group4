package com.clinic.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ClinicAppFX extends Application {

    private final double collapsedWidth = 60;
    private final double expandedWidth = 230;

    private BorderPane root;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Clinic Management System");

        root = new BorderPane();

        VBox sidebar = buildSidebar();
        root.setLeft(sidebar);

        // DEFAULT PAGE
        root.setCenter(new DashboardPage());

        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   private VBox buildSidebar() {
    VBox sidebar = new VBox(20); // Spacing between elements is 20
    sidebar.setPadding(new Insets(20));
    sidebar.setPrefWidth(collapsedWidth);
    sidebar.setStyle("-fx-background-color: #AEDCEB;");

    // --- 1. Doctor Profile Section ---

    // Profile photo setup (same as before)
    Image img;
    try {
        img = new Image(getClass().getResourceAsStream("/images/doctor.png"));
    } catch (Exception e) {
        System.err.println("Profile image not found!");
        img = null;
    }

    ImageView doctorImageView = new ImageView(img);
    doctorImageView.setFitWidth(60);
    doctorImageView.setFitHeight(66);
    doctorImageView.setPreserveRatio(true);

    Circle clipCircle = new Circle(30, 30, 30);
    doctorImageView.setClip(clipCircle);

    if (img != null) {
        double cropSize = Math.min(img.getWidth(), img.getHeight());
        doctorImageView.setViewport(new Rectangle2D(
                (img.getWidth() - cropSize) / 2,
                (img.getHeight() - cropSize) / 2,
                cropSize,
                cropSize
        ));
    }

    // Doctor Name Label
    javafx.scene.control.Label doctorNameLabel = new javafx.scene.control.Label("Dr. Jane Doe");
    doctorNameLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #333333;");
    doctorNameLabel.setVisible(false); 
    doctorNameLabel.setManaged(false);

    // VBox to hold image and name
    VBox doctorProfileVBox = new VBox(5);
    doctorProfileVBox.setAlignment(Pos.CENTER);
    doctorProfileVBox.getChildren().addAll(doctorImageView, doctorNameLabel);
    
    VBox.setMargin(doctorProfileVBox, new Insets(0, 0, 30, 0));


    // --- 2. Navigation Buttons ---

    // Buttons (same as before)
    Button dashboardBtn = createSidebarBtn("DASHBOARD", "/images/dashboard.jpg");
    Button patientsBtn = createSidebarBtn("PATIENTS", "/images/patient.png");
    Button consultBtn = createSidebarBtn("CONSULTATION", "/images/consultation.png");
    Button appointmentBtn = createSidebarBtn("APPOINTMENT", "/images/appointment.png");
   
   Region bottomSpacer = new Region();
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    Button exitBtn = new Button();
    exitBtn.setMaxWidth(Double.MAX_VALUE);
    
ImageView backIcon = new ImageView();
try {
    
    backIcon.setImage(new Image(getClass().getResource("/images/back.png").toExternalForm()));
} catch (Exception e) {
    System.err.println("Back icon not found: /images/back.png. Using text fallback.");
    exitBtn.setText("\u2190"); 
}

backIcon.setFitWidth(25);
backIcon.setFitHeight(25);
backIcon.setPreserveRatio(true);

exitBtn.setText("");
exitBtn.setGraphic(backIcon);
exitBtn.setStyle(
    "-fx-background-color: transparent;" +
    "-fx-text-fill: black;" +
    "-fx-font-size: 14px;" +
    "-fx-alignment: center;" 
);


    // --- 3. Hover Expand/Collapse Effect ---

    // Hover expand effect
    sidebar.setOnMouseEntered(e -> {
        sidebar.setPrefWidth(expandedWidth);
        // Show Doctor Name
        doctorNameLabel.setText("Dr. Jane Doe");
        doctorNameLabel.setVisible(true);
        doctorNameLabel.setManaged(true);
        // Show Button Text
        dashboardBtn.setText("DASHBOARD");
        patientsBtn.setText("PATIENTS");
        consultBtn.setText("CONSULATION");
        appointmentBtn.setText("APPOINTMENT");
        exitBtn.setText("");
    });

    sidebar.setOnMouseExited(e -> {
        sidebar.setPrefWidth(collapsedWidth);
        // Hide Doctor Name
        doctorNameLabel.setText(""); 
        doctorNameLabel.setVisible(false);
        doctorNameLabel.setManaged(false);
        // Hide Button Text
        dashboardBtn.setText("");
        patientsBtn.setText("");
        consultBtn.setText("");
        appointmentBtn.setText("");
    
        exitBtn.setText("");
        exitBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14;" +
            "-fx-alignment: center-left;" + 
            "-fx-font-weight: bold;"
        );
    });


    // --- 4. Page Switching (same as before) ---
    dashboardBtn.setOnAction(e -> root.setCenter(new DashboardPage()));
    patientsBtn.setOnAction(e -> root.setCenter(new PatientsPage()));
    consultBtn.setOnAction(e -> root.setCenter(new ConsultationPage()));
    appointmentBtn.setOnAction(e -> root.setCenter(new HBox())); 
    exitBtn.setOnAction(e -> root.setCenter(new DashboardPage()));

    // --- 5. Add all components to the sidebar ---
    sidebar.getChildren().addAll(
            doctorProfileVBox, 
            dashboardBtn,
            patientsBtn,
            consultBtn,
            appointmentBtn,
            bottomSpacer,
            exitBtn
    );

    return sidebar;
}
    private Button createSidebarBtn(String label, String iconPath) {
        ImageView icon = null;
        try {
            icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
            icon = new ImageView(); // fallback empty
        }

        icon.setFitWidth(40);
        icon.setFitHeight(40);
        icon.setSmooth(true);
        icon.setPreserveRatio(true);


        Button btn = new Button(label, icon);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 14;" +
                "-fx-alignment: center;" +
                "-fx-font-weight: bold;"
        );
        // Para sa hover effect ng button
    btn.setOnMouseEntered(e -> btn.setStyle(
        "-fx-background-color: #C0E8F5;" + 
        "-fx-text-fill: #000;" +
        "-fx-font-size: 14;" +
        "-fx-alignment: center-left;" + 
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 5;"
    ));
    btn.setOnMouseExited(e -> btn.setStyle(
        "-fx-background-color: transparent;" +
        "-fx-text-fill: black;" +
        "-fx-font-size: 14;" +
        "-fx-alignment: center-left;" + 
        "-fx-font-weight: bold;"
    ));

        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
