package ro.andreidobrescu.emojilike.touchdetector;

import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import ro.andreidobrescu.emojilike.EmojiConfig;

/**
 * 被界面容器持有
 * 1. 构建EmojiConfig.setUp时，会将构建的Config传入这里---->构建EmojiTriggerManager加入缓存
 * 2. 接管界面容器的dispatchTouchEvent，分发给EmojiTriggerManagers
 */
public class EmojiLikeTouchDetector
{
    private List<EmojiTriggerManager> emojiTriggerManagers;


    /**
     * 1. 构建EmojiConfig.setUp时，会将构建的Config传入这里---->构建EmojiTriggerManager加入缓存
     */
    public void configure (EmojiConfig config)
    {
        EmojiTriggerManager emojiTriggerManager=new EmojiTriggerManager();
        emojiTriggerManager.configure(config);
        getEmojiTriggerManagers().add(emojiTriggerManager);
    }


    /**
     * 2. 接管界面容器的dispatchTouchEvent，分发给EmojiTriggerManager
     */
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        for (EmojiTriggerManager manager : getEmojiTriggerManagers())
        {
            if (manager.getEmojiView().isShowed()||event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_UP)
            {
                boolean shouldCallSuper=manager.onTouch(event);
                if (shouldCallSuper==false&&event.getAction()==MotionEvent.ACTION_MOVE)
                    return false;
            }
        }

        return true;
    }

    /**************** get|set *********************************/
    private List<EmojiTriggerManager> getEmojiTriggerManagers()
    {
        if (emojiTriggerManagers==null)
            emojiTriggerManagers=new LinkedList<>();
        return emojiTriggerManagers;
    }
}
