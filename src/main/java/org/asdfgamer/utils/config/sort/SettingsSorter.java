package org.asdfgamer.utils.config.sort;

import org.asdfgamer.utils.config.Caption;
import org.asdfgamer.utils.config.Setting;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * This Class sorts the given Settings and Captions in an predefined order.
 */
public class SettingsSorter
{

    /**
     * This is the used comparator.
     */
    private final Comparator<ListElement> comparator;

    /**
     * This is the List where all Elements are sorted.
     */
    private final List<ListElement> content = new LinkedList<>();

    /**
     * This creates an new SettingsSorter witch sorts the elements in the order they are in the class.
     */
    public SettingsSorter()
    {

        this.comparator = new WrittenOrder();
    }

    /**
     * This creates an new SettingsSorter with an custom comparator.
     *
     * @param comparator The custom Comparator.
     */
    @SuppressWarnings("unused")
    public SettingsSorter(Comparator<ListElement> comparator)
    {

        this.comparator = comparator;
    }

    /**
     * This adds an new Setting to the List
     *
     * @param setting The setting itself.
     */
    public void add(Setting setting)
    {

        content.add(new SettingElement(setting));
        content.sort(comparator);
    }

    /**
     * This is used to add all Captions of the class to the List in the right order.
     *
     * @param classObject The Class where the Settings and Captions are defined.
     */
    public void add(Class classObject)
    {

        Map<String, Caption> captions = getFieldAnnotations(Caption.class, classObject);
        captions.forEach((name, caption) ->
        {
            for (int i = 0; i < content.size(); i++)//Nested for loop could be better
            {
                if ((content.get(i) instanceof SettingElement) && (((SettingElement) content.get(i)).getName().equals(name)))
                {
                    content.add(i, new CaptionElement(content.get(i).getLineNumber(), caption));
                    content.sort(comparator);
                    break;
                }
            }
        });
    }

    /**
     * This is used to add many Settings to the sorter.
     *
     * @param settings The List with all Settings that should be added.
     */
    public void add(List<Setting> settings)
    {

        for (Setting setting : settings)
        {
            content.add(new SettingElement(setting));
        }
        content.sort(comparator);
    }

    /**
     * This returns the List with all Elements sorted in the given Way (normally the order they are declared in the class)
     *
     * @return A List with all sorted elements
     */
    public List<ListElement> getSortedList()
    {

        return content;
    }

    /**
     * This returns all Field Annotations of the given Type from the given Class.
     *
     * @param annotationClass The Type of the Annotation.
     * @param classObject     The Class where the annotations should be searched.
     * @param <T>             The Type of the Annotation.
     * @return An Map with the Name of the Annotated Field and the responding Annotation.
     */
    private <T extends Annotation> Map<String, T> getFieldAnnotations(Class<T> annotationClass, Class classObject)
    {

        Map<String, T> elements = new HashMap<>();
        Field[] fields = classObject.getFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(annotationClass))
            {
                elements.put(field.getName(), field.getAnnotation(annotationClass));
            }
        }
        return elements;
    }
}
