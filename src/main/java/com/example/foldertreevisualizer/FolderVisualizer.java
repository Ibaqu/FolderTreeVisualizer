package com.example.foldertreevisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class FolderVisualizer extends Application {

    private TreeView<String> treeView;
    private TextField pathField;

    @Override
    public void start(Stage primaryStage) {
        treeView = new TreeView<>();
        pathField = new TextField();

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> browse());

        HBox pathBox = new HBox(pathField, browseButton);
        VBox root = new VBox(pathBox, treeView);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Folder Visualizer");
        primaryStage.show();
    }

    private void browse() {
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) {
            displayFolder(selectedDirectory);
        }
    }

    private void displayFolder(File directory) {
        pathField.setText(directory.getAbsolutePath());
        TreeItem<String> rootItem = new TreeItem<>(directory.getName());
        treeView.setRoot(rootItem);
        populateTreeView(directory, rootItem);
    }

    private void populateTreeView(File directory, TreeItem<String> parentItem) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                TreeItem<String> newItem = new TreeItem<>(file.getName());
                parentItem.getChildren().add(newItem);
                if (file.isDirectory()) {
                    populateTreeView(file, newItem);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}