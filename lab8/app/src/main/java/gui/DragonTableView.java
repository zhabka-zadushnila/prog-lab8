package gui;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import gui.managers.CommandsManager;
import gui.managers.LocaleManager;
import gui.screens.DragonFormScreen;
import gui.screens.LoginScreen;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import managers.CollectionManager;
import structs.User;
import structs.classes.Color;
import structs.classes.Coordinates;
import structs.classes.Dragon;
import structs.classes.DragonCave;
import structs.classes.DragonCharacter;
import structs.classes.DragonType;
import structs.wrappers.DragonDisplayWrapper;


public class DragonTableView extends Application {
    private final LocaleManager localeManager = LocaleManager.getInstance();
    private static CollectionManager collectionManager;
    private final ObservableList<DragonDisplayWrapper> masterData = FXCollections.observableArrayList();
    User user = null;


    Label userLabel;
    private final static int POLL_RATIO = 1000;

    private TableView<DragonDisplayWrapper> table = new TableView<>();
    private TextField filterField;
    private Pane visual;
    private CommandsManager commandsManager = new CommandsManager();

    public static void initialize(CollectionManager cm) {
        collectionManager = cm;
    }

    @Override
    public void start(Stage primaryStage) {
        startLogin();
        primaryStage.titleProperty().bind(localeManager.createStringBinding("app.title"));


        if (collectionManager == null) {
            collectionManager = new CollectionManager();
            Dragon testDragon1 = new Dragon("Smaug", new Coordinates(10.5, 100L), 171, Color.BLACK, DragonType.FIRE, DragonCharacter.EVIL, new DragonCave(1000, 100000.0));
            testDragon1.setOwnerLogin("user1");
            Dragon testDragon2 = new Dragon("Viserion", new Coordinates(25.0, 500L), 5, Color.YELLOW, DragonType.AIR, DragonCharacter.FICKLE, null);
            testDragon2.setOwnerLogin("user2");
            collectionManager.addElement("smaug_key", testDragon1);
            collectionManager.addElement("viserion_key", testDragon2);
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        BorderPane topPanel = createTopPanel();
        root.setTop(topPanel);

        setupTable();
        root.setCenter(table);

        FlowPane bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        visual = createRightPannel(primaryStage); 
        visual.prefWidthProperty().bind(root.widthProperty().multiply(0.475));
        visual.prefHeightProperty().bind(root.heightProperty());
        visual.setStyle(
            "-fx-border-color: #333333;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 4;" +
            "-fx-background-color: white;"
        );


        Label title = new Label("Область визуализации");
        title.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        VBox rightBox = new VBox(5);

        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.getChildren().addAll(title, visual);
        BorderPane.setMargin(rightBox, new Insets(0, 0, 0, 10)); 

        root.setRight(rightBox);

        table.requestFocus();
        table.getSelectionModel().select(0);
        table.getFocusModel().focus(0);
        loadDataFromCollectionManager();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);

        new Thread(() -> {
            while (true) {
                try {
                    //DragonDisplayWrapper dragonDisplayWrapper = table.getSelectionModel().getSelectedItem();
                    if(table.getSelectionModel().getSelectedItem()!=null){
                        Thread.sleep(200);
                    }else{
                        collectionManager.sync();

                        Platform.runLater(() -> {
                            loadDataFromCollectionManager();
                            updateVisualPane(primaryStage);
                        });
                        /*if(dragonDisplayWrapper != null){
                            System.out.println(table.getSelectionModel().getSelectedIndex());
                            System.out.println(table.getSelectionModel().getFocusedIndex());

                            table.getSelectionModel().select(dragonDisplayWrapper);

                        }*/
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        localeManager.localeProperty().addListener((obs, oldVal, newVal) -> table.refresh());
        primaryStage.show();
    }

    private void startLogin() {
        this.user = (new LoginScreen()).start();
    }

    private void updateVisualPane(Stage primaryStage) {
        visual.getChildren().clear();  
        for (DragonDisplayWrapper dragonWrapper : masterData) {
            Dragon dragon = dragonWrapper.getOriginalDragon();
            Node visualisation = getVisualisation(dragon, primaryStage, dragonWrapper.getKey());
            visual.getChildren().add(visualisation);
        }
    }

    private void updateDragon(Dragon dragon, String key){

        DragonDisplayWrapper selectedForUpdate = new DragonDisplayWrapper(key, dragon);
        if (dragon.getOwnerLogin().equals(user.getLogin())) {
            DragonFormScreen updateDialog = new DragonFormScreen();
            Dragon newDragon = updateDialog.updateDragon(selectedForUpdate);

            if (newDragon != null) {
                newDragon.setOwnerLogin(user.getLogin());
                collectionManager.replaceElement(selectedForUpdate.getKey(), newDragon);
                String response = commandsManager.updateDragon(new DragonDisplayWrapper(selectedForUpdate.getKey(), newDragon), user);
                showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                collectionManager.sync();
                loadDataFromCollectionManager();
            }
        
        } else {
            showAlert(Alert.AlertType.ERROR, "Bruh", "Sorry, but you can not modify someones dragon");
        }
    }
    private BorderPane createTopPanel() {
        BorderPane topPanel = new BorderPane();

        HBox filterPanel = new HBox(10);
        filterPanel.setPadding(new Insets(10));
        filterPanel.setAlignment(Pos.CENTER_RIGHT);
        Label filterLabel = new Label();
        filterLabel.textProperty().bind(localeManager.createStringBinding("filter.label"));
        filterField = new TextField();
        filterField.promptTextProperty().bind(localeManager.createStringBinding("filter.prompt"));

        Button refreshButton = new Button();
        refreshButton.textProperty().bind(localeManager.createStringBinding("button.refresh"));
        refreshButton.setOnAction(e -> loadDataFromCollectionManager());

        ComboBox<Locale> languageSelector = createLanguageSelector();


        filterPanel.getChildren().addAll(filterLabel, filterField, refreshButton, languageSelector);


        HBox userPanel = new HBox(10);
        userPanel.setPadding(new Insets(10));
        userPanel.setAlignment(Pos.CENTER_LEFT);
        Label userLabel = new Label();
        userLabel.setText((user == null) ? "" : user.getLogin());


        Button registerButton = new Button();
        registerButton.textProperty().bind(localeManager.createStringBinding("button.registerLogin"));


        userPanel.getChildren().addAll(userLabel, registerButton);
        registerButton.setOnAction(e -> {
            startLogin();
            userLabel.setText((user == null) ? "" : user.getLogin());
        });

        topPanel.setLeft(userPanel);
        topPanel.setRight(filterPanel);
        return topPanel;
    }

    private ComboBox<Locale> createLanguageSelector() {
        ComboBox<Locale> languageSelector = new ComboBox<>();

        languageSelector.getItems().add(new Locale("ru", "RU"));
        languageSelector.getItems().add(new Locale("pl", "PL"));
        languageSelector.getItems().add(new Locale("is", "IS"));
        languageSelector.getItems().add(new Locale("en", "IN"));

        languageSelector.setConverter(new javafx.util.StringConverter<Locale>() {
            @Override
            public String toString(Locale locale) {
                if (locale == null) return "";
                return locale.getDisplayLanguage(locale);
            }
            @Override
            public Locale fromString(String string) {
                return null;
            }
        });

        languageSelector.setValue(localeManager.getLocale());

        languageSelector.valueProperty().addListener((obs, oldLocale, newLocale) -> {
            if (newLocale != null) {
                localeManager.setLocale(newLocale);
            }
        });

        return languageSelector;
    }

    private Pane createRightPannel(Stage primaryStage){
        Pane pane = new Pane();
        for (DragonDisplayWrapper dragonWrapper : masterData) {
            Dragon dragon = dragonWrapper.getOriginalDragon();
            Node visualisation = getVisualisation(dragon, primaryStage, dragonWrapper.getKey());
            pane.getChildren().add(visualisation);
        }
        return pane;
    }


    private Node getVisualisation(Dragon dragon, Stage primaryStage, String key){
        DragonType type = dragon.getType();
        String ownersLogin = dragon.getOwnerLogin();
        javafx.scene.paint.Color color = getColorByOwner(ownersLogin);
        double x = dragon.getCoordinates().getX();
        double y = dragon.getCoordinates().getY();
        double correctX = ((101*x) % 500) + 50;
        double correctY = (y*423 % 600);
        switch(type){
            case FIRE -> {
            Circle figure = new Circle(correctX, correctY, 15);
            figure.setFill(color);
            attachInfoHandler(figure, dragon, primaryStage, key);
            return figure;
        }
        case AIR -> {
            Polygon figure = new Polygon();
            figure.getPoints().addAll(
                correctX, correctY - 20,
                correctX - 15, correctY + 15,
                correctX + 15, correctY + 15
            );
            figure.setFill(color);
            attachInfoHandler(figure, dragon, primaryStage, key);
            return figure;
        }
        case WATER -> {
            Polygon figure = new Polygon();
            figure.getPoints().addAll(
                correctX, correctY - 20,
                correctX - 15, correctY,
                correctX, correctY + 20,
                correctX + 15, correctY
            );
            figure.setFill(color);
            attachInfoHandler(figure, dragon, primaryStage, key);
            return figure;

        }
        case UNDERGROUND -> {
            Rectangle figure = new Rectangle(correctX - 15, correctY - 15, 30, 30);
            figure.setFill(color);
            attachInfoHandler(figure, dragon, primaryStage, key);
            return figure;
        }default ->{
            Circle figure = new Circle();
            figure.setFill(color);
            attachInfoHandler(figure, dragon, primaryStage, key);
            return figure;
        }
        }


    }

    private javafx.scene.paint.Color getColorByOwner(String login){
        int hash = Math.abs(login.hashCode());
        double hue = (hash*788) % 360;
        double saturation = 0.7;
        double brightness = 0.9;
        return javafx.scene.paint.Color.hsb(hue, saturation, brightness);
    }

private void attachInfoHandler(Node node, Dragon dragon, Stage primaryStage, String key) {
    node.setOnMouseClicked(event -> {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.setTitle("Информация о драконе");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label header = new Label(dragon.getName() + " (" + dragon.getOwnerLogin() + ")");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        StringBuilder content = new StringBuilder();
        content.append("Имя: ").append(dragon.getName()).append("\n")
               .append("Координаты: (")
               .append(dragon.getCoordinates().getX()).append(", ")
               .append(dragon.getCoordinates().getY()).append(")\n")
               .append("Возраст: ").append(dragon.getAge()).append("\n")
               .append("Тип: ").append(dragon.getType()).append("\n")
               .append("Характер: ").append(dragon.getCharacter());
        if (dragon.getCave() != null) {
            content.append("\nПещера: ")
                   .append(dragon.getCave().getNumberOfTreasures())
                   .append(" сокровищ");
        }

        TextArea infoArea = new TextArea(content.toString());
        infoArea.setEditable(false);
        infoArea.setWrapText(true);

        Button updateButton = new Button("Обновить");
        updateButton.setOnAction(e -> {
            updateDragon(dragon, key);
            dialog.close();
        });

        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, updateButton, closeButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        vbox.getChildren().addAll(header, infoArea, buttons);

        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    });
}
    private void setupTable() {
        TableColumn<DragonDisplayWrapper, String> keyCol = new TableColumn<>();
        keyCol.textProperty().bind(localeManager.createStringBinding("column.key"));
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<DragonDisplayWrapper, String> ownerCol = new TableColumn<>();
        ownerCol.textProperty().bind(localeManager.createStringBinding("column.owner"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<DragonDisplayWrapper, String> nameCol = new TableColumn<>();
        nameCol.textProperty().bind(localeManager.createStringBinding("column.name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DragonDisplayWrapper, Double> xCol = new TableColumn<>("X");
        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));

        TableColumn<DragonDisplayWrapper, Long> yCol = new TableColumn<>("Y");
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));

        TableColumn<DragonDisplayWrapper, LocalDate> dateCol = new TableColumn<>();
        dateCol.textProperty().bind(localeManager.createStringBinding("column.creationDate"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<DragonDisplayWrapper, Integer> ageCol = new TableColumn<>();
        ageCol.textProperty().bind(localeManager.createStringBinding("column.age"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<DragonDisplayWrapper, String> colorCol = new TableColumn<>();
        colorCol.textProperty().bind(localeManager.createStringBinding("column.color"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableColumn<DragonDisplayWrapper, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<DragonDisplayWrapper, String> charCol = new TableColumn<>("Character");
        charCol.setCellValueFactory(new PropertyValueFactory<>("character"));

        TableColumn<DragonDisplayWrapper, Integer> depthCol = new TableColumn<>("Cave Depth");
        depthCol.setCellValueFactory(new PropertyValueFactory<>("depth"));

        TableColumn<DragonDisplayWrapper, Double> treasuresCol = new TableColumn<>("Treasures");
        treasuresCol.setCellValueFactory(new PropertyValueFactory<>("treasures"));

        table.getColumns().addAll(keyCol, ownerCol, nameCol, xCol, yCol, dateCol, ageCol, colorCol, typeCol, charCol, depthCol, treasuresCol);

        FilteredList<DragonDisplayWrapper> filteredData = new FilteredList<>(masterData, p -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dragonWrapper -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return dragonWrapper.getStreamOfFields()
                        .anyMatch(field -> field.toLowerCase().contains(lowerCaseFilter));
            });
        });


        SortedList<DragonDisplayWrapper> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private FlowPane createBottomPanel() {
        FlowPane bottomPanel = new FlowPane();
        bottomPanel.setPadding(new Insets(10, 10, 10, 120));
        bottomPanel.setHgap(10);
        bottomPanel.setVgap(10);
        bottomPanel.setAlignment(Pos.CENTER_LEFT);

        String[] commandNames = {"info", "show", "insert", "update", "remove_key", "remove_greater", "replace_if_lower"};

        for (String commandName : commandNames) {
            Button btn = new Button(commandName.replace('_', ' '));
            btn.setOnAction(e -> handleCommand(commandName));
            bottomPanel.getChildren().add(btn);
        }
        return bottomPanel;
    }


    private void handleCommand(String commandName) {

        System.out.println("Executing command: " + commandName);

        switch (commandName) {

            case "insert":
                DragonFormScreen insertDialog = new DragonFormScreen();
                DragonDisplayWrapper newEntry = insertDialog.getNewDragon();
                if (newEntry != null) {
                    newEntry.getValue().setOwnerLogin(user.getLogin());
                    String response = commandsManager.insertDragon(newEntry, user);
                    showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                    collectionManager.sync();
                    loadDataFromCollectionManager();
                }
                break;

            case "update":
                DragonDisplayWrapper selectedForUpdate = table.getSelectionModel().getSelectedItem();
                if(selectedForUpdate != null){
                    if (selectedForUpdate.getOwner().equals(user.getLogin())) {
                        DragonFormScreen updateDialog = new DragonFormScreen();
                        Dragon newDragon = updateDialog.updateDragon(selectedForUpdate);
                        newDragon.setOwnerLogin(user.getLogin());
                        if (newDragon != null) {
                            collectionManager.replaceElement(selectedForUpdate.getKey(), newDragon);
                            String response = commandsManager.updateDragon(new DragonDisplayWrapper(selectedForUpdate.getKey(), newDragon), user);
                            showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                            collectionManager.sync();
                            loadDataFromCollectionManager();
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Bruh", "Sorry, but you can not modify someones dragon");
                    }

                }
                break;

            case "replace_if_lower":
                DragonDisplayWrapper selectedForReplace = table.getSelectionModel().getSelectedItem();
                if(selectedForReplace != null){
                    if (selectedForReplace.getOwner().equals(user.getLogin())) {
                        DragonFormScreen updateDialog = new DragonFormScreen();
                        Dragon newDragon = updateDialog.updateDragon(selectedForReplace);
                        newDragon.setOwnerLogin(user.getLogin());
                        if (newDragon != null) {
                            collectionManager.replaceElement(selectedForReplace.getKey(), newDragon);
                            String response = commandsManager.replaceIfLowerDragon(new DragonDisplayWrapper(selectedForReplace.getKey(), newDragon), user);
                            showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                            collectionManager.sync();
                            loadDataFromCollectionManager();
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Bruh", "Sorry, but you can not modify someones dragon");
                    }

                }
                break;

            case "remove_key":
                DragonDisplayWrapper selectedForRemove = table.getSelectionModel().getSelectedItem();
                if(selectedForRemove != null){
                    if (selectedForRemove.getOwner().equals(user.getLogin())) {
                        String response = commandsManager.removeKeyDragon(new String[]{selectedForRemove.getKey()}, user);
                        showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                        collectionManager.sync();
                        loadDataFromCollectionManager();

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Bruh", "Sorry, but you can not modify someones dragon");
                    }

                }
                break;

            case "remove_greater":
                DragonDisplayWrapper selectedForRemoveGr = table.getSelectionModel().getSelectedItem();
                if(selectedForRemoveGr != null){
                    if (selectedForRemoveGr.getOwner().equals(user.getLogin())) {
                        String response = commandsManager.removeKeyGrDragon(new String[]{selectedForRemoveGr.getKey()}, user);
                        showAlert(Alert.AlertType.INFORMATION, "Execution result", response);
                        collectionManager.sync();
                        loadDataFromCollectionManager();

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Bruh", "Sorry, but you can not modify someones dragon");
                    }

                }
                break;

            case "info":
                collectionManager.sync();
                loadDataFromCollectionManager();
                Map<String, Object> info = collectionManager.getCollectionInfoMap();
                showAlert(Alert.AlertType.INFORMATION, "Collection Info",
                        "Type: " + info.get("Type") + "\n" +
                                "Initialization Date: " + info.get("Date") + "\n" +
                                "Number of elements: " + info.get("ElementsQuantity"));
                break;

            case "show":
            default:
                System.out.println("Displaying current collection state.");
                collectionManager.sync();
                loadDataFromCollectionManager();
                break;
        }
    }

    private void loadDataFromCollectionManager() {
        masterData.clear();
        Map<String, Dragon> collection = collectionManager.getCollection();
        for (Map.Entry<String, Dragon> entry : collection.entrySet()) {
            masterData.add(new DragonDisplayWrapper(entry.getKey(), entry.getValue()));
        }
        table.sort();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Вспомогательный класс-обертка для удобного отображения в TableView.
     * Он "уплощает" объект Dragon, делая его поля доступными через простые геттеры.
     */

}

