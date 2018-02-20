package org.asdfgamer.utils.config.sort;

/**
 * This is an Element of the List witch contains all Setting ans Captions so they can be used in the right order.
 */
public abstract class ListElement
{

    /**
     * This is the content of the Element.
     */
    private final String content;

    /**
     * This is the LineNumber of the Element (or the LineNumber of the associated Field if the element is an annotation).
     */
    private final int lineNumber;

    /**
     * This creates an new ListElement.
     * @param lineNumber The LineNumber of the Element (or the LineNumber of the associated Field if the element is an annotation).
     * @param content This is the content of the element
     */
    ListElement(int lineNumber, String content)
    {
        this.lineNumber = lineNumber;
        this.content = content;
    }

    /**
     * This returns the Content
     * @return The Content.
     */
    public String getContent()
    {
        return content;
    }

    /**
     * This returns the LineNumber.
     * @return The LineNumber
     */
    public int getLineNumber()
    {
        return lineNumber;
    }
}

