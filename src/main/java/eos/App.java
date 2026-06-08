package eos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import eos.core.*;

public class App extends Application {
    private Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        showAdminDashboardScene();
    }

    // --- ADMIN DASHBOARD SCENE ---
    private void showAdminDashboardScene() {
        BorderPane rootLayout = new BorderPane();

        HBox headerBar = new HBox();
        headerBar.setPadding(new Insets(15, 20, 15, 20));
        headerBar.setStyle("-fx-background-color: #1e293b;");
        headerBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("ADMIN DASHBOARD");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBar.getChildren().addAll(titleLabel, spacer);
        rootLayout.setTop(headerBar);

        TabPane tabPane = new TabPane();
        Tab viewOrdersTab = buildViewOrderTab();
        Tab manageProductsTab = new Tab("Manage Products");
        manageProductsTab.setClosable(false);

        tabPane.getTabs().addAll(viewOrdersTab, manageProductsTab);
        rootLayout.setCenter(tabPane);

        mainStage.setScene(new Scene(rootLayout, 900, 600));
        mainStage.setTitle("Admin Dashboard");
        mainStage.show();
    }

    // --- VIEW ORDERS TAB ---
    private Tab buildViewOrderTab() {
        Tab tab = new Tab("View Orders");
        tab.setClosable(false);

        Label titleLabel = new Label("All Orders");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        Label subtitleLabel = new Label("Orders across all customers");
        subtitleLabel.setStyle("-fx-text-fill: #6b7280;");

        VBox headerBlock = new VBox(2, titleLabel, subtitleLabel);
        headerBlock.setPadding(new Insets(0, 0, 12, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by username, item, or date...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #d1d5db; -fx-padding: 6 10;");

        Button refreshBtn = new Button("⟳  Refresh");
        refreshBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 6 14; -fx-cursor: hand;");

        HBox toolbarRow = new HBox(10, searchField, refreshBtn);
        toolbarRow.setAlignment(Pos.CENTER_LEFT);
        toolbarRow.setPadding(new Insets(0, 0, 10, 0));

        Label summaryLabel = new Label();
        summaryLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");

        ObservableList<String> masterData = FXCollections.observableArrayList();
        loadOrders(masterData);

        FilteredList<String> filteredData = new FilteredList<>(masterData, p -> true);
        ListView<String> orderListView = new ListView<>(filteredData);
        summaryLabel.setText(masterData.size() + " orders found");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal == null ? "" : newVal.trim().toLowerCase();
            filteredData.setPredicate(row -> filter.isEmpty() || row.toLowerCase().contains(filter));
            summaryLabel.setText(filteredData.size() + " of " + masterData.size() + " orders");
        });

        refreshBtn.setOnAction(e -> {
            masterData.clear();
            loadOrders(masterData);
            searchField.clear();
            summaryLabel.setText(masterData.size() + " orders found");
        });

        VBox tabContent = new VBox(6, headerBlock, toolbarRow, summaryLabel, orderListView);
        VBox.setVgrow(orderListView, Priority.ALWAYS);
        tabContent.setPadding(new Insets(20));
        tabContent.setStyle("-fx-background-color: #f8fafc;");

        tab.setContent(tabContent);
        return tab;
    }

    // --- LOAD ORDERS FROM DISK ---
    private void loadOrders(ObservableList<String> data) {
        File ordersFolder = new File(System.getProperty("user.dir") + "/data/orders/");
        File[] orderFiles = ordersFolder.listFiles();

        if (orderFiles == null) return;

        for (File file : orderFiles) {
            String username = file.getName().replace(".txt", "");
            List<Order> orders = OrderFileHandler.loadOrders(username);

            for (Order o : orders) {
                StringBuilder itemNames = new StringBuilder();
                for (Product p : o.getItems()) {
                    if (itemNames.length() > 0) itemNames.append(", ");
                    itemNames.append(p.getName());
                }
                data.add(username + " | " + itemNames + " | RM " +
                    String.format("%.2f", o.getTotal()) + " | " + o.getTimestamp());
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}