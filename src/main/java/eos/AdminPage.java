/* Written By
 * AHMAD HASBOL KAFFI BIN KHALID 2514079
 * MUHAMMAD ARIF NAUFAL BIN NAZRI 2516439 
 */

package eos;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import eos.core.*;

public class AdminPage {

    // ── Data model for the TableView ──────────────────────────────────────────
    public static class OrderRow {
	private final SimpleStringProperty username;
	private final SimpleStringProperty items;
	private final SimpleStringProperty total;
	private final SimpleStringProperty timestamp;

	public OrderRow(String username, String items, String total, String timestamp) {
	    this.username  = new SimpleStringProperty(username);
	    this.items     = new SimpleStringProperty(items);
	    this.total     = new SimpleStringProperty(total);
	    this.timestamp = new SimpleStringProperty(timestamp);
	}

	public String getUsername()  { return username.get(); }
	public String getItems()     { return items.get(); }
	public String getTotal()     { return total.get(); }
	public String getTimestamp() { return timestamp.get(); }

	public SimpleStringProperty usernameProperty()  { return username; }
	public SimpleStringProperty itemsProperty()     { return items; }
	public SimpleStringProperty totalProperty()     { return total; }
	public SimpleStringProperty timestampProperty() { return timestamp; }
    }

    // ── Build the View Orders tab content ─────────────────────────────────────
    private Tab buildViewOrderTab() {
	Tab tab = new Tab("View Orders");
	tab.setClosable(false);

	// ── Header ──
	Label title = new Label("All Orders");
	title.setFont(Font.font("System", FontWeight.BOLD, 20));

	Label subtitle = new Label("Orders across all customers");
	subtitle.setStyle("-fx-text-fill: #6b7280;");

	VBox header = new VBox(2, title, subtitle);
	header.setPadding(new Insets(0, 0, 12, 0));

	// ── Search bar ──
	TextField searchField = new TextField();
	searchField.setPromptText("Search by username, item, or date…");
	searchField.setPrefWidth(300);
	searchField.setStyle(
		"-fx-background-radius: 6; -fx-border-radius: 6; " +
		"-fx-border-color: #d1d5db; -fx-padding: 6 10;"
		);

	Button refreshBtn = new Button("⟳  Refresh");
	refreshBtn.setStyle(
		"-fx-background-color: #2563eb; -fx-text-fill: white; " +
		"-fx-background-radius: 6; -fx-padding: 6 14; -fx-cursor: hand;"
		);

	HBox toolbar = new HBox(10, searchField, refreshBtn);
	toolbar.setAlignment(Pos.CENTER_LEFT);
	toolbar.setPadding(new Insets(0, 0, 10, 0));

	// ── Summary label ──
	Label summaryLabel = new Label();
	summaryLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");

	// ── TableView ──
	TableView<OrderRow> table = new TableView<>();
	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	table.setStyle("-fx-border-color: #e5e7eb; -fx-border-radius: 8;");

	TableColumn<OrderRow, String> colUser = new TableColumn<>("Username");
	colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
	colUser.setMinWidth(120);

	TableColumn<OrderRow, String> colItems = new TableColumn<>("Items");
	colItems.setCellValueFactory(new PropertyValueFactory<>("items"));
	colItems.setMinWidth(280);

	TableColumn<OrderRow, String> colTotal = new TableColumn<>("Total (RM)");
	colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
	colTotal.setMinWidth(100);
	colTotal.setStyle("-fx-alignment: CENTER-RIGHT;");

	TableColumn<OrderRow, String> colTime = new TableColumn<>("Timestamp");
	colTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
	colTime.setMinWidth(160);

	table.getColumns().addAll(colUser, colItems, colTotal, colTime);

	// Stripe rows for readability
	table.setRowFactory(tv -> new TableRow<>() {
	    @Override
	    protected void updateItem(OrderRow item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
		    setStyle("");
		} else {
		    setStyle(getIndex() % 2 == 0
			    ? "-fx-background-color: #ffffff;"
			    : "-fx-background-color: #f9fafb;");
		}
	    }
	});

	// ── Load data ──
	ObservableList<OrderRow> masterData = FXCollections.observableArrayList();
	loadOrders(masterData);

	FilteredList<OrderRow> filteredData = new FilteredList<>(masterData, p -> true);
	table.setItems(filteredData);
	summaryLabel.setText(masterData.size() + " orders found");

	// ── Wire search ──
	searchField.textProperty().addListener((obs, oldVal, newVal) -> {
	    String filter = newVal == null ? "" : newVal.trim().toLowerCase();
	    filteredData.setPredicate(row -> {
		if (filter.isEmpty()) return true;
		return row.getUsername().toLowerCase().contains(filter)
		    || row.getItems().toLowerCase().contains(filter)
		    || row.getTimestamp().toLowerCase().contains(filter);
	    });
	    summaryLabel.setText(filteredData.size() + " of " + masterData.size() + " orders");
	});

	// ── Wire refresh ──
	refreshBtn.setOnAction(e -> {
	    masterData.clear();
	    loadOrders(masterData);
	    searchField.clear();
	    summaryLabel.setText(masterData.size() + " orders found");
	});

	// ── Layout assembly ──
	VBox root = new VBox(6, header, toolbar, summaryLabel, table);
	VBox.setVgrow(table, Priority.ALWAYS);
	root.setPadding(new Insets(20));
	root.setStyle("-fx-background-color: #f8fafc;");

	tab.setContent(root);
	return tab;
    }

    // ── Read orders from disk ─────────────────────────────────────────────────
    private void loadOrders(ObservableList<OrderRow> data) {
	File ordersFolder = new File(System.getProperty("user.dir") + "/data/orders/");
	File[] orderFiles = ordersFolder.listFiles();

	if (orderFiles == null) return;

	for (File file : orderFiles) {
	    String username = file.getName().replace(".txt", "");
	    List<Order> orders = OrderFileHandler.loadOrders(username);

	    for (Order o : orders) {
		StringBuilder sb = new StringBuilder();
		for (Product p : o.getItems()) {
		    if (sb.length() > 0) sb.append(", ");
		    sb.append(p.getName());
		}
		data.add(new OrderRow(
			    username,
			    sb.toString(),
			    String.format("%.2f", o.getTotal()),
			    o.getTimestamp()
			    ));
	    }
	}
    }

    // ── App entry ─────────────────────────────────────────────────────────────
    public Scene createScene() {
	TabPane tabPane = new TabPane();

	Tab manageProductTab = new Tab("Manage Products");
	manageProductTab.setClosable(false);

	tabPane.getTabs().addAll(buildViewOrderTab(), buildManageProductTab());

	return new Scene(tabPane, 900, 600);
    }
    private Tab buildManageProductTab() {

	Tab tab = new Tab("Manage Products");
	tab.setClosable(false);

	Inventory inventory = new Inventory();

	ObservableList<Product> productList =
	    FXCollections.observableArrayList(
		    inventory.getAll()
		    );
	// FORM

	TextField codeField = new TextField();
	TextField nameField = new TextField();
	TextField priceField = new TextField();
	TextField stockField = new TextField();

	GridPane form = new GridPane();
	form.setHgap(10);
	form.setVgap(10);

	form.add(new Label("Code"), 0, 0);
	form.add(codeField, 1, 0);

	form.add(new Label("Name"), 0, 1);
	form.add(nameField, 1, 1);

	form.add(new Label("Price"), 0, 2);
	form.add(priceField, 1, 2);

	form.add(new Label("Stock"), 0, 3);
	form.add(stockField, 1, 3);

	// TABLE

	TableView<Product> table = new TableView<>();
	table.setItems(productList);
	TableColumn<Product, String> codeCol =
	    new TableColumn<>("Code");

	TableColumn<Product, String> nameCol =
	    new TableColumn<>("Name");
	codeCol.setCellValueFactory(
		cellData -> new SimpleStringProperty(
		    cellData.getValue().getCode()
		    )
		);

	nameCol.setCellValueFactory(
		cellData -> new SimpleStringProperty(
		    cellData.getValue().getName()
		    )
		);

	TableColumn<Product, String> priceCol =
	    new TableColumn<>("Price");

	priceCol.setCellValueFactory(
		cellData -> new SimpleStringProperty(
		    String.valueOf(cellData.getValue().getPrice())
		    )
		);

	TableColumn<Product, String> stockCol =
	    new TableColumn<>("Stock");

	stockCol.setCellValueFactory(
		cellData -> new SimpleStringProperty(
		    String.valueOf(cellData.getValue().getAvailability())
		    )
		);
	table.getColumns().addAll(
		codeCol,
		nameCol,
		priceCol,
		stockCol
		);

	table.setColumnResizePolicy(
		TableView.CONSTRAINED_RESIZE_POLICY
		);
	// BUTTONS

	Button addBtn = new Button("Add Product");
	Button editBtn = new Button("Edit Product");
	Button deleteBtn = new Button("Delete Product");
	Button clearBtn = new Button("Clear");

	HBox buttonBox = new HBox(
		10,
		addBtn,
		editBtn,
		deleteBtn,
		clearBtn
		);
	// TABLE SELECTION
	table.getSelectionModel()
	    .selectedItemProperty()
	    .addListener((obs, oldVal, product) -> {

		if (product != null) {

		    codeField.setText(
			    product.getCode());

		    nameField.setText(
			    product.getName());

		    priceField.setText(
			    String.valueOf(
				product.getPrice()
				)
			    );

		    stockField.setText(
			    String.valueOf(
				product.getAvailability()
				)
			    );
		}
	    });
	// ADD

	addBtn.setOnAction(e -> {

	    try {

		Product product =
		    new Product(
			    nameField.getText(),
			    codeField.getText(),
			    Double.parseDouble(
				priceField.getText()
				),
			    Integer.parseInt(
				stockField.getText()
				)
			    );

		inventory.addProduct(product);
		System.out.println("Added product");
		System.out.println("Total products: " + inventory.getAll().size());

		productList.clear();
		productList.addAll(
			inventory.getAll()
			);

		table.refresh();

	    } catch (Exception ex) {

		Alert alert =
		    new Alert(
			    Alert.AlertType.ERROR,
			    "Invalid input!"
			    );

		alert.showAndWait();
	    }
	});
	// EDIT

	editBtn.setOnAction(e -> {

	    Product selected =
		table.getSelectionModel()
		.getSelectedItem();

	    if (selected == null)
		return;

	    try {

		inventory.removeProduct(
			selected.getCode()
			);

		Product updated =
		    new Product(
			    nameField.getText(),
			    codeField.getText(),
			    Double.parseDouble(
				priceField.getText()
				),
			    Integer.parseInt(
				stockField.getText()
				)
			    );

		inventory.addProduct(updated);

		productList.clear();
		productList.addAll(
			inventory.getAll()
			);

		table.refresh();

	    } catch (Exception ex) {

		Alert alert =
		    new Alert(
			    Alert.AlertType.ERROR,
			    "Invalid input!"
			    );

		alert.showAndWait();
	    }
	});
	// DELETE

	deleteBtn.setOnAction(e -> {

	    Product selected =
		table.getSelectionModel()
		.getSelectedItem();

	    if (selected == null)
		return;

	    inventory.removeProduct(
		    selected.getCode()
		    );

	    productList.clear();
	    productList.addAll(
		    inventory.getAll()
		    );

	    table.refresh();
	});
	// CLEAR

	clearBtn.setOnAction(e -> {

	    codeField.clear();
	    nameField.clear();
	    priceField.clear();
	    stockField.clear();

	    table.getSelectionModel()
		.clearSelection();
	});
	// LAYOUT
	VBox root = new VBox(
		15,
		form,
		buttonBox,
		table
		);

	root.setPadding(new Insets(15));

	VBox.setVgrow(
		table,
		Priority.ALWAYS
		);

	tab.setContent(root);

	return tab;
    }
}
