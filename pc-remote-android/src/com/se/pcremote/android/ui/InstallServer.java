package com.se.pcremote.android.ui;

import com.se.pcremote.android.R;

import android.app.Activity;
import android.os.Bundle;

public class InstallServer extends Activity
{
    public InstallServer()
    {}

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_server);
    }
}