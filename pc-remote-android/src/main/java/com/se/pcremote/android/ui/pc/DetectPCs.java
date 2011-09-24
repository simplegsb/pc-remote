package com.se.pcremote.android.ui.pc;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * Detects {@link com.se.pcremote.android.PC PC} in the local network.
 * </p>
 * 
 * @author Gary Buyn
 */
public class DetectPCs extends ListActivity
{
    /**
     * <p>
     * Maps of information about the detected {@link com.se.pcremote.android.PC PC}s to be displayed.
     * </p>
     */
    private List<HashMap<String, String>> fDetectedPcMaps;

    /**
     * <p>
     * A {@link android.view.View View} to be displayed when the detection of {@link com.se.pcremote.android.PC PC}s is in progress.
     * </p>
     */
    private View fInProgressView;

    /**
     * <p>
     * The task used to detect the {@link com.se.pcremote.android.PC PC}s.
     * </p>
     */
    private PCDetectionTask fPcDetectionTask;

    /**
     * <p>
     * The task used to detect an instance of PC Remote Server running on the {@link com.se.pcremote.android.PC PC}s.
     * </p>
     */
    private PCRemoteServerDetectionTask fPcRemoteServerDetectionTask;

    /**
     * <p>
     * Creates an instance of <code>DetectPCs</code>.
     * </p>
     */
    public DetectPCs()
    {
        fDetectedPcMaps = new ArrayList<HashMap<String, String>>();
        fPcDetectionTask = null;
        fPcRemoteServerDetectionTask = null;
        fInProgressView = null;
    }

    /**
     * <p>
     * Adds a map of information about a detected {@link com.se.pcremote.android.PC PC} to the maps of information to be displayed.
     * </p>
     * 
     * @param detectedPcMap The map of information to be added.
     */
    public void addDetectedPcMap(final HashMap<String, String> detectedPcMap)
    {
        fDetectedPcMaps.add(detectedPcMap);
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
    }

    /**
     * <p>
     * Retrieves a map of information about a detected {@link com.se.pcremote.android.PC PC} at the given address.
     * </p>
     * 
     * @param address The address to retrieve the map of information for.
     * 
     * @return A map of information about the detected {@link com.se.pcremote.android.PC PC} at the given address if a <code>PC</code> has been
     * detected there or <code>null</code> if the <code>PC</code> has not been detected.
     */
    public HashMap<String, String> getDetectedPcMap(final InetAddress address)
    {
        HashMap<String, String> detectedPcMap = null;

        for (HashMap<String, String> currentDetectedPcMap : fDetectedPcMaps)
        {
            if (currentDetectedPcMap.get("host_name").equals(address.getHostName()))
            {
                detectedPcMap = currentDetectedPcMap;
                break;
            }
        }

        return (detectedPcMap);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fInProgressView = LayoutInflater.from(this).inflate(R.layout.detect_pcs_in_progress, null);
        fInProgressView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        getListView().addFooterView(fInProgressView);
        setListAdapter(new DetectPCsAdapter(this, fDetectedPcMaps, R.layout.detect_pcs_item, new String[] {"host_name", "pc_exists_status",
                "server_detection_status"}, new int[] {R.id.detect_pcs_host_name, R.id.detect_pcs_exists_status, R.id.detect_pcs_server_detection_status}));

        getListView().setTextFilterEnabled(true);

        // Opens the 'Create/Edit PC' activity.
        getListView().setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
            {
                finish();

                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT, PCRemoteProvider.PC_URI);
                intent.putExtra("host", fDetectedPcMaps.get(position).get("host_name"));

                startActivity(intent);
            }
        });

        // Start two concurrent detection tasks, one to detect the PCs and another to detect the PC Remote Server running on those PCs.
        fPcDetectionTask = new PCDetectionTask(this);
        fPcDetectionTask.execute();

        fPcRemoteServerDetectionTask = new PCRemoteServerDetectionTask(this);
        fPcRemoteServerDetectionTask.execute();
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onDestroy()
    {
        if (fPcDetectionTask != null)
        {
            fPcDetectionTask.cancel(true);
        }
        if (fPcRemoteServerDetectionTask != null)
        {
            fPcRemoteServerDetectionTask.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(final Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the maps of information about the detected PCs.
        ArrayList<String> hostNames = savedInstanceState.getStringArrayList("hostNames");
        ArrayList<String> pcExists = savedInstanceState.getStringArrayList("pcExists");
        ArrayList<String> pcExistsStatuses = savedInstanceState.getStringArrayList("pcExistsStatuses");
        ArrayList<String> serverDetectionStates = savedInstanceState.getStringArrayList("serverDetectionStatuses");
        for (int index = 0; index < hostNames.size(); index++)
        {
            HashMap<String, String> detectedPcMap = new HashMap<String, String>();
            detectedPcMap.put("host_name", hostNames.get(index));
            detectedPcMap.put("pc_exists", pcExists.get(index));
            detectedPcMap.put("pc_exists_status", pcExistsStatuses.get(index));
            detectedPcMap.put("server_detection_status", serverDetectionStates.get(index));

            fDetectedPcMaps.add(detectedPcMap);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState)
    {
        // Save the maps of information about the detected PCs.
        ArrayList<String> hostNames = new ArrayList<String>();
        ArrayList<String> pcExists = new ArrayList<String>();
        ArrayList<String> pcExistsStatuses = new ArrayList<String>();
        ArrayList<String> serverDetectionStates = new ArrayList<String>();
        for (HashMap<String, String> detectedPcMap : fDetectedPcMaps)
        {
            hostNames.add(detectedPcMap.get("host_name"));
            pcExists.add(detectedPcMap.get("pc_exists"));
            pcExistsStatuses.add(detectedPcMap.get("pc_exists_status"));
            serverDetectionStates.add(detectedPcMap.get("server_detection_status"));
        }

        savedInstanceState.putStringArrayList("hostNames", hostNames);
        savedInstanceState.putStringArrayList("pcExists", pcExists);
        savedInstanceState.putStringArrayList("pcExistsStatuses", pcExistsStatuses);
        savedInstanceState.putStringArrayList("serverDetectionStatuses", serverDetectionStates);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * <p>
     * Removes the {@link android.view.View View} to be displayed when the detection of {@link com.se.pcremote.android.PC PC}s is in progress.
     * </p>
     */
    public void removeInProgressView()
    {
        getListView().removeFooterView(fInProgressView);
    }
}
