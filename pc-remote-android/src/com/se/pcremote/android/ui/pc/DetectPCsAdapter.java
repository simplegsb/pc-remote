package com.se.pcremote.android.ui.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.pcremote.android.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);

        View insertView = view.findViewById(R.id.detect_pcs_insert);
        TextView hostNameView = (TextView) view.findViewById(R.id.detect_pcs_host_name);
        TextView existsStatusView = (TextView) view.findViewById(R.id.detect_pcs_exists_status);
        TextView serverDetectionStatusView = (TextView) view.findViewById(R.id.detect_pcs_server_detection_status);

        // For some reason I can't apply these styles in XML.
        hostNameView.setTextAppearance(hostNameView.getContext(), android.R.style.TextAppearance_Large);
        existsStatusView.setTextAppearance(existsStatusView.getContext(), android.R.style.TextAppearance_Small);
        serverDetectionStatusView.setTextAppearance(view.getContext(), android.R.style.TextAppearance_Small);

        if (isEnabled(position))
        {
            insertView.setVisibility(View.VISIBLE);
            existsStatusView.setVisibility(View.INVISIBLE);
        }
        else
        {
            insertView.setVisibility(View.INVISIBLE);
            existsStatusView.setVisibility(View.VISIBLE);
        }

        return (view);
    }

    @Override
    public boolean isEnabled(final int position)
    {
        @SuppressWarnings("unchecked")
        HashMap<String, String> detectedPcMap = (HashMap<String, String>) getItem(position);

        return (!Boolean.parseBoolean(detectedPcMap.get("pc_exists")));
    }
}
