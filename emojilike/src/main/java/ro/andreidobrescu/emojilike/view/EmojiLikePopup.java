package ro.andreidobrescu.emojilike.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.EmojiLikeView;
import ro.andreidobrescu.emojilike.touchdetector.EmojiTriggerManager;
import ro.andreidobrescu.emojilikelib.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by mathilde on 2018/4/26.
 */

public class EmojiLikePopup extends BasePopup {

    public View rootView;
    public EmojiLikeView emojiLikeView;

    public EmojiLikePopup(@NonNull WeakReference<Activity> reference){
        super(reference);
        this.activity = reference.get();
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(activity).inflate(R.layout.emoji_like_popup_layout, null, true);
        emojiLikeView = rootView.findViewById(R.id.emojiView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        setContentView(rootView);
        setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setWidth(WRAP_CONTENT);
        setHeight(WRAP_CONTENT);
    }



    public void showAtLocation(View view, int x, int y) {
        super.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, x, y);

    }

    public void configure(EmojiConfig emojiConfig) {
    }
}
