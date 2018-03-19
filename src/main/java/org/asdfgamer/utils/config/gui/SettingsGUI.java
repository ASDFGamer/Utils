package org.asdfgamer.utils.config.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.asdfgamer.utils.config.Caption;
import org.asdfgamer.utils.config.SettingClassInfo;
import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.SettingsProperty;
import org.asdfgamer.utils.config.sort.CaptionElement;
import org.asdfgamer.utils.config.sort.ListElement;
import org.asdfgamer.utils.config.sort.SettingElement;
import org.asdfgamer.utils.config.sort.SettingsSorter;
import org.asdfgamer.utils.other.Convertible;
import org.asdfgamer.utils.other.Utils;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.*;

/**
 * This returns a Scene that can be used to view and change the Settings.
 * It can be customized with different CSS Styles
 */
public class SettingsGUI
{

    /**
     * This is the used logger.
     */
    private final static Logger LOG = getLogger(SettingsGUI.class.getName());

    /**
     * This is the Settings Object used to save the Settings.
     */
    private final Settings SETTINGS;

    /**
     * This is the array that holds the css styles. //TODO should it be an normal string?
     */
    private String[] css = new String[5];

    /**
     * This is the scene that gets created.
     */
    private Scene scene = null;

    /**
     * This is the text on the Save Button.
     */
    private String saveText;

    /**
     * This creates a new SettingsGui.
     *
     * @param settings The Settings that should be used to save the Settings.
     */
    public SettingsGUI(Settings settings)
    {

        this.SETTINGS = settings;
    }

    //TODO
    public void addCSS(String[] CSS)
    {

        this.css = CSS;
    }

    /**
     * This returns the Scene for the settings.
     *
     * @return A scene where you can change the settings.
     */
    public Scene getScene()
    {

        if (scene == null)
        {
            scene = createScene();
        }
        return scene;
    }

    /**
     * This creates the scene if it isn't present.
     *
     * @return The new Scene.
     */
    private Scene createScene()
    {
        //TODO check if there is only one Class pictured and remove the Tabpane then.
        TabPane tabPane = new TabPane();
        addTabs(tabPane);
        Scene scene = new Scene(tabPane);
        //TODO add CSS
        return scene;
    }

    /**
     * This Adds Tabs on the Tabpane for every Class and fills them.
     *
     * @param tabPane The tabPane that should get the Settings.
     */
    private void addTabs(TabPane tabPane)
    {

        Set<String> classes = SettingClassInfo.getClasses();
        for (String classname : classes)
        {
            if (showClass(classname))
            {
                Tab tab = createTab(classname);
                if (tab.getContent() instanceof GridPane && ((GridPane) tab.getContent()).getRowCount() > 1)
                {
                    tabPane.getTabs().add(tab);
                }
            }
        }
    }

    /**
     * This checks if the given Class should be shown on the GUI.
     *
     * @param classname The Class that should be checked.
     * @return true, if it should be shown, otherwise false.
     */
    private boolean showClass(String classname)
    {

        try
        {
            return Class.forName(classname).getAnnotation(NoGUI.class) == null;
        } catch (ClassNotFoundException e)
        {
            LOG.warning(bundle.getString("classNotFound") + classname);
            return false;
        }
    }

    /**
     * This creates an Tab for a Class.
     *
     * @param classname The Class for the Tab.
     * @return The created tab.
     */
    private Tab createTab(String classname)
    { //TODO add possibility to scroll

        Tab tab = new Tab();
        tab.setText(getClassNameView(classname));
        tab.setClosable(false);
        GridPane gridPane = new GridPane();
        try
        {
            List<SettingsProperty> settings = getSettingsFromObject(Class.forName(classname));
            SettingsSorter sorter = new SettingsSorter();
            sorter.add(settings);
            sorter.add(Class.forName(classname));
            List<ListElement> elements = sorter.getSortedList();
            for (ListElement element : elements)
            {
                if (element instanceof CaptionElement)
                {
                    addCaption(gridPane, (CaptionElement) element);
                } else if (element instanceof SettingElement)
                {
                    addSetting(gridPane, (SettingElement) element);
                }
            }
            addBottomLine(gridPane, Class.forName(classname));
        } catch (ClassNotFoundException e)
        {
            LOG.warning(bundle.getString("classNotFound") + classname);
        }
        tab.setContent(gridPane);

        return tab;
    }

    /**
     * This adds an bottom Line to the Tab where you can save the Settings and load the default Settings.
     *
     * @param gridPane  The Gridpane from the Tab
     * @param classname The Classname of the Tab.
     */
    private void addBottomLine(GridPane gridPane, Object classname)
    {

        int next = 0;
        Button ok = new Button("OK");
        int row = gridPane.getRowCount();
        {
            saveText = bundle.getString("save");
            Button save = new Button(saveText);
            save.setDefaultButton(true);
            save.setOnAction(event -> SETTINGS.save(classname));

            gridPane.add(save, next, row);
            next++;
        } //TODO how can i signalize, that the scene can be changed?
        gridPane.add(ok, next, row);
        next++;
        Button defaultSettings = new Button(bundle.getString("defaultSettings"));
        defaultSettings.setOnAction(event -> loadDefaultSettings(gridPane));
        gridPane.add(defaultSettings, next, row);
    }

    /**
     * This loads the default Settings for all Textfield and Checkbox elements on the gridpane
     *
     * @param gridPane The Gridpane with the Settings.
     */
    private void loadDefaultSettings(GridPane gridPane)
    {

        ObservableList<Node> children = gridPane.getChildren();
        DefaultValue defaultValue = new DefaultValue();
        Button save = null;
        for (Node child : children)
        {
            if (child instanceof Button)
            {
                if (((Button) child).getText().equals(saveText))
                {
                    save = (Button) child;
                }
            } else if (child instanceof TextField || child instanceof CheckBox || child instanceof ComboBox)
            {
                child.fireEvent(defaultValue);
            }
        }
        if (save == null)
        {
            LOG.warning(bundle.getString("cantFindSaveButton"));
        } else
        {
            LOG.warning("SettingsGUI.loadDefaultSettings Zeile 234");
            save.fire();
        }
    }

    /**
     * This adds an Caption to the GridPane.
     *
     * @param gridPane       The GridPane that should get the Caption.
     * @param captionElement The CaptionElement that should be added.
     */
    private void addCaption(GridPane gridPane, CaptionElement captionElement)
    {

        int row = gridPane.getRowCount();
        Label caption = new Label(captionElement.getContent());
        gridPane.add(caption, 0, row, 3, 1);

    }

    /**
     * This adds an Setting to the GridPane.
     *
     * @param gridPane       The GridPane that should get the Setting.
     * @param settingElement The Setting that should be added.
     */
    private void addSetting(GridPane gridPane, SettingElement settingElement)
    {

        int pos = gridPane.getRowCount();
        Label name = new Label(settingElement.getName());
        gridPane.add(name, 0, pos);

        gridPane.add(getSettingChangeElement(settingElement.getSetting()), 1, pos);

        gridPane.add(new Label(settingElement.getSetting().getInformationText()), 2, pos);
    }

    /**
     * This selects the right control element for every setting and creates it.
     *
     * @param setting The Setting that should get the control element..
     * @return The new control element..
     */
    private Control getSettingChangeElement(SettingsProperty setting)
    {

        switch (setting.getType())
        {
            case Double:
                return createDoubleElement(setting);
            case Integer:
                return createIntegerElement(setting);
            case Boolean:
                return createBooleanElement(setting);
            case String:
                return createStringElement(setting);
            case Enum:
                return createEnumElement(setting);
            default:
                return new Label(bundle.getString("internalError"));
        }
    }

    private ComboBox<Enum> createEnumElement(SettingsProperty setting)
    {

        ComboBox<Enum> comboBox = new ComboBox<>();
        Enum[] elements = Utils.getAllEnumElements(setting.getEnum());
        comboBox.getItems().addAll(elements);
        comboBox.getSelectionModel().select(setting.getEnum());
        comboBox.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            LOG.warning("SettingsGUI.createEnumElement Zeile 319");
            setting.set(comboBox.getValue());

        });

        comboBox.setOnAction(event ->
        {
            if (event instanceof DefaultValue)
            {
                LOG.info(Utils.getEnumElement(setting.getDefaultValue()) + "");
                comboBox.getSelectionModel().select(Utils.getEnumElement(setting.getDefaultValue()));
                setting.setToDefaultValue();
            }
        });
        return comboBox;
    }

    /**
     * This creates an new TextField to edit an String Setting.
     *
     * @param setting The Setting that should be edited.
     * @return The newly created TextField.
     */
    private TextField createStringElement(SettingsProperty setting)
    {

        TextField text = new TextField();
        text.setText(setting.get());

        text.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                setting.set(text.getText());
            }
        });

        text.setOnAction(event ->
        {
            if (event instanceof DefaultValue)
            {
                text.setText(setting.getDefaultValue());
                setting.setToDefaultValue();
            }
        });
        return text;
    }

    /**
     * This creates an new CheckBox to change an Boolean Setting.
     *
     * @param setting The Setting that should be edited.
     * @return The newly created CheckBox.
     */
    private CheckBox createBooleanElement(SettingsProperty setting)
    {

        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(setting.getBoolean());

        checkBox.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                setting.setBoolean(checkBox.isSelected());
            }
        });

        checkBox.setOnAction(event ->
        {
            if (event instanceof DefaultValue)
            {
                checkBox.setSelected(Utils.isTrue(setting.getDefaultValue(), TRUE_VALUES));
                setting.setToDefaultValue();
            }
        });
        return checkBox;
    }

    /**
     * This creates an new TextField to edit an double Setting.
     *
     * @param setting The Setting that should be edited.
     * @return The newly created TextField.
     */
    private TextField createDoubleElement(SettingsProperty setting)
    {

        TextField number = new TextField();
        number.setText(setting.get());

        number.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue && Convertible.toDouble(number.getText()))
            {
                setting.set(number.getText());
            }
        });

        number.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!Convertible.toDouble(newValue))
            {
                number.setStyle("-fx-text-fill: red");
            } else
            {
                number.setStyle("-fx-text-fill: black");//TODO set the color that is defined in the given CSS
            }
        });
        number.setOnAction(event ->
        {
            if (event instanceof DefaultValue)
            {
                number.setText(setting.getDefaultValue());
                setting.setToDefaultValue();
            }
        });
        return number;
    }

    /**
     * This creates an new TextField to edit an integer Setting.
     *
     * @param setting The Setting that should be edited.
     * @return The newly created TextField.
     */
    private TextField createIntegerElement(SettingsProperty setting)
    {

        TextField number = new TextField();
        number.setText(setting.get());

        number.focusedProperty().addListener((observable, oldValue, newValue) -> //TODO save on change or save on save button click?
        {
            if (!newValue && Convertible.toInt(number.getText()))
            {
                setting.set(number.getText());
            }
        });

        number.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!Convertible.toInt(newValue))
            {
                number.setStyle("-fx-text-fill: red");
            } else
            {
                number.setStyle("-fx-text-fill: black");//TODO set the color that is defined in the given CSS
            }
        });
        number.setOnAction(event ->
        {
            if (event instanceof DefaultValue)
            {
                number.setText(setting.getDefaultValue());
                setting.setToDefaultValue();
            }
        });
        return number;
    }

    /**
     * This gets the Name of the Class Name that should be shown.
     *
     * @param classname The Classname that is in question.
     * @return The Name that gets shown.
     */
    private String getClassNameView(String classname)
    {

        try
        {
            Caption caption = Class.forName(classname).getAnnotation(Caption.class);
            if (caption != null)
            {
                ResourceBundle bundle = Settings.getResourceBundle();
                if (bundle != null && bundle.containsKey(caption.value()))
                {
                    return bundle.getString(caption.value());
                }
                return caption.value();
            }

        } catch (ClassNotFoundException e)
        {
            LOG.info(bundle.getString("classNotFound") + classname);
        }
        return classname;
    }

    /**
     * This is used to reset the values to the default value.
     */
    private class DefaultValue extends ActionEvent
    {

    }
}
