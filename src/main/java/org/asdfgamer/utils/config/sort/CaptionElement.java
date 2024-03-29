package org.asdfgamer.utils.config.sort;

import org.asdfgamer.utils.config.annotations.Caption;

/**
 * This Class is used to create Elements that can be added to an List of ListElements.
 */
public class CaptionElement extends ListElement
{

    /**
     * This creates an {@link ListElement} for a Caption that can be sorted.
     *
     * @param lineNumber The Linenumber of the field that belongs to the Caption-annotation.
     * @param caption    The Caption Element.
     */
    CaptionElement(int lineNumber, Caption caption)
    {

        super(lineNumber, caption.value());
    }
}
