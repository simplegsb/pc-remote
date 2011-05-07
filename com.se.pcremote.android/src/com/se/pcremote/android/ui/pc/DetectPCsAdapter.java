package com.se.pcremote.android.ui.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

/**
 * <p>
 * An adapter that disables rows representing existing {@link com.se.pcremote.android.PC PC}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class DetectPCsAdapter extends SimpleAdapter
{
    /**
     * <p>
     * Creates an instance of <code>DetectPCsAdapter</code>.
     * </p>
     * 
     * @param context The context in which the list is to be displayed.
     * @param data The data to display in the list.
     * @param resource The {@link android.view.View View} used to represent a row in the list.
     * @param from The keys of the data to map the <code>View</code>s in the given resource from.
     * @param to The <code>View</code>s in the given resource to map the keys to.
     */
    public DetectPCsAdapter(final Context context, final List<? extends Map<String, ?>> data, final int resource, final String[] from, final int[] to)
    {
        super(context, data, resource, from, to);
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return (false);
    }

    @Override
    public boolean isEnabled(final int position)
    {
        @SuppressWarnings("unchecked")
        HashMap<String, String> detectedPcMap = (HashMap<String, String>) getItem(position);

        return (!Boolean.parseBoolean(detectedPcMap.get("pc_exists")));
    }
}
