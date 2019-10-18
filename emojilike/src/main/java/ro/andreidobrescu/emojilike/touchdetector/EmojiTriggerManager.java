package ro.andreidobrescu.emojilike.touchdetector;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.EmojiLikeView;
/**
 * 接管界面容器的touch事件
 * 1. 转发  onTouchDown 、move、up给EmojiLikeView；
 * 2. 控制 EmojiLikeView的延时显示和隐藏；
 */
public class EmojiTriggerManager {

    /**
     * View
     */
    private View triggerView; //触发view
    private EmojiLikeView emojiView;//目标浮层View

    /**
     * 传入值
     */
    private int touchDownDelay;//延时触发，EmojiLikeView的show
    private int touchUpDelay;//延时触发，EmojiLikeView的hide

    /**
     * 中间变量
     */
    public boolean triggerViewTouched = false;//标示是否在一次触发延时弹窗事件中

    public boolean shouldSendEventsToEmojiView = false;//延时任务是否已经被触发 == View已经显示
    private boolean shouldWaitForClosing = false;//标示是否在延时关闭弹窗中，是的话就终止其他事件响应

    private MotionEvent downEvent;//缓存 引发触摸控件的触摸事件的  DownEvent，延时任务被执行时传递给View
    private MotionEvent upEvent;

    /***************************  初始化  ***********************/
    public void configure(EmojiConfig config) {
        this.triggerView = config.triggerView;
        this.emojiView = config.emojiView;
        this.touchDownDelay = config.touchDownDelay;
        this.touchUpDelay = config.touchUpDelay;
    }

    /**************************** 核心函数 *************************/
    public boolean onTouch(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        int action = event.getAction();

        //if the emoji view is closing (when the user releases the touch), wait for the closing to be done
        if (shouldWaitForClosing)
            return true;

        if (action == MotionEvent.ACTION_DOWN) {
            //校验触摸事件在 触发View的布局范围内
            if (intersectView(triggerView, (int) x, (int) y)) {
                triggerViewTouched = true;//标记一次触发延时弹窗事件开始
                shouldSendEventsToEmojiView = false;//标记，但是弹窗延时任务还未执行，也就是说View还未显示
                downEvent = event;//记录down事件

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (triggerViewTouched) {
                            // 如果用户还没松手，并且一直出于down状态，则开始弹窗
                            shouldSendEventsToEmojiView = true;
                            emojiView.onTouchDown(downEvent.getRawX(), downEvent.getRawY());
                            emojiView.show();
                        }
                    }
                }, touchDownDelay);

                return false;
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (triggerViewTouched) {//标示正在一次触发延时弹窗事件中
                if (shouldSendEventsToEmojiView) {//并且 View已经显示
                    emojiView.onTouchMove(x, y);
                    return false;
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            triggerViewTouched = false;//标记一次触发延时弹窗事件结束
            shouldSendEventsToEmojiView = false;//标记，弹窗延时任务还未执行，也就是说View还未显示
            shouldWaitForClosing = true;//标记，关闭延时任务已经开始执行，但是View还未隐藏
            upEvent = event;

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    shouldWaitForClosing = false;
                    emojiView.onTouchUp(upEvent.getRawX(), upEvent.getRawY());
                    emojiView.hide();
                }
            }, touchUpDelay);

            return false;
        }

        return true;
    }

    /**
     * 校验触摸事件，是否在 触发View的布局范围内
     */
    private boolean intersectView(View view, int rx, int ry) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();
        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
    }


    /**************************** get|set *************************/
    public EmojiLikeView getEmojiView() {
        return emojiView;
    }
}
