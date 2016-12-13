package suncere.androidappcf.controls;

import suncere.androidappcf.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class TipView {
	
    private Dialog mDialog;
 
    public TipView(Context context,int layout) {
        mDialog = new Dialog(context, R.style.dialog);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setAttributes(wl);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(layout);
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
    }
 
    public void show() {
        mDialog.show();
    }
    
    public void setCancelable(boolean flag){
    	mDialog.setCancelable(flag);
    }
    
    public void dismiss()
    {
    	mDialog.dismiss();
    }
    
    public View findViewById(int id)
    {
    	return mDialog.findViewById(id);
    }
}