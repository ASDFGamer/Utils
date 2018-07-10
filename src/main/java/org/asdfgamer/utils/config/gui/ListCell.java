package org.asdfgamer.utils.config.gui;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;

/**
 * A custom cell that shows a checkbox, label and button in the
 * TreeCell.
 */
class ListCell extends TreeCell<Control>
{
    @Override
    protected void updateItem(Control item, boolean empty)
    {
        super.updateItem(item, empty);

        // If the cell is empty we don't show anything.
        if (isEmpty())
        {
            setGraphic(null);
            setText(null);
        } else
        {
            // We only show the custom cell if it is a leaf, meaning it has
            // no children.
            if (this.getTreeItem().isLeaf())
            {

                // A custom HBox that will contain your check box, label and
                // button.
                HBox cellBox = new HBox(10);

                CheckBox checkBox = new CheckBox();
                //Label label = new Label(item);
                Button button = new Button("Press!");
                // Here we bind the pref height of the label to the height of the checkbox. This way the label and the checkbox will have the same size.
                //label.prefHeightProperty().bind(checkBox.heightProperty());

                cellBox.getChildren().addAll(checkBox, /*label,*/ button);

                // We set the cellBox as the graphic of the cell.
                setGraphic(cellBox);
                setText(null);
            } else
            {
                // If this is the root we just display the text.
                setGraphic(null);
                //setText(item);
            }
        }
    }
}

