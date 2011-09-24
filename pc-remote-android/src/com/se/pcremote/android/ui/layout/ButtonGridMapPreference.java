package com.se.pcremote.android.ui.layout;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.se.pcremote.android.Key;
import com.se.pcremote.android.PCRemoteProvider;
import com.se.pcremote.android.R;

/**
 * <p>
 * A preference that edits the mapping of Keys for the button grid.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ButtonGridMapPreference extends Preference
{
    /**
     * <p>
     * Edits a button in the button grid.
     * </p>
     * 
     * @author Gary Buyn
     */
    private class ButtonGridMapPreferenceListener implements OnClickListener
    {
        /**
         * <p>
         * The column the button being edited is in.
         * </p>
         */
        private int fButtonGridColumnIndex;

        /**
         * <p>
         * The row the button being edited is in.
         * </p>
         */
        private int fButtonGridRowIndex;

        /**
         * <p>
         * Creates an instance of <code>ButtonGridMapPreferenceListener</code>.
         * </p>
         * 
         * @param buttonGridRowIndex The row the button being edited is in.
         * @param buttonGridColumnIndex The column the button being edited is in.
         */
        public ButtonGridMapPreferenceListener(final int buttonGridRowIndex, final int buttonGridColumnIndex)
        {
            fButtonGridColumnIndex = buttonGridColumnIndex;
            fButtonGridRowIndex = buttonGridRowIndex;
        }

        @Override
        public void onClick(final View view)
        {
            if (fLayoutPreferences != null)
            {
                // Inform the activity as to which button is being edited.
                fLayoutPreferences.setButtonGridColumnIndex(fButtonGridColumnIndex);
                fLayoutPreferences.setButtonGridRowIndex(fButtonGridRowIndex);

                // Query the Keys.
                fLayoutPreferences.startActivityForResult(new Intent(Intent.ACTION_VIEW, PCRemoteProvider.KEY_URI),
                        LayoutPreferences.QUERY_KEYS_REQUEST);
            }
        }
    }

    /**
     * <p>
     * The padding around the title.
     * </p>
     */
    private static final int TITLE_PADDING = 15;

    /**
     * <p>
     * The button grid.
     * </p>
     */
    private TableLayout fButtonGrid;

    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.layout.LayoutPreferences LayoutPreferences} on which this <code>ButtonGridMapPreference</code> is being
     * displayed.
     * </p>
     */
    private LayoutPreferences fLayoutPreferences;

    /**
     * <p>
     * Creates an instance of <code>ButtonGridMapPreference</code>.
     * </p>
     * 
     * @param context The context in which this <code>ButtonGridMapPreference</code> resides.
     */
    public ButtonGridMapPreference(final Context context)
    {
        super(context);

        fLayoutPreferences = null;
        fButtonGrid = null;
    }

    /**
     * <p>
     * Creates an instance of <code>ButtonGridMapPreference</code>.
     * </p>
     * 
     * @param context The context in which this <code>ButtonGridMapPreference</code> resides.
     * @param attrs The attributes of the XML tag that is inflating the <code>ButtonGridMapPreference</code>.
     */
    public ButtonGridMapPreference(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);

        fLayoutPreferences = null;
        fButtonGrid = null;
    }

    /**
     * <p>
     * Creates an instance of <code>ButtonGridMapPreference</code>.
     * </p>
     * 
     * @param context The context in which this <code>ButtonGridMapPreference</code> resides.
     * @param attrs The attributes of the XML tag that is inflating the <code>ButtonGridMapPreference</code>.
     * @param defStyle The default style to apply to this <code>ButtonGridMapPreference</code>.
     */
    public ButtonGridMapPreference(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        fLayoutPreferences = null;
        fButtonGrid = null;
    }

    /**
     * <p>
     * Builds the button grid portion of this <code>ButtonGridMapPreference</code>.
     * </p>
     * 
     * @return The button grid portion of this <code>ButtonGridMapPreference</code>.
     */
    public View buildButtonGrid()
    {
        int buttonGridHeight = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("layoutButtonGridHeight", "0"));
        int buttonGridWidth = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("layoutButtonGridWidth", "0"));

        if (fButtonGrid == null)
        {
            fButtonGrid = new TableLayout(getContext());
            fButtonGrid.setShrinkAllColumns(true);
            fButtonGrid.setStretchAllColumns(true);
        }
        else
        {
            fButtonGrid.removeAllViews();
        }

        for (int rowIndex = 0; rowIndex < buttonGridHeight; rowIndex++)
        {
            TableRow buttonGridRow = new TableRow(getContext());

            for (int columnIndex = 0; columnIndex < buttonGridWidth; columnIndex++)
            {
                Key key = fLayoutPreferences.getLayout().getButtonGridKey(getContext(), rowIndex, columnIndex);
                if (key != null)
                {
                    if (key.getImageResourceId() == -1)
                    {
                        Button buttonGridButton = new Button(getContext());
                        buttonGridButton.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f));
                        buttonGridButton.setText(key.getName());
                        buttonGridButton.setTextAppearance(getContext(), R.style.ButtonGridTextAppearance);
                        buttonGridButton.setOnClickListener(new ButtonGridMapPreferenceListener(rowIndex, columnIndex));
                        buttonGridRow.addView(buttonGridButton);
                    }
                    else
                    {
                        ImageButton buttonGridButton = new ImageButton(getContext());
                        buttonGridButton.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f));
                        buttonGridButton.setImageResource(key.getImageResourceId());
                        buttonGridButton.setOnClickListener(new ButtonGridMapPreferenceListener(rowIndex, columnIndex));
                        buttonGridRow.addView(buttonGridButton);
                    }
                }
                else
                {
                    Button buttonGridButton = new Button(getContext());
                    buttonGridButton.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f));
                    buttonGridButton.setOnClickListener(new ButtonGridMapPreferenceListener(rowIndex, columnIndex));
                    buttonGridRow.addView(buttonGridButton);
                }
            }

            fButtonGrid.addView(buttonGridRow);
        }

        return (fButtonGrid);
    }

    @Override
    protected View onCreateView(final ViewGroup parent)
    {
        LinearLayout preference = new LinearLayout(getContext());
        preference.setOrientation(LinearLayout.VERTICAL);

        int adjustedTitlePadding = (int) (TITLE_PADDING * getContext().getResources().getDisplayMetrics().density + 0.5f);

        TextView title = new TextView(getContext());
        title.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
        title.setPadding(adjustedTitlePadding, adjustedTitlePadding, adjustedTitlePadding, adjustedTitlePadding);
        title.setText(getTitle());
        preference.addView(title);

        preference.addView(buildButtonGrid());

        return (preference);
    }

    /**
     * <p>
     * Set the {@link com.se.pcremote.android.ui.layout.LayoutPreferences LayoutPreferences} on which this <code>ButtonGridMapPreference</code> is
     * being displayed.
     * </p>
     * 
     * @param layoutPreferences The <code>LayoutPreferences</code> on which this <code>ButtonGridMapPreference</code> is being displayed.
     */
    public void setLayoutPreferences(final LayoutPreferences layoutPreferences)
    {
        fLayoutPreferences = layoutPreferences;
    }
}
