package org.asdfgamer.utils.config.gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.asdfgamer.utils.config.*;
import org.asdfgamer.utils.other.Utils;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.SettingUtils.bundle;
import static org.asdfgamer.utils.config.SettingUtils.getSettingsFromEnum;

/**
 * This returns a Scene that can be used to view and change the Settings.
 * It can be customized with different CSS Styles
 */
public class SettingsGUI
{

    private final static Logger LOG = getLogger(SettingsGUI.class.getName());

    private final Settings SETTINGS;

    private String[] css = new String[5];

    private Scene scene = null;

    public SettingsGUI(Settings settings)
    {

        this.SETTINGS = settings;
    }

    public void addCSS(String[] CSS)
    {

        this.css = CSS;
    }

    public Scene getScene()
    {

        if (scene == null)
        {
            scene = createScene();
        }
        return scene;
    }

    private Scene createScene()
    {

        TabPane tabPane = new TabPane();
        addTabs(tabPane);
        Scene scene = new Scene(tabPane);
        return scene;
    }

    private void addTabs(TabPane tabPane)
    {

        Set<String> classes = SettingClassInfo.getClasses();
        for (String classname : classes)
        {
            if (showClass(classname))
            {
                Tab tab = createTab(classname);
                if (tab.getContent() instanceof GridPane && ((GridPane) tab.getContent()).getRowCount() > 0)
                {
                    tabPane.getTabs().add(tab);
                }
            }
        }
    }

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

    private Tab createTab(String classname)
    {

        Tab tab = new Tab();
        tab.setText(getClassNameView(classname));
        GridPane gridPane = new GridPane();
        try
        {
            Map<String, SettingsProperty> settings;
            if (Utils.isEnum(Class.forName(classname)))
            {
                settings = getSettingsFromEnum(classname);
            } else
            {
                settings = Utils.getFields(Class.forName(classname));
            }
            for (Map.Entry<String, SettingsProperty> entry : settings.entrySet())
            {
                addSetting(gridPane, entry.getKey(), entry.getValue());
            }
        } catch (ClassNotFoundException e)
        {
            LOG.warning(bundle.getString("classNotFound") + classname);
        }
        tab.setContent(gridPane);

        return tab;
    }

    private void addSetting(GridPane gridPane, String name, SettingsProperty setting)
    {

        int pos = gridPane.getRowCount();
        Label nameLabel = new Label(name);
        gridPane.add(nameLabel, 0, pos);

        gridPane.add(getSettingChangeElement(setting), 1, pos);

        gridPane.add(new Label(setting.getInformationText()), 2, pos);
    }

    private Node getSettingChangeElement(SettingsProperty setting)
    {

        switch (setting.getType())
        {
            case Double:
            case Integer:
                TextField number = new TextField();
                //TODO set content
                return number;
            case Boolean:
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(setting.getBoolean());
                return checkBox;
            case String:
                TextField text = new TextField();
                return text;
            case Enum:
            default:
                return new Label(bundle.getString("internalError"));//TODO add
        }
    }

    private String getClassNameView(String classname)
    {

        try
        {
            Caption caption = Class.forName(classname).getAnnotation(Caption.class);
            if (caption != null)
            {
                return caption.value();
            }

        } catch (ClassNotFoundException e)
        {
            LOG.info(bundle.getString("classNotFound") + classname);
        }
        return classname;
    }
}
