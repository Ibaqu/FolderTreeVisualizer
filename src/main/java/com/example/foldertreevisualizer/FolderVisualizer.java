package com.example.foldertreevisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FolderVisualizer extends Application {

    private TreeView<File> treeView;
    private TextField pathField;
    private Label fileNameLabel;
    private Label fileExtensionLabel;
    private Label fileSizeLabel;
    private Label fileCreationDateLabel;

    @Override
    public void start(Stage primaryStage) {
        treeView = new TreeView<>();
        treeView.setCellFactory(param -> new FileTreeCell());
        pathField = new TextField();

        fileNameLabel = new Label();
        fileExtensionLabel = new Label();
        fileSizeLabel = new Label();
        fileCreationDateLabel = new Label();

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> browse());

        HBox pathBox = new HBox(pathField, browseButton);
        VBox fileInfoBox = new VBox(10);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label fileLabel = new Label("File name : ");
        gridPane.add(fileLabel, 0, 0);
        gridPane.add(fileNameLabel, 1, 0);

        Label extensionLabel = new Label("File extension : ");
        gridPane.add(extensionLabel, 0, 1);
        gridPane.add(fileExtensionLabel, 1, 1);

        Label sizeLabel = new Label("File size : ");
        gridPane.add(sizeLabel, 0, 2);
        gridPane.add(fileSizeLabel, 1, 2);

        Label dateLabel = new Label("File creation date : ");
        gridPane.add(dateLabel, 0, 3);
        gridPane.add(fileCreationDateLabel, 1, 3);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            if (treeView.getSelectionModel().getSelectedItem() != null) {
                File file = treeView.getSelectionModel().getSelectedItem().getValue();
                if (file.delete()) {
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete file");
                }
            }
        });

        fileInfoBox.getChildren().addAll(gridPane, deleteButton);

        VBox root = new VBox(pathBox, treeView, fileInfoBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Folder Visualizer");
        primaryStage.show();
    }

    private void browse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getAbsolutePath());
            treeView.setRoot(new FileTreeItem(selectedDirectory));
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        } else {
            return fileName.substring(dotIndex + 1);
        }
    }

    private String getFileSize(File file) {
        if (file.isDirectory()) {
            return "Folder";
        } else {
            return String.format("%.2f KB", file.length() / 1024.0);
        }
    }

    private String getFileCreationDate(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Instant instant = attr.creationTime().toInstant();
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return dateTime.format(formatter);
        } catch (IOException e) {
            return "Unknown";
        }
    }

    private class FileTreeCell extends TreeCell<File> {

        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getName());
            }
        }
    }

    private class FileTreeItem extends TreeItem<File> {

        public FileTreeItem(File file) {
            super(file);
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    getChildren().add(new FileTreeItem(child));
                }
            }
        }

        @Override
        public boolean isLeaf() {
            return getValue().isFile();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}