package com.se.pcremote.android.ui.controlpad;

import android.content.Context;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.se.pcremote.android.Key;
import com.se.pcremote.android.Layout;
import com.se.pcremote.android.R;

/**
 * <p>
 * The content {@link android.view.View View} for the {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} activity. This view is
 * created programmatically because it is based on the properties of the <code>ControlPad</code>'s currently active
 * {@link com.se.pcremote.android.Layout Layout}.
 * </p>
 * 
 * @author Gary Buyn
 */
public class ControlPadView extends LinearLayout
{
    /**
     * <p>
     * The font size of the summary message displayed when the active {@link com.se.pcremote.android.Layout Layout} does not exist (the 'lost'
     * screen).
     * </p>
     */
    private static final int LOST_SUMMARY_FONT_SIZE = 18;

    /**
     * <p>
     * The font size of the title message displayed when the active {@link com.se.pcremote.android.Layout Layout} does not exist (the 'lost' screen).
     * </p>
     */
    private static final int LOST_TITLE_FONT_SIZE = 36;

    /**
     * <p>
     * The ID of the left mouse button.
     * </p>
     */
    public static final int MOUSE_BUTTON_LEFT = -1;

    /**
     * <p>
     * The ID of the right mouse button.
     * </p>
     */
    public static final int MOUSE_BUTTON_RIGHT = -3;

    /**
     * <p>
     * The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} that will display this <code>ControlPadView</code> as its content.
     * </p>
     */
    private ControlPad fControlPad;

    /**
     * <p>
     * Creates an instance of <code>ControlPadView</code>.
     * </p>
     * 
     * @param controlPad The {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad} that will display this <code>ControlPadView</code> as
     * its content.
     */
    public ControlPadView(final ControlPad controlPad)
    {
        super(controlPad);

        fControlPad = controlPad;
    }

    /**
     * <p>
     * Builds this <code>ControlPadView</code> based on the {@link com.se.pcremote.android.ui.controlpad.ControlPad ControlPad}'s currently active
     * {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     */
    public void build()
    {
        removeAllViews();

        Layout layout = fControlPad.getLayout();
        if (layout.getId() == 0)
        {
            setOrientation(LinearLayout.VERTICAL);

            TextView lostTitle = new TextView(getContext());
            lostTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            lostTitle.setText(R.string.lost_title);
            lostTitle.setTextSize(LOST_TITLE_FONT_SIZE);
            lostTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(lostTitle);
            ImageView lostImage = new ImageView(getContext());
            lostImage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
            lostImage.setScaleType(ScaleType.CENTER);
            lostImage.setImageResource(R.drawable.lost);
            addView(lostImage);
            TextView lostSummary = new TextView(getContext());
            lostSummary.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            lostSummary.setText(R.string.lost_summary);
            lostSummary.setTextSize(LOST_SUMMARY_FONT_SIZE);
            lostSummary.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(lostSummary);
        }
        else
        {
            ViewGroup parentView = this;
            addView(buildKeyEventView());

            // Prepare the parent view depending on the orientation of the device.
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            {
                setOrientation(LinearLayout.VERTICAL);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                    && (layout.hasButtonGrid() || layout.hasKeyboardButton()))
            {
                LinearLayout left = new LinearLayout(getContext());
                left.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
                left.setOrientation(LinearLayout.VERTICAL);
                addView(left);
                parentView = left;
            }

            if (layout.hasButtonGrid())
            {
                View buttonGrid = buildButtonGrid(layout);
                parentView.addView(buttonGrid);
            }

            if (layout.hasKeyboardButton())
            {
                View keyboard = buildKeyboardButton();
                parentView.addView(keyboard);
            }

            // Prepare the parent view depending on the orientation of the device.
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                    && (layout.hasMousePad() || layout.hasMouseButtons()))
            {
                LinearLayout right = new LinearLayout(getContext());
                right.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
                right.setOrientation(LinearLayout.VERTICAL);
                addView(right);
                parentView = right;
            }

            if (layout.hasMousePad())
            {
                View mousePad = buildMousePad(layout);
                parentView.addView(mousePad);
            }

            if (layout.hasMouseButtons())
            {
                View mouseButtons = buildMouseButtons();
                parentView.addView(mouseButtons);
            }
        }
    }

    /**
     * <p>
     * Builds the button grid portion of this <code>ControlPadView</code> based on the given {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     * 
     * @param layout The <code>Layout</code> to base the button grid on.
     * 
     * @return The button grid portion of this <code>ControlPadView</code>.
     */
    private View buildButtonGrid(final Layout layout)
    {
        TableLayout buttonGrid = new TableLayout(getContext());
        buttonGrid.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        buttonGrid.setShrinkAllColumns(true);
        buttonGrid.setStretchAllColumns(true);

        for (int rowIndex = 0; rowIndex < layout.getButtonGridHeight(); rowIndex++)
        {
            TableRow buttonGridRow = new TableRow(getContext());
            buttonGridRow.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, 0, 1.0f));

            for (int columnIndex = 0; columnIndex < layout.getButtonGridWidth(); columnIndex++)
            {
                Key key = layout.getButtonGridKey(getContext(), rowIndex, columnIndex);
                if (key != null)
                {
                    if (key.getImageResourceId() == -1)
                    {
                        Button buttonGridButton = new Button(getContext());
                        buttonGridButton.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.FILL_PARENT, 1.0f));
                        buttonGridButton.setId(key.getId());
                        buttonGridButton.setText(key.getName());
                        buttonGridButton.setTextAppearance(fControlPad, R.style.ButtonGridTextAppearance);
                        buttonGridButton.setOnTouchListener(new ControlPadListener(fControlPad));
                        buttonGridRow.addView(buttonGridButton);
                    }
                    else
                    {
                        ImageButton buttonGridButton = new ImageButton(getContext());
                        buttonGridButton.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.FILL_PARENT, 1.0f));
                        buttonGridButton.setId(key.getId());
                        buttonGridButton.setImageResource(key.getImageResourceId());
                        buttonGridButton.setOnTouchListener(new ControlPadListener(fControlPad));
                        buttonGridRow.addView(buttonGridButton);
                    }
                }
                else
                {
                    Button buttonGridButton = new Button(getContext());
                    buttonGridButton.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.FILL_PARENT, 1.0f));
                    buttonGridRow.addView(buttonGridButton);
                }
            }

            buttonGrid.addView(buttonGridRow);
        }

        return (buttonGrid);
    }

    /**
     * <p>
     * Builds the keyboard button portion of this <code>ControlPadView</code>.
     * </p>
     * 
     * @return The keyboard button portion of this <code>ControlPadView</code>.
     */
    private View buildKeyboardButton()
    {
        ImageButton keyboardButton = new ImageButton(getContext());
        keyboardButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        keyboardButton.setImageResource(R.drawable.keyboard_button);
        keyboardButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                InputMethodManager inputMgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        return (keyboardButton);
    }

    /**
     * <p>
     * Builds a hidden control capable of receiving key events from the soft and hard keyboards.
     * </p>
     * 
     * @return A hidden control capable of receiving key events from the soft and hard keyboards.
     */
    private View buildKeyEventView()
    {
        Button keyEventView = new Button(getContext())
        {
            @Override
            public InputConnection onCreateInputConnection(final EditorInfo outAttrs)
            {
                outAttrs.imeOptions |= EditorInfo.IME_ACTION_NONE;
                return (super.onCreateInputConnection(outAttrs));
            }
        };
        keyEventView.setLayoutParams(new LayoutParams(1, 1)); // Make it so small that it isn't visible but still holds focus.
        keyEventView.setFocusable(true);
        keyEventView.setFocusableInTouchMode(true);
        keyEventView.setOnKeyListener(new ControlPadListener(fControlPad));

        return (keyEventView);
    }

    /**
     * <p>
     * Builds the mouse buttons portion of this <code>ControlPadView</code>.
     * </p>
     * 
     * @return The mouse buttons portion of this <code>ControlPadView</code>.
     */
    private View buildMouseButtons()
    {
        LinearLayout mouseButtons = new LinearLayout(getContext());
        mouseButtons.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        ImageButton leftMouseButton = new ImageButton(getContext());
        leftMouseButton.setId(MOUSE_BUTTON_LEFT);
        leftMouseButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        leftMouseButton.setImageResource(R.drawable.mouse_button_left);
        leftMouseButton.setOnClickListener(new ControlPadListener(fControlPad));
        mouseButtons.addView(leftMouseButton);

        ImageButton rightMouseButton = new ImageButton(getContext());
        rightMouseButton.setId(MOUSE_BUTTON_RIGHT);
        rightMouseButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        rightMouseButton.setImageResource(R.drawable.mouse_button_right);
        rightMouseButton.setOnClickListener(new ControlPadListener(fControlPad));
        mouseButtons.addView(rightMouseButton);

        return (mouseButtons);
    }

    /**
     * <p>
     * Builds the mouse pad portion of this <code>ControlPadView</code> based on the given {@link com.se.pcremote.android.Layout Layout}.
     * </p>
     * 
     * @param layout The <code>Layout</code> to base the button grid on.
     * 
     * @return The mouse pad portion of this <code>ControlPadView</code>.
     */
    private View buildMousePad(final Layout layout)
    {
        LinearLayout mousePad = new LinearLayout(getContext());
        mousePad.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        mousePad.setBackgroundResource(R.drawable.mouse_pad_border);

        final GestureDetector gestureDetector = new GestureDetector(getContext(), new ControlPadListener(fControlPad));
        mousePad.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event)
            {
                gestureDetector.onTouchEvent(event);
                return (true);
            }
        });

        return (mousePad);
    }
}
