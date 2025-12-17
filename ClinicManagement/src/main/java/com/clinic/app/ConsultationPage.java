package com.clinic.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDateTime;

public class ConsultationPage extends VBox {

    private ListView<ConsultationItem> list;          
    private ObservableList<ConsultationItem> masterList;

    public ConsultationPage() {

        // ROOT STYLE
        this.setPadding(new Insets(0, 0, 0, 0));
        this.setSpacing(0);
        this.setStyle("-fx-background-color: #ffffffff;");

        // =============== TOPBAR ===============
        HBox topbar = new HBox();
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setPadding(new Insets(0, 0, 0, 0));
        topbar.setStyle("-fx-background-color: #EAF6FB;");
        topbar.setMinHeight(70);

        Label title = new Label("CONSULTATION PAGE");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
         title.setPadding(new Insets(0, 0, 0, 60));
        topbar.getChildren().add(title);

        // =============== INNER CONTENT ===============
        VBox container = new VBox();
        container.setPadding(new Insets(25, 40, 30, 40));
        container.setSpacing(25);
        container.setAlignment(Pos.TOP_LEFT);

        // SEARCH + FILTER
        container.getChildren().add(buildSearchRow());

        // STATS ROW
        container.getChildren().add(buildStatsRow());

        // ---------------- List with Header ----------------
        container.getChildren().add(buildListWithHeader());

        this.getChildren().addAll(topbar, container);
    }

    // ---------------- Search + Filter Row ----------------
    private HBox buildSearchRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        // SEARCH BAR CONTAINER
        HBox searchBar = new HBox(8);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(5, 12, 5, 12));
        searchBar.setStyle("""
                -fx-background-color: #D9D9D9;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                """);

        Label searchIcon = new Label("🔍");
        searchIcon.setStyle("-fx-font-size: 14;");

        TextField search = new TextField();
        search.setPromptText("Search");
        search.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        search.setPrefWidth(220);

        // AUTO FILTER WHILE TYPING
        search.textProperty().addListener((obs, oldVal, newVal) -> filterList(newVal));

        searchBar.getChildren().addAll(searchIcon, search);

        // FILTER BUTTON
        Button filterBtn = new Button("FILTER");
        filterBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #00C6FF;
                -fx-font-weight: bold;
                """);

        row.getChildren().addAll(searchBar, filterBtn);
        return row;
    }

    // ---------------- Filter Logic ----------------
    private void filterList(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            list.setItems(masterList);
            return;
        }

        String lower = keyword.toLowerCase();
        ObservableList<ConsultationItem> filtered = FXCollections.observableArrayList();

        for (ConsultationItem item : masterList) {
            if (item.getName().toLowerCase().contains(lower)) {
                filtered.add(item);
            }
        }

        list.setItems(filtered);
    }

    // ---------------- Statistics Row ----------------
    private HBox buildStatsRow() {
        HBox row = new HBox(150);
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(
                statBlock("34", "Total Consultations"),
                statBlock("12", "Upcoming Consultations"),
                statBlock("20", "Completed Consultations"),
                statBlock("2", "Cancelled Consultations")
        );

        return row;
    }

    private VBox statBlock(String num, String label) {
        VBox box = new VBox(2);
        Label l1 = new Label(num);
        l1.setFont(Font.font("Arial", FontWeight.BOLD, 23));

        Label l2 = new Label(label);
        l2.setFont(Font.font(14));
        l2.setStyle("-fx-text-fill: #555;");

        box.getChildren().addAll(l1, l2);
        return box;
    }

    // ----------------  Patient List ----------------
    private VBox buildListWithHeader() {
        VBox container = new VBox(0); 

        // HEADER
        Label header = new Label("Patient");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        header.setStyle("""
                -fx-background-color: #ffffff;
                -fx-text-fill: #00C6FF;
                -fx-padding: 8;
                -fx-border-color: #D9D9D9;
                -fx-border-width: 0 0 1 0;
                """);

        // LIST
        masterList = sampleConsultations();
        list = new ListView<>();
        list.setItems(masterList);
        list.setCellFactory(c -> new ConsultationCell());
        list.setStyle("""
            -fx-background-color: transparent;
            -fx-control-inner-background: transparent;
            -fx-padding: 0;
            -fx-selection-bar: transparent;
            -fx-selection-bar-non-focused: transparent;
        """);

        container.getChildren().addAll(header, list);
        return container;
    }

    // ---------------- Sample Data ----------------
    private ObservableList<ConsultationItem> sampleConsultations() {
        return FXCollections.observableArrayList(
                new ConsultationItem(
                        "Teo Dizon",
                        "Follow-up",
                        LocalDateTime.of(2025, 11, 3, 10, 0),
                        "Discussed test result and treatment plan.",
                        "Completed",
                        "male"
                ),
                new ConsultationItem(
                        "Samantha Jones",
                        "Initial Check-up",
                        LocalDateTime.of(2025, 11, 28, 14, 30),
                        "Patient complains of persistent cough and mild fever. Recommended further testing for respiratory infection.",
                        "Upcoming",
                        "female"
                )
        );
    }
}
