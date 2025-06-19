package gui.screens;

import gui.managers.LocaleManager;
import structs.wrappers.DragonDisplayWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import structs.classes.*;

import java.util.Map;

public class DragonFormScreen {
    private final LocaleManager localeManager = LocaleManager.getInstance();
    private Dragon resultDragon = null;
    private DragonDisplayWrapper resultEntry = null;

    public DragonDisplayWrapper getNewDragon() {
        this.resultEntry = null;
        showEntryForm(localeManager.getString("form.title.create"));
        return this.resultEntry;
    }

    public Dragon updateDragon(DragonDisplayWrapper dragonWrapper) {
        this.resultDragon = null;
        showDragonForm(localeManager.getString("form.title.update")+" " + dragonWrapper.getKey(), dragonWrapper.getOriginalDragon());
        return this.resultDragon;
    }

    private void showDragonForm(String title, Dragon existingDragon) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField xField = new TextField();
        TextField yField = new TextField();
        TextField ageField = new TextField();
        ComboBox<Color> colorBox = new ComboBox<>();
        colorBox.getItems().setAll(Color.values());
        ComboBox<DragonType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(DragonType.values());
        ComboBox<DragonCharacter> characterBox = new ComboBox<>();
        characterBox.getItems().setAll(DragonCharacter.values());

        CheckBox hasCaveCheck = new CheckBox();
        TextField depthField = new TextField();
        TextField treasuresField = new TextField();

        depthField.disableProperty().bind(hasCaveCheck.selectedProperty().not());
        treasuresField.disableProperty().bind(hasCaveCheck.selectedProperty().not());

        grid.add(new Label(localeManager.getString("form.label.name")), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(localeManager.getString("form.label.cordX")), 0, 1);
        grid.add(xField, 1, 1);
        grid.add(new Label(localeManager.getString("form.label.cordY")), 0, 2);
        grid.add(yField, 1, 2);
        grid.add(new Label(localeManager.getString("form.label.age")), 0, 3);
        grid.add(ageField, 1, 3);
        grid.add(new Label(localeManager.getString("form.label.color")), 0, 4);
        grid.add(colorBox, 1, 4);
        grid.add(new Label(localeManager.getString("form.label.type")), 0, 5);
        grid.add(typeBox, 1, 5);
        grid.add(new Label(localeManager.getString("form.label.character")), 0, 6);
        grid.add(characterBox, 1, 6);
        grid.add(new Label(localeManager.getString("form.label.hasCave")), 0, 7);
        grid.add(hasCaveCheck, 1, 7);
        grid.add(new Label(localeManager.getString("form.label.caveDepth")), 0, 8);
        grid.add(depthField, 1, 8);
        grid.add(new Label(localeManager.getString("form.label.caveTreasures")), 0, 9);
        grid.add(treasuresField, 1, 9);

        if (existingDragon != null) {
            nameField.setText(existingDragon.getName());
            xField.setText(String.valueOf(existingDragon.getCoordinates().getX()));
            yField.setText(String.valueOf(existingDragon.getCoordinates().getY()));
            ageField.setText(String.valueOf(existingDragon.getAge()));
            colorBox.setValue(existingDragon.getColor());
            typeBox.setValue(existingDragon.getType());
            characterBox.setValue(existingDragon.getCharacter());

            if (existingDragon.getCave() != null) {
                hasCaveCheck.setSelected(true);
                depthField.setText(String.valueOf(existingDragon.getCave().getDepth()));
                treasuresField.setText(String.valueOf(existingDragon.getCave().getNumberOfTreasures()));
            }
        } else {
            colorBox.getSelectionModel().selectFirst();
            typeBox.getSelectionModel().selectFirst();
        }

        Button okButton = new Button(localeManager.getString("form.button.ok"));
        Button cancelButton = new Button(localeManager.getString("form.button.cancel"));

        okButton.setOnAction(e -> {
            if (validateAndCreateDragon(nameField, xField, yField, ageField, colorBox, typeBox, characterBox, hasCaveCheck, depthField, treasuresField)) {
                dialogStage.close();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(10, cancelButton, okButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 10);

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void showEntryForm(String title) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField xField = new TextField();
        TextField yField = new TextField();
        TextField ageField = new TextField();
        ComboBox<Color> colorBox = new ComboBox<>();
        colorBox.getItems().setAll(Color.values());
        ComboBox<DragonType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(DragonType.values());
        ComboBox<DragonCharacter> characterBox = new ComboBox<>();
        characterBox.getItems().setAll(DragonCharacter.values());

        CheckBox hasCaveCheck = new CheckBox();
        TextField depthField = new TextField();
        TextField treasuresField = new TextField();

        depthField.disableProperty().bind(hasCaveCheck.selectedProperty().not());
        treasuresField.disableProperty().bind(hasCaveCheck.selectedProperty().not());

        grid.add(new Label("Key:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label(localeManager.getString("form.label.name")), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label(localeManager.getString("form.label.cordX")), 0, 2);
        grid.add(xField, 1, 2);
        grid.add(new Label(localeManager.getString("form.label.cordY")), 0, 3);
        grid.add(yField, 1, 3);
        grid.add(new Label(localeManager.getString("form.label.age")), 0, 4);
        grid.add(ageField, 1, 4);
        grid.add(new Label(localeManager.getString("form.label.color")), 0, 5);
        grid.add(colorBox, 1, 5);
        grid.add(new Label(localeManager.getString("form.label.type")), 0, 6);
        grid.add(typeBox, 1, 6);
        grid.add(new Label(localeManager.getString("form.label.character")), 0, 7);
        grid.add(characterBox, 1, 7);
        grid.add(new Label(localeManager.getString("form.label.hasCave")), 0, 8);
        grid.add(hasCaveCheck, 1, 8);
        grid.add(new Label(localeManager.getString("form.label.caveDepth")), 0, 9);
        grid.add(depthField, 1, 9);
        grid.add(new Label(localeManager.getString("form.label.caveTreasures")), 0, 10);
        grid.add(treasuresField, 1, 10);


        colorBox.getSelectionModel().selectFirst();
        typeBox.getSelectionModel().selectFirst();
        characterBox.getSelectionModel().selectFirst();

        Button okButton = new Button(localeManager.getString("form.button.ok"));
        Button cancelButton = new Button(localeManager.getString("form.button.cancel"));

        okButton.setOnAction(e -> {
            if (validateAndCreateEntry(idField, nameField, xField, yField, ageField, colorBox, typeBox, characterBox, hasCaveCheck, depthField, treasuresField)) {
                dialogStage.close();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(10, cancelButton, okButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 11);

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }


    private boolean validateAndCreateDragon(TextField nameField, TextField xField, TextField yField, TextField ageField,
                                            ComboBox<Color> colorBox, ComboBox<DragonType> typeBox, ComboBox<DragonCharacter> characterBox,
                                            CheckBox hasCaveCheck, TextField depthField, TextField treasuresField) {
        try {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(localeManager.getString("form.error.name"));
            }

            double x = Double.parseDouble(xField.getText().trim());
            long y = Long.parseLong(yField.getText().trim());
            if (y > 984) {
                throw new IllegalArgumentException(localeManager.getString("form.error.y"));
            }
            Coordinates coordinates = new Coordinates(x, y);

            int age = Integer.parseInt(ageField.getText().trim());
            if (age <= 0) {
                throw new IllegalArgumentException(localeManager.getString("form.error.age"));
            }

            Color color = colorBox.getValue();
            if (color == null) throw new IllegalArgumentException(localeManager.getString("form.error.color"));

            DragonType type = typeBox.getValue();
            if (type == null) throw new IllegalArgumentException(localeManager.getString("form.error.type"));
            DragonCharacter character = characterBox.getValue();

            DragonCave cave = null;
            if (hasCaveCheck.isSelected()) {
                if (depthField.getText().trim().isEmpty() || treasuresField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException(localeManager.getString("form.error.cave"));
                }
                int depth = Integer.parseInt(depthField.getText().trim());
                double treasures = Double.parseDouble(treasuresField.getText().trim());
                if (treasures <= 0) {
                    throw new IllegalArgumentException(localeManager.getString("form.error.treasures"));
                }
                cave = new DragonCave(depth, treasures);
            }

            this.resultDragon = new Dragon(name, coordinates, age, color, type, character, cave);
            return true;

        } catch (NumberFormatException nfe) {
            showAlert(localeManager.getString("form.error.title"), localeManager.getString("form.error.number"));
            return false;
        } catch (IllegalArgumentException iae) {
            showAlert(localeManager.getString("form.error.title"), iae.getMessage());
            return false;
        }
    }

    private boolean validateAndCreateEntry(TextField idField, TextField nameField, TextField xField, TextField yField, TextField ageField,
                                           ComboBox<Color> colorBox, ComboBox<DragonType> typeBox, ComboBox<DragonCharacter> characterBox,
                                           CheckBox hasCaveCheck, TextField depthField, TextField treasuresField) {
        try {
            String id = idField.getText();
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException(localeManager.getString("form.error.id"));
            }

            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(localeManager.getString("form.error.name"));
            }

            double x = Double.parseDouble(xField.getText().trim());
            long y = Long.parseLong(yField.getText().trim());
            if (y > 984) {
                throw new IllegalArgumentException(localeManager.getString("form.error.y"));
            }
            Coordinates coordinates = new Coordinates(x, y);

            int age = Integer.parseInt(ageField.getText().trim());
            if (age <= 0) {
                throw new IllegalArgumentException(localeManager.getString("form.error.age"));
            }

            Color color = colorBox.getValue();
            if (color == null) throw new IllegalArgumentException(localeManager.getString("form.error.color"));

            DragonType type = typeBox.getValue();
            if (type == null) throw new IllegalArgumentException(localeManager.getString("form.error.type"));
            DragonCharacter character = characterBox.getValue();

            DragonCave cave = null;
            if (hasCaveCheck.isSelected()) {
                if (depthField.getText().trim().isEmpty() || treasuresField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException(localeManager.getString("form.error.cave"));
                }
                int depth = Integer.parseInt(depthField.getText().trim());
                double treasures = Double.parseDouble(treasuresField.getText().trim());
                if (treasures <= 0) {
                    throw new IllegalArgumentException(localeManager.getString("form.error.treasures"));
                }
                cave = new DragonCave(depth, treasures);
            }

            this.resultEntry = new DragonDisplayWrapper(id, new Dragon(name, coordinates, age, color, type, character, cave));
            return true;

        } catch (NumberFormatException nfe) {
            showAlert(localeManager.getString("form.error.title"), localeManager.getString("form.error.number"));
            return false;
        } catch (IllegalArgumentException iae) {
            showAlert(localeManager.getString("form.error.title"), iae.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.titleProperty().bind(localeManager.createStringBinding(title));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}