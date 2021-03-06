/* The MIT License (MIT)
* Copyright (c) 2016 BELATRIX
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package com.belatrix.events.presentation.ui.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.belatrix.events.BxEventsApplication;
import com.belatrix.events.R;
import com.belatrix.events.di.component.DaggerUIComponent;
import com.belatrix.events.di.component.UIComponent;
import com.belatrix.events.utils.DialogUtils;
import com.belatrix.events.utils.SnackbarUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author PedroCarrillo
 */
public abstract class BelatrixBaseActivity extends AppCompatActivity implements FragmentListener {

    private AlertDialog errorAlertDialog;
    private ProgressDialog progressDialog;
    @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;
    UIComponent uiComponent;
    @BindString(R.string.menu_title_share)
    protected String stringShare;
    @BindString(R.string.dialog_title_error)
    protected String stringError;
    @BindString(R.string.app_name)
    protected String stringAppName;
    @BindString(R.string.no_internet_connection_message)
    protected String stringNoInternet;
    private Snackbar noInternetSnackBar;
    abstract protected void initDependencies(UIComponent uiComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        uiComponent = DaggerUIComponent.builder().applicationComponent(BxEventsApplication.get(this).getComponent()).build();
        initDependencies(uiComponent);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter actionFilters = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnReceiver, actionFilters);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mConnReceiver);
    }

    public UIComponent getUiComponent() {
        return uiComponent;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        replaceFragment(R.id.main_content, fragment, addToBackStack);
    }

    @Override
    public void replaceFragment(int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = fragment.getClass().getSimpleName();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void removeFragment(int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(getSupportFragmentManager().findFragmentById(containerId));
        transaction.commit();
    }

    @Override
    public void showError(String message) {
        if (errorAlertDialog == null) {
            errorAlertDialog = DialogUtils.createErrorDialog(this, null);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (!errorAlertDialog.isShowing()) {
            errorAlertDialog.setMessage(message);
            errorAlertDialog.show();
        }
    }

    @Override
    public void setTitle(String title) {
        if (toolbar != null){
            toolbar.setTitle(title);
        }
        else if (!activityHandleTitle() && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.dialog_message_loading));
    }

    @Override
    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = DialogUtils.createProgressDialog(this, null);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showSnackBar(String message) {
        if (toolbar != null) {
            SnackbarUtils.createInformationSnackBar(toolbar, message, null, Snackbar.LENGTH_LONG, null).show();
        }
    }

    @Override
    public void showSnackBar(View view, String message) {
        if (toolbar != null) {
            SnackbarUtils.createInformationSnackBar(view, message, null, Snackbar.LENGTH_LONG, null).show();
        }
    }

    @Override
    public void showSnackBar(View view, String message, String action, View.OnClickListener onClickListener) {
        if (toolbar != null) {
            SnackbarUtils.createInformationSnackBar(toolbar, message, action, Snackbar.LENGTH_LONG, onClickListener).show();
        }
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void setActivityResult(int resultCode) {
        setResult(resultCode);
    }

    @Override
    public void setActivityResult(int resultCode, Intent resultIntent) {
        setResult(resultCode, resultIntent);
    }

    protected boolean activityHandleTitle() {
        return false;
    }

    protected void setToolbar() {
        setSupportActionBar(toolbar);
    }

    protected void setNavigationToolbar() {
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateBack();
                }
            });
        }
    }

    @Override
    public void setToolbar(Toolbar customToolbar) {
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (customToolbar != null) {
            customToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    protected void navigateBack() {
        onBackPressed();
    }


    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    private final BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (context != null) {
               boolean isConnected = isNetworkAvailable(context);
                if (!isConnected) {
                    noInternetSnackBar =  SnackbarUtils.createInformationSnackBar(toolbar, stringNoInternet, null, Snackbar.LENGTH_INDEFINITE, null);
                    noInternetSnackBar.show();
                }else{
                    if(noInternetSnackBar!=null && noInternetSnackBar.isShown()){
                        noInternetSnackBar.dismiss();
                    }
                }
            }
        }
    };

    private static ConnectivityManager getConnectivityManager(@NonNull Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        boolean result = false;
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
            result = connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        return result;
    }
}
