/* Written By
 * MUHAMMAD IKHWAN HAKIM BIN MAZRI 2513269
 */

package eos;

import eos.core.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.*;

public class CustomerPage {

    private Cart currentCart;
    private Label cartLabel;
    private Inventory inventory;
    private User currentUser;

    private TableView<Product> productTable;
    private ObservableList<Product> masterProductData;
    private TextField searchField;

    public Scene createScene(User user, Cart cart) {
	currentUser = user;
	inventory = new Inventory();
	currentCart = cart;

	//  TOP BAR
	HBox top = new HBox(10);
	top.setPadding(new Insets(15));

	Label logo = new Label("EOS Shop");
	logo.setFont(Font.font("Arial", FontWeight.BOLD, 18));

	Region spacer = new Region();
	HBox.setHgrow(spacer, Priority.ALWAYS);

	searchField = new TextField();
	searchField.setPromptText("Search...");

	cartLabel = new Label("Cart (0)");
	cartLabel.setOnMouseClicked(e -> showCart());

	Label orderLabel = new Label("Orders");
	orderLabel.setStyle("-fx-text-fill: blue; -fx-cursor: hand;");
	orderLabel.setOnMouseClicked(e -> showOrders());

	top.getChildren().addAll(logo, spacer, searchField, cartLabel, orderLabel);

	//  PRODUCT TABLE
	productTable = new TableView<>();
	masterProductData = FXCollections.observableArrayList();
	masterProductData.addAll(inventory.getAll());

	FilteredList<Product> filtered = new FilteredList<>(masterProductData);
	productTable.setItems(filtered);

	searchField.textProperty().addListener((obs, o, n) -> {
	    filtered.setPredicate(p -> {
		if (n == null || n.isEmpty()) return true;
		String f = n.toLowerCase();
		return p.getName().toLowerCase().contains(f)
		    || p.getCode().toLowerCase().contains(f);
	    });
	});

	//  COLUMNS
	TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
	nameCol.setCellValueFactory(d ->
		new SimpleStringProperty(d.getValue().getName())
		);

	TableColumn<Product, String> codeCol = new TableColumn<>("Code");
	codeCol.setCellValueFactory(d ->
		new SimpleStringProperty(d.getValue().getCode())
		);
	codeCol.setStyle("-fx-alignment: CENTER;");

	TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
	priceCol.setCellValueFactory(d ->
		new SimpleDoubleProperty(d.getValue().getPrice()).asObject()
		);
	priceCol.setCellFactory(tc -> new TableCell<>() {
	    protected void updateItem(Double val, boolean empty) {
		super.updateItem(val, empty);
		setText(empty ? null : String.format("RM %.2f", val));
	    }
	});
	priceCol.setStyle("-fx-alignment: CENTER;");

	TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
	stockCol.setCellValueFactory(d ->
		new SimpleIntegerProperty(d.getValue().getAvailability()).asObject()
		);
	stockCol.setStyle("-fx-alignment: CENTER;");

	TableColumn<Product, Void> actionCol = new TableColumn<>("Action");
	actionCol.setCellFactory(tc -> new TableCell<>() {
	    private final Button btn = new Button("Add");

	    {
		btn.setOnAction(e -> {
		    Product p = getTableView().getItems().get(getIndex());
		    if (p != null) {
			currentCart.addItem(p);
			cartLabel.setText("Cart (" + currentCart.getItems().size() + ")");
		    }
		});
	    }

	    protected void updateItem(Void item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(empty ? null : btn);
		setAlignment(Pos.CENTER);
	    }
	});

	productTable.getColumns().addAll(nameCol, codeCol, priceCol, stockCol, actionCol);


	productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	nameCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.4);
	codeCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.15);
	priceCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.15);
	stockCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.15);
	actionCol.setMaxWidth(1f * Integer.MAX_VALUE * 0.15);

	BorderPane root = new BorderPane();
	root.setTop(top);
	root.setCenter(productTable);

	return new Scene(root, 900, 600);
    }

    // Cart
    private void showCart() {

	Stage cartStage = new Stage();
	cartStage.setTitle("Cart");

	VBox root = new VBox(15);
	root.setPadding(new Insets(20));

	if (currentCart.getItems().isEmpty()) {
	    root.getChildren().add(new Label("Cart empty"));
	    cartStage.setScene(new Scene(root, 300, 150));
	    cartStage.show();
	    return;
	}

	TableView<CartRow> table = new TableView<>();

	TableColumn<CartRow, String> nameCol = new TableColumn<>("Name");
	nameCol.setCellValueFactory(d -> d.getValue().name);

	TableColumn<CartRow, Number> qtyCol = new TableColumn<>("Qty");
	qtyCol.setCellValueFactory(d -> d.getValue().qty);
	qtyCol.setStyle("-fx-alignment: CENTER;");

	TableColumn<CartRow, String> codeCol = new TableColumn<>("Code");
	codeCol.setCellValueFactory(d -> d.getValue().code);
	codeCol.setStyle("-fx-alignment: CENTER;");

	TableColumn<CartRow, String> priceCol = new TableColumn<>("Total");
	priceCol.setCellValueFactory(d -> d.getValue().price);
	priceCol.setStyle("-fx-alignment: CENTER;");

	table.getColumns().addAll(nameCol, qtyCol, codeCol, priceCol);
	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	Map<String, Integer> qtyMap = new HashMap<>();
	Map<String, Product> productMap = new HashMap<>();

	for (Product p : currentCart.getItems()) {
	    String code = p.getCode();
	    qtyMap.put(code, qtyMap.getOrDefault(code, 0) + 1);
	    productMap.put(code, p);
	}

	ObservableList<CartRow> rows = FXCollections.observableArrayList();

	for (String code : qtyMap.keySet()) {
	    rows.add(new CartRow(productMap.get(code), qtyMap.get(code)));
	}

	table.setItems(rows);

	double total = currentCart.getItems().stream().mapToDouble(Product::getPrice).sum();

	Label totalLabel = new Label("Total: RM " + String.format("%.2f", total));

	Button checkout = new Button("Checkout");

	checkout.setOnAction(e -> {
	    currentCart.checkout(inventory, currentUser.getUsername());

	    cartLabel.setText("Cart (0)");

	    masterProductData.clear();
	    masterProductData.addAll(inventory.getAll());
	    productTable.refresh();

	    cartStage.close();

	    new Alert(Alert.AlertType.INFORMATION, "Order placed!").showAndWait();
	});

	Button clear = new Button("Clear");
	clear.setOnAction(e -> {
	    currentCart.getItems().clear();
	    cartLabel.setText("Cart (0)");
	    cartStage.close();
	});

	HBox buttons = new HBox(10, clear, checkout);
	buttons.setAlignment(Pos.CENTER_RIGHT);

	root.getChildren().addAll(table, totalLabel, buttons);

	cartStage.setScene(new Scene(root, 450, 350));
	cartStage.show();
    }

    // ORDER
    private void showOrders() {

	Stage orderStage = new Stage();
	orderStage.setTitle("Order History");

	VBox root = new VBox(15);
	root.setPadding(new Insets(20));

	TableView<OrderRow> table = new TableView<>();

	TableColumn<OrderRow, String> nameCol = new TableColumn<>("Name");
	TableColumn<OrderRow, String> codeCol = new TableColumn<>("Code");
	TableColumn<OrderRow, String> priceCol = new TableColumn<>("Price");
	TableColumn<OrderRow, String> timeCol = new TableColumn<>("Time");

	nameCol.setCellValueFactory(d -> d.getValue().name);
	codeCol.setCellValueFactory(d -> d.getValue().code);
	priceCol.setCellValueFactory(d -> d.getValue().price);
	timeCol.setCellValueFactory(d -> d.getValue().time);

	codeCol.setStyle("-fx-alignment: CENTER;");
	priceCol.setStyle("-fx-alignment: CENTER;");

	table.getColumns().addAll(nameCol, codeCol, priceCol, timeCol);
	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	ObservableList<OrderRow> rows = FXCollections.observableArrayList();

	List<Order> orders = OrderFileHandler.loadOrders(currentUser.getUsername());

	for ( Order o : orders ) {
	    for ( Product p : o.getItems()) {
		rows.add( new OrderRow (
			    p.getName(),
			    p.getCode(),
			    "RM" + String.format("%.2f", p.getPrice()),
			    o.getTimestamp()
			    ));
	    }
	}

	table.setItems(rows);
	root.getChildren().add(table);

	orderStage.setScene(new Scene(root, 500, 400));
	orderStage.show();
    }

    static class CartRow {
	SimpleStringProperty name, code, price;
	SimpleIntegerProperty qty;

	CartRow(Product p, int q) {
	    name = new SimpleStringProperty(p.getName());
	    code = new SimpleStringProperty(p.getCode());
	    qty = new SimpleIntegerProperty(q);
	    price = new SimpleStringProperty(
		    String.format("RM %.2f", p.getPrice() * q)
		    );
	}
    }

    static class OrderRow {
	SimpleStringProperty name, code, price, time;

	OrderRow(String n, String c, String p, String t) {
	    name = new SimpleStringProperty(n);
	    code = new SimpleStringProperty(c);
	    price = new SimpleStringProperty(p);
	    time = new SimpleStringProperty(t);
	}
    }
}
