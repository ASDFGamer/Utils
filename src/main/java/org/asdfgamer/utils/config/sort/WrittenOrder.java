package org.asdfgamer.utils.config.sort;

import java.util.Comparator;

/**
 * This sorts the Elements in the order they are written in the classes.
 */
public class WrittenOrder implements Comparator<ListElement>
{

    @Override
    public int compare(ListElement o1, ListElement o2)
    {

        int result = Integer.compare(o1.getLineNumber(), o2.getLineNumber());
        if (result == 0)
        {
            if (o1 instanceof CaptionElement)
            {
                result = -1;
            } else if (o2 instanceof CaptionElement)
            {
                result = 1;
            }
        }
        return result;
    }
}
