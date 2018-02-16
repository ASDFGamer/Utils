package org.asdfgamer.utils.config.sort;

import org.asdfgamer.utils.config.Caption;

/**
 * This Class is used to create Elements that can be added to an List of ListElements.
 */
class CaptionElement extends ListElement
{

    /**
     * This creates an {@link ListElement} for a Caption that can be sorted.
     *
     * @param lineNumber The Linenumber of the field that belongs to the Caption-annotation.
     * @param content    The content of the Caption (The Value Element.
     */
    CaptionElement(int lineNumber, Caption content)
    {

        super(lineNumber, "\n#"+content.value() + "\n");
    }
}
