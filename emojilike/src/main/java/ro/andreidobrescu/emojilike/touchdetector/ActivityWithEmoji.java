package ro.andreidobrescu.emojilike.touchdetector;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;

import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.touchdetector.EmojiLikeTouchDetector;
import ro.andreidobrescu.emojilike.touchdetector.IActivityWithEmoji;

/**
 * Created by using on 7/4/2016.
 * used to detect touches
 */
public class ActivityWithEmoji extends AppCompatActivity implements IActivityWithEmoji
{
    private EmojiLikeTouchDetector emojiLikeTouchDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        emojiLikeTouchDetector=new EmojiLikeTouchDetector();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        boolean shouldCallSuper=emojiLikeTouchDetector.dispatchTouchEvent(event);
        if (shouldCallSuper)
            return super.dispatchTouchEvent(event);
        return false;
    }

    @Override
    public void configureEmojiLike(EmojiConfig config)
    {
        emojiLikeTouchDetector.configure(config);
    }
}
