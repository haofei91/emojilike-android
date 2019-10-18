package ro.andreidobrescu.emojilike.view;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by mathilde on 2019/1/15.
 */

public class BasePopup extends PopupWindow {
    protected Activity activity;

    public BasePopup(@NonNull WeakReference<Activity> weakReference){
        this.activity = weakReference.get();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if(activity ==null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        try{
            super.showAtLocation(parent, gravity, x, y);
        } catch (Exception ex) {}

    }
}
