package com.clinic.app;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DashboardPage extends StackPane {

    public DashboardPage() {
        this.setStyle("-fx-background-color:#f5f5f5;");
        loadDashboardPage();
    }

    private void loadDashboardPage() {
        VBox dashboard = new VBox();
        dashboard.setPadding(new Insets(20));
        Text t = new Text("DASHBOARD PAGE");
        t.setFont(Font.font(30));
        dashboard.getChildren().add(t);
        this.getChildren().setAll(dashboard);
    }
}
