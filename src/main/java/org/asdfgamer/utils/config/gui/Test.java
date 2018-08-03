package org.asdfgamer.utils.config.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application
{
    public void start(Stage stage)
    {

        VBox view = new VBox();
        view.setPrefSize(600, 400);

        // Creating the root node
        final TreeItem<String> root = new TreeItem<>("Root node");
        root.setExpanded(true);

        // Creating the tree items that will be the first children of the root node
        // and the parent to the child nodes.
        final TreeItem<String> parentNode1 = new TreeItem<>("Parent node 1");
        final TreeItem<String> parentNode2 = new TreeItem<>("Parent node 2");
        final TreeItem<String> parentNode3 = new TreeItem<>("Parent node 3");

        // Creating the tree items that will be the children of the parent
        // nodes.
        final TreeItem<String> childNode1 = new TreeItem<>("Child Node 1");
        final TreeItem<String> childNode2 = new TreeItem<>("Child Node 2");
        final TreeItem<String> childNode3 = new TreeItem<>("Child Node 3");

        // Adding tree items to the root
        root.getChildren().setAll(parentNode1, parentNode2, parentNode3);

        // Add the child nodes to all children of the root
        for (TreeItem<String> parent : root.getChildren())
        {
            parent.getChildren().addAll(childNode1, childNode2, childNode3);
        }

        // Creating a tree table view
        final TreeView<String> treeView = new TreeView<>(root);

        // We set show root to false. This will hide the root and only show it's children in the Treeview.
        treeView.setShowRoot(false);

//        treeView.setCellFactory(e -> new ListCell());

        view.getChildren().add(treeView);

        Scene scene = new Scene(view);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args)
    {
        launch();
    }
}
