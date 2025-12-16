package com.clinic.app;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PatientsPage extends VBox {

    private final PatientDAO patientDAO = new PatientDAO();
    private final ObservableList<Patient> masterList = FXCollections.observableArrayList();
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private final Set<Patient> selected = new HashSet<>();

    private TableView<Patient> table;
    private Label selectedCountLabel;
    private Label totalPatientsLabel; 
    private VBox overviewContent;
    private VBox listContent;
    private VBox inviteContent;
    private FlowPane gridView;
    private ScrollPane gridViewContainer; 
    private HBox toolbarWrapper; 
    private ToggleButton tabInviteBtn;
    private Button notificationBtn;

    public PatientsPage() {
        DatabaseUtil.initializeDatabase();
        
        // ROOT STYLE
        this.setPadding(new Insets(0, 0, 0, 0));
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #ffffffff;");
        // ================= TOPBAR =================
        HBox topbar = new HBox(15);
        topbar.setSpacing(15);
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setPadding(Insets.EMPTY);
        topbar.setStyle("-fx-background-color: #EAF6FB;");
        topbar.setMinHeight(70);
        topbar.setPrefHeight(70);
        topbar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(topbar, Priority.ALWAYS);


        Label title = new Label("PATIENTS MANAGER");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setPadding(new Insets(0, 0, 0, 75));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button inviteBtn = new Button("Invite new patients +");
        inviteBtn.setStyle("-fx-background-color: #86D3DF; -fx-font-weight: bold; -fx-background-radius: 0; -fx-font-size: 15;");
        inviteBtn.setCursor(Cursor.HAND);

        notificationBtn = new Button("🔔");
        notificationBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18;");
        notificationBtn.setCursor(Cursor.HAND);
        setupNotifications();
        Button userBtn = new Button();
        userBtn.setStyle("-fx-background-radius: 30; -fx-background-color: transparent;");
        userBtn.setCursor(Cursor.HAND);
        
        ImageView userImage = new ImageView(getClass().getResource("/images/doctor.png").toExternalForm());
        userImage.setFitWidth(35);
        userImage.setFitHeight(35);
        double size = 35.0;
        double radius = size / 2.0; // 17.5
        Circle clip = new Circle(radius, radius, radius);
        userImage.setClip(clip);
        userBtn.setGraphic(userImage);
        userBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("User Options");
            alert.setContentText("Profile, Settings, Logout...");
            alert.show();
        });

        HBox topButtons = new HBox(5, inviteBtn, userBtn, notificationBtn);
        topButtons.setAlignment(Pos.CENTER_RIGHT);
        topButtons.setPadding(new Insets(0, 30, 0, 0));

        topbar.getChildren().addAll(title, spacer, topButtons);

        // ================= TABS =================
        ToggleButton tabOverviewBtn = new ToggleButton("Patients Overview");
        ToggleButton tabListBtn = new ToggleButton("Patients List");
        tabInviteBtn = new ToggleButton("Invitations");

        ToggleGroup tabGroup = new ToggleGroup();
        tabOverviewBtn.setToggleGroup(tabGroup);
        tabListBtn.setToggleGroup(tabGroup);
        tabInviteBtn.setToggleGroup(tabGroup);
        tabListBtn.setSelected(true);

        String tabDefault = "-fx-padding: 15 30;" +
                "-fx-background-color: #dfeef5;" +
                "-fx-text-fill: #7f8c8d;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 0;";

        String tabActive = "-fx-padding: 15 30;" +
                "-fx-background-color: #86D3DF;" +
                "-fx-text-fill: #084c50;" +
                "-fx-border-width: 2px;" +
                "-fx-border-color: #b5d3df;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 18px;" +
                "-fx-background-radius: 0;";

        Stream.of(tabOverviewBtn, tabListBtn, tabInviteBtn).forEach(tab -> {
            tab.setStyle(tabDefault);
            tabGroup.selectedToggleProperty().addListener((o, oldT, newT) -> {
                tab.setStyle(tab.equals(newT) ? tabActive : tabDefault);
            });
        });

        HBox tabs = new HBox(10, tabOverviewBtn, tabListBtn, tabInviteBtn);
        tabs.setPadding(new Insets(0, 20, 0, 50));
        tabs.setAlignment(Pos.CENTER_LEFT);

        HBox searchBar = buildSearchBar();
        HBox.setHgrow(searchBar, Priority.ALWAYS);

        HBox tabsRow = new HBox(10, tabs, searchBar);
        tabsRow.setAlignment(Pos.CENTER_LEFT);
        tabsRow.setPadding(new Insets(0, 20, 0, 20));

        // ================= CONTENT SETUP =================

        // Toolbar
        HBox toolbar = buildToolbar();

        // totalPatientsLabel 
        totalPatientsLabel = new Label("Total patients: 0"); 
        totalPatientsLabel.setStyle(
    "-fx-font-size: 14px;" + 
    "-fx-font-weight: bold;" +
    "-fx-text-fill: #000000ff;" 
);
        
        toolbarWrapper = new HBox(10, toolbar, totalPatientsLabel);
        toolbarWrapper.setAlignment(Pos.CENTER_LEFT); 
        HBox.setHgrow(toolbar, Priority.ALWAYS); 
        toolbarWrapper.setPadding(new Insets(0, 35, 0, 40)); 
        toolbarWrapper.setVisible(false); 

       
        overviewContent = new VBox();
        overviewContent.getChildren().add(new Label("Overview content goes here..."));

        table = buildTable();
        listContent = new VBox(10, table); 
        listContent.setPadding(new Insets(0, 35, 15, 70));
        VBox.setVgrow(table, Priority.ALWAYS);

        inviteContent = new VBox();
        inviteContent.getChildren().add(new Label("Invitation content goes here..."));

        gridViewContainer = buildGridView();
        gridViewContainer.setPadding(new Insets(10, 35, 20, 50));
        gridViewContainer.setVisible(false); 
        
        StackPane contentPane = new StackPane(overviewContent, listContent, inviteContent, gridViewContainer);

        // 4. View Switching Logic
        ToggleButton listViewBtn = (ToggleButton) toolbar.getChildren().get(0);
        ToggleButton gridViewBtn = (ToggleButton) toolbar.getChildren().get(1);

        listViewBtn.setOnAction(e -> showPatientsView("list", listViewBtn, gridViewBtn));
        gridViewBtn.setOnAction(e -> showPatientsView("grid", listViewBtn, gridViewBtn));
        
        // 5. Tab Switching Logic
        tabOverviewBtn.setOnAction(e -> showTab("overview"));
        tabListBtn.setOnAction(e -> showTab("list"));
        tabInviteBtn.setOnAction(e -> showTab("invite"));

        inviteBtn.setOnAction(e -> {
            tabInviteBtn.setSelected(true);
            showTab("invite");
        });

        // 6. Final Layout Assembly
        // ================= ADD PATIENT BUTTON (FAB) =================
        Label plusCircle = new Label("+");
        plusCircle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        plusCircle.setTextFill(Color.BLACK);
        plusCircle.setAlignment(Pos.CENTER);
        int circleSize = 32;
        plusCircle.setMinSize(circleSize, circleSize);
        plusCircle.setStyle(
                "-fx-background-color: #B4DDED;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 50;");

        Label addText = new Label("ADD");
        addText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        addText.setTextFill(Color.BLACK);

        HBox content = new HBox(8, addText, plusCircle);
        content.setAlignment(Pos.CENTER);
        Button fab = new Button();
        fab.setGraphic(content);
        fab.setStyle("-fx-background-color: #B4DDED; -fx-background-radius: 40;" +
                "-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 40;");
        fab.setPrefSize(120, 50);
        fab.setCursor(Cursor.HAND);
        fab.setOnAction(e -> openAddPatientDialog());

        StackPane stack = new StackPane(contentPane, fab);
        StackPane.setAlignment(fab, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(fab, new Insets(0, 50, 50, 0));
        fab.visibleProperty().bind(tabListBtn.selectedProperty());
        fab.managedProperty().bind(tabListBtn.selectedProperty());

        // ROOT VBox: topbar -> tabsRow -> toolbarWrapper -> stack(views)
        this.getChildren().addAll(topbar, tabsRow, toolbarWrapper, stack);
        VBox.setVgrow(stack, Priority.ALWAYS);

        // Set initial tab after all components are built
        Platform.runLater(() -> showTab("list"));

        // ================= LOAD PATIENTS =================
        loadPatientsFromDatabase();
    }

    // ---------------- Tab switch ----------------
    private void showTab(String tab) {
        overviewContent.setVisible(false);
        listContent.setVisible(false);
        inviteContent.setVisible(false);
        if (gridViewContainer != null) gridViewContainer.setVisible(false); 
        
        // Toolbar visibility logic: Ihiwalay ang toolbar
        toolbarWrapper.setVisible(tab.equals("list")); 

        switch (tab) {
            case "overview" -> overviewContent.setVisible(true);
            case "list" -> {
            
                HBox toolbar = (HBox) toolbarWrapper.getChildren().get(0);
                if (toolbar.getChildren().get(0) instanceof ToggleButton listViewBtn) {
                     listViewBtn.setSelected(true);
                    
                     ToggleButton gridViewBtn = (ToggleButton) toolbar.getChildren().get(1);
                     showPatientsView("list", listViewBtn, gridViewBtn);
                }
            }
            case "invite" -> inviteContent.setVisible(true);
        }
    }

    // ---------------- Patient List/Grid View switch ----------------
    private void showPatientsView(String view, ToggleButton listBtn, ToggleButton gridBtn) {

        String activeStyle = "-fx-background-color:transparent;  -fx-padding: 10;"; 
        String inactiveStyle = "-fx-background-color: transparent;  -fx-border-color: transparent;  -fx-padding: 10;"; 

        if (view.equals("list")) {
            listContent.setVisible(true);
            gridViewContainer.setVisible(false);
            
            // Apply new icon styles
            listBtn.setStyle(activeStyle);
            gridBtn.setStyle(inactiveStyle);
            
        } else if (view.equals("grid")) {
            updateGridView(); 
            listContent.setVisible(false);
            gridViewContainer.setVisible(true);
            
            // Apply new icon styles
            gridBtn.setStyle(activeStyle);
            listBtn.setStyle(inactiveStyle);
        }
    }

    // ---------------- Search bar ----------------
    private HBox buildSearchBar() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setPrefHeight(10);
        searchField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-font-size: 14px;");

        Label icon = new Label("🔍");
        icon.setStyle("-fx-font-size: 12px;");

        HBox searchBox = new HBox(2, searchField, icon);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(2, 12, 2, 12));
        searchBox.setPrefHeight(10);
        searchBox.setMinWidth(10);
        searchBox.setMaxHeight(10);
        HBox.setMargin(searchBox, new Insets(30, 15, 0, 0));
        searchBox.setStyle(
                "-fx-background-color: #dfeef5;" +
                        "-fx-border-color: #eeeeee;" +
                        "-fx-border-width: 1;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;" +
                        "-fx-min-width: 10;");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            ObservableList<Patient> filteredList = masterList.filtered(p -> p.getFirstName().toLowerCase().contains(filter) ||
                    p.getLastName().toLowerCase().contains(filter));
            table.setItems(filteredList);

            if (gridViewContainer.isVisible()) {
                gridView.getChildren().clear();
                filteredList.forEach(p -> gridView.getChildren().add(createPatientCard(p)));
            }
            updateSelectedCountLabel(); 
        });

        HBox wrapper = new HBox(searchBox);
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        return wrapper;
    }


    // ----------------------- Toolbar ------------------------
    private HBox buildToolbar() {
        HBox toolbar = new HBox(5);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 5, 0, 5)); 
        toolbar.setStyle("-fx-font-size: 14px;");

        int iconSizeList = 36; 
        int iconSizetiles = 28;

        // === List View Toggle Button ===
        ImageView listViewIcon = new ImageView(getClass().getResource("/images/listview.png").toExternalForm());
        listViewIcon.setFitWidth(iconSizeList);
        listViewIcon.setFitHeight(iconSizeList);
        
        ToggleButton listViewBtn = new ToggleButton();
        listViewBtn.setGraphic(listViewIcon);
        listViewBtn.setPadding(new Insets(10));
        listViewBtn.setCursor(Cursor.HAND);
        listViewBtn.setSelected(true); 
        
        // === Tile View Toggle Button ===
    
        ImageView tileViewIcon = new ImageView(getClass().getResource("/images/tileview.png").toExternalForm());
        tileViewIcon.setFitWidth(iconSizetiles);
        tileViewIcon.setFitHeight(iconSizetiles);

        ToggleButton gridViewBtn = new ToggleButton();
        gridViewBtn.setGraphic(tileViewIcon);
        gridViewBtn.setPadding(new Insets(10));
        gridViewBtn.setCursor(Cursor.HAND);
        
        ToggleGroup viewGroup = new ToggleGroup();
        listViewBtn.setToggleGroup(viewGroup);
        gridViewBtn.setToggleGroup(viewGroup);
        
        // Initial styling
        String activeStyle = "-fx-background-color: transparent; -fx-padding: 10;"; 
        String inactiveStyle = "-fx-background-color: white; -fx-border-color: transparent; -fx-border-width: 1px; -fx-padding: 10;"; 

        listViewBtn.setStyle(activeStyle);
        gridViewBtn.setStyle(inactiveStyle);
        
        Region viewSpacer = new Region();
        HBox.setHgrow(viewSpacer, Priority.NEVER);
        viewSpacer.setPrefWidth(20); 

        selectedCountLabel = new Label("Selected 0");

        ComboBox<String> actions = new ComboBox<>();
        actions.getItems().addAll("Delete selected", "Export selected");
        actions.setValue("Choose action");
        actions.setStyle(
    "-fx-background-color: transparent;" + 
    "-fx-border-color: transparent;" +     
    "-fx-font-weight: bold;" +             
    "-fx-text-fill: BLACK;"            
);

        actions.setOnAction(e -> {
            String selectedAction = actions.getValue();
            if (selectedAction == null || selectedAction.equals("Choose action")) return;

            if (selected.isEmpty()) {
                alert("No patients selected.");
                actions.setValue("Choose action");
                return;
            }

            if (selectedAction.equals("Delete selected")) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setHeaderText("Delete selected patients");
                confirm.setContentText("Are you sure you want to delete " + selected.size() + " patient(s)?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            patientDAO.deletePatients(selected.stream().map(Patient::getId).toList());
                            selected.clear();
                            loadPatientsFromDatabase();
                            if (gridViewContainer.isVisible()) updateGridView();
                        } catch (SQLException ex) {
                            alert("Database Error: " + ex.getMessage());
                        }
                    }
                });
            } else if (selectedAction.equals("Export selected")) {
                StringBuilder sb = new StringBuilder();
                selected.forEach(p -> sb.append(p.getFirstName()).append(" ").append(p.getLastName()).append("\n"));
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText("Exported Patients");
                info.setContentText(sb.toString());
                info.show();
            }

            actions.setValue("Choose action");
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); 
       
        toolbar.getChildren().addAll(listViewBtn, gridViewBtn, viewSpacer, selectedCountLabel, actions, spacer); 
        toolbar.setPadding(new Insets(0, 20, 0, 20));
        return toolbar;
    }

    // ---------------- Table with circle checkbox (List View) ----------------
    private TableView<Patient> buildTable() {
        TableView<Patient> t = new TableView<>(patients);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Patient, Boolean> selectCol = new TableColumn<>("");
        selectCol.setPrefWidth(60);
        selectCol.setSortable(false);
    
        // Header Circle Checkbox (Select All)
        Label selectAllCircle = new Label();
        selectAllCircle.setPrefSize(18, 18);
        selectAllCircle.setStyle("-fx-background-color: white;" + "-fx-border-color: #d9e8e9;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-font-size: 11px;" + "-fx-cursor: hand;");
        selectCol.setGraphic(selectAllCircle);
        selectCol.setStyle("-fx-alignment: CENTER;");
    
        selectAllCircle.setOnMouseClicked(e -> {
            ObservableList<Patient> currentItems = t.getItems();
            boolean shouldSelectAll = selected.size() != currentItems.size();
            
            if (shouldSelectAll) {
                selected.addAll(currentItems);
                selectAllCircle.setText("✔");
                selectAllCircle.setStyle("-fx-background-color: #1e90ff;" + "-fx-border-color: #1c7ed6;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;" + "-fx-font-size: 12px;");
            } else {
                currentItems.forEach(selected::remove); 
                selectAllCircle.setText("");
                selectAllCircle.setStyle("-fx-background-color: white;" + "-fx-border-color: #d9e8e9;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-font-size: 11px;");
            }
            
            updateSelectedCountLabel(); 
            t.refresh(); 
            updateGridView(); 
        });
        
        Platform.runLater(() -> t.refresh());

        // Column Value Binding
        selectCol.setCellValueFactory(data -> {
            Patient p = data.getValue();
            BooleanProperty prop = new SimpleBooleanProperty(selected.contains(p));
            prop.addListener((obs, oldVal, newVal) -> {
                if (newVal) selected.add(p);
                else selected.remove(p);
                updateSelectedCountLabel();
                updateGridView(); 
            });
            return prop;
        });

        // Row Circle Checkbox
        selectCol.setCellFactory(col -> new TableCell<>() {
            private final Label circle = new Label();

             {
                circle.setPrefSize(18, 18);
                circle.setStyle("-fx-background-color: white;" + "-fx-border-color: #d9e8e9;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-font-size: 11px;" + "-fx-cursor: hand;");

                circle.setOnMouseClicked(e -> {
                    Patient p = getTableView().getItems().get(getIndex());
                    if (selected.contains(p)) selected.remove(p);
                    else selected.add(p);
                    updateSelectedCountLabel();
                    getTableView().refresh();
                    updateGridView(); 
                    
                    Platform.runLater(() -> {
                         ObservableList<Patient> currentItems = t.getItems();
                         if (selected.size() == currentItems.size() && !currentItems.isEmpty()) {
                             selectAllCircle.setText("✔");
                             selectAllCircle.setStyle("-fx-background-color: #1e90ff;" + "-fx-border-color: #1c7ed6;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;" + "-fx-font-size: 12px;");
                         } else {
                             selectAllCircle.setText("");
                             selectAllCircle.setStyle("-fx-background-color: white;" + "-fx-border-color: #d9e8e9;" + "-fx-border-radius: 20;" + "-fx-background-radius: 20;" + "-fx-alignment: center;" + "-fx-font-size: 11px;");
                         }
                    });
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }
                Patient p = getTableView().getItems().get(getIndex());
                if (selected.contains(p)) {
                    circle.setText("✔");
                    circle.setStyle("-fx-background-color: #1e90ff; -fx-border-color: #1c7ed6; -fx-border-radius: 20; -fx-background-radius: 20; -fx-alignment: center; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
                } else {
                     circle.setText("");
                    circle.setStyle("-fx-background-color: white; -fx-border-color: #d9e8e9; -fx-border-radius: 20; -fx-background-radius: 20; -fx-alignment: center; -fx-font-size: 11px;");
                }
                setGraphic(circle);
                setAlignment(Pos.CENTER);
            }
        });

        // ================= Other Columns =================
        TableColumn<Patient, String> fn = new TableColumn<>("First Name");
        fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        fn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Patient, String> ln = new TableColumn<>("Last Name");
        ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ln.setStyle("-fx-alignment: CENTER;");

        TableColumn<Patient, Integer> age = new TableColumn<>("Age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        age.setStyle("-fx-alignment: CENTER;");

        TableColumn<Patient, String> sex = new TableColumn<>("Sex");
        sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        sex.setStyle("-fx-alignment: CENTER;");

        TableColumn<Patient, LocalDate> lv = new TableColumn<>("Last Visit");
        lv.setCellValueFactory(new PropertyValueFactory<>("lastVisit"));
        lv.setStyle("-fx-alignment: CENTER;");

        t.getColumns().addAll(selectCol, fn, ln, age, sex, lv);
        
        t.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-border-color: transparent; -fx-table-cell-border-color: transparent; -fx-table-row-cell-border-color: transparent; -fx-table-column-border-color: transparent; -fx-control-inner-background: white; -fx-control-inner-background-alt: white; -fx-padding: 0;");
        Platform.runLater(() -> {
            t.lookupAll(".column-header").forEach(header -> header.setStyle("-fx-background-color: #bde5f1;" + "-fx-text-fill: white;" + "-fx-alignment: CENTER;" + "-fx-font-weight: bold;"));
        });

        return t;
    }

    // ---------------- Grid/Tile View Setup ----------------
    private ScrollPane buildGridView() {
        gridView = new FlowPane(15, 15); 
        gridView.setPadding(new Insets(10));
        gridView.setAlignment(Pos.TOP_LEFT);
        gridView.setHgap(20);
        gridView.setVgap(20);
        gridView.setStyle("-fx-background-color: white;");
        updateGridView(); 

        ScrollPane scrollPane = new ScrollPane(gridView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: white; -fx-border-color: white;");

        return scrollPane;
    }

    private void updateGridView() {
        ObservableList<Patient> currentPatients = table.getItems();

        gridView.getChildren().clear();
        for (Patient p : currentPatients) {
            VBox card = createPatientCard(p);
            gridView.getChildren().add(card);
        }
    }

    private VBox createPatientCard(Patient patient) {
        Label name = new Label(patient.getFirstName() + " " + patient.getLastName());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label ageSex = new Label(patient.getAge() + " | " + patient.getSex());
        ageSex.setTextFill(Color.GRAY);
        
        Label lastVisit = new Label("Last Visit: " + patient.getLastVisit().toString());
        
        Label cardCircle = new Label();
        cardCircle.setPrefSize(18, 18);
        cardCircle.setCursor(Cursor.HAND);
        
        boolean isSelected = selected.contains(patient);
        if (isSelected) {
             cardCircle.setText("✔");
             cardCircle.setStyle("-fx-background-color: #1e90ff; -fx-border-color: #1c7ed6; -fx-background-radius: 20; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center; -fx-font-size: 12px; -fx-border-radius: 20;");
        } else {
             cardCircle.setText("");
             cardCircle.setStyle("-fx-background-color: white; -fx-border-color: #d9e8e9; -fx-background-radius: 20; -fx-font-size: 11px; -fx-border-radius: 20;");
        }


        cardCircle.setOnMouseClicked(e -> {
            if (selected.contains(patient)) {
                selected.remove(patient);
            } else {
                selected.add(patient);
            }
            updateSelectedCountLabel();
            updateGridView(); 
            table.refresh(); 
        });


        VBox card = new VBox(5, cardCircle, name, ageSex, lastVisit);
        card.setPadding(new Insets(15));
        card.setPrefSize(200, 150);
        card.setStyle("-fx-background-color: #EAF6FB; -fx-border-color: #86D3DF; -fx-border-width: 2px; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.setCursor(Cursor.HAND);
        
        return card;
    }
    
    // ---------------- Notifications ----------------
   // Palitan ang buong method na ito:
private void setupNotifications() {
    // TANGGALIN: notificationBtn.setOnAction(e -> notificationDropdown.setVisible(!notificationDropdown.isVisible()));

    // PALITAN NG:
    notificationBtn.setOnAction(e -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Notification Center");
        alert.setContentText("• New patient added\n• Appointment updated\n• Database synced");
        alert.setTitle("Clinic Notifications");
        alert.show();
    });
}

    // ---------------- Load patients ----------------
    private void loadPatientsFromDatabase() {
        try {
            List<Patient> list = patientDAO.getAllPatients();
            masterList.setAll(list);
            patients.setAll(list);
            updateSelectedCountLabel();
            if (table != null) table.setItems(patients);
            if (gridView != null) updateGridView();
        } catch (SQLException ex) {
            alert(ex.getMessage());
        }
    }

    private void updateSelectedCountLabel() {
        if (selectedCountLabel == null || totalPatientsLabel == null) return;
        selectedCountLabel.setText("Selected " + selected.size());
        
        int totalDisplaying = table.getItems() != null ? table.getItems().size() : 0;
        totalPatientsLabel.setText("Total patients: " + totalDisplaying);
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }
    

    // ---------------- Add patient dialog ----------------
    private void openAddPatientDialog() {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    if (getScene() != null) dialog.initOwner(getScene().getWindow());

    Label title = new Label("Add Patient");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
    title.setTextFill(Color.web("#084c50"));

    TextField fn = new TextField();
    fn.setPromptText("First Name");
    TextField ln = new TextField();
    ln.setPromptText("Last Name");
    TextField age = new TextField();
    age.setPromptText("Age");

    ComboBox<String> sex = new ComboBox<>(FXCollections.observableArrayList("Male", "Female"));
    sex.setPromptText("Select Sex");

    DatePicker visit = new DatePicker();
    visit.setPromptText("Select Last Visit");

    VBox form = new VBox(10,
            new Label("First Name:"), fn,
            new Label("Last Name:"), ln,
            new Label("Age:"), age,
            new Label("Sex:"), sex,
            new Label("Last Visit:"), visit);
    form.setPadding(new Insets(10));
    form.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d0d0d0; -fx-border-radius: 10; -fx-background-radius: 10;");

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");

    save.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
    save.setCursor(Cursor.HAND);
    cancel.setStyle("-fx-background-color: #d9d9d9; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 5;");
    cancel.setCursor(Cursor.HAND);

    save.setOnAction(e -> {
        try {
            Patient p = new Patient(fn.getText(), ln.getText(), Integer.parseInt(age.getText()), sex.getValue(), visit.getValue());
            patientDAO.addPatient(p);
            loadPatientsFromDatabase();
            dialog.close();
        } catch (Exception ex) {
            alert("Invalid input: " + ex.getMessage());
        }
    });

    cancel.setOnAction(e -> dialog.close());

    HBox btnBox = new HBox(10, cancel, save);
    btnBox.setAlignment(Pos.CENTER_RIGHT);

    VBox overlay = new VBox(20, title, form, btnBox);
    overlay.setPadding(new Insets(20));
    overlay.setStyle("-fx-background-color: #f5f7fa; -fx-border-radius: 10; -fx-background-radius: 10;");

    dialog.setScene(new Scene(overlay, 400, 450));
    dialog.showAndWait();
}
}