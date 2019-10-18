package ro.andreidobrescu.emojilike;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import ro.andreidobrescu.emojilike.common.EmojiException;
import ro.andreidobrescu.emojilike.entity.EmojiEntity;
import ro.andreidobrescu.emojilike.listener.OnEmojiSelectedListener;
import ro.andreidobrescu.emojilike.touchdetector.ActivityWithEmoji;
import ro.andreidobrescu.emojilike.touchdetector.IActivityWithEmoji;
import ro.andreidobrescu.emojilike.view.EmojiCellView;
import ro.andreidobrescu.emojilike.view.EmojiLikePopup;
import ro.andreidobrescu.emojilike.view.factory.IEmojiCellViewFactory;
import ro.andreidobrescu.emojilikelib.R;

/**
 * Created by using on 7/4/2016.
 * Use this class to configure the emoji like view
 */
public class EmojiConfig
{
    /**
     * 界面容器
     */
    IActivityWithEmoji target;

    /**
     * 控件
     */
    public View triggerView;
    public EmojiLikeView emojiView;
    public EmojiLikePopup emojiLikePopup;

    /**
     * 浮层的延时控制 和动画
     */
    public int touchDownDelay;
    public int touchUpDelay;

    public Animation emojiViewOpenedAnimation;
    public Animation emojiViewClosedAnimation;

    /**
     * 背景Image
     */
    public int backgroundImageResource;
    public int backgroundViewHeight;
    public int backgroundViewMarginBottom;

    /**
     * 表情items
     */
    public List <EmojiEntity> emojis;
    public IEmojiCellViewFactory cellViewFactory;

    public int emojiImagesContainerHeight;//线性容器的高度
    public int emojiViewMarginLeft;//线性容器的左右padding
    public int emojiViewMarginRight;

    public  int selectedEmojiWeight;//线性容器中，选中表情的宽度权重
    public int selectedEmojiMarginBottom;//线性容器中，选中表情的间距
    public  int selectedEmojiMarginTop;
    public  int selectedEmojiMarginLeft;
    public  int selectedEmojiMarginRight;

    public int unselectedEmojiWeight;//线性容器中，未选中表情的宽度权重
    public int unselectedEmojiMarginBottom;//线性容器中，未选中表情的间距
    public int unselectedEmojiMarginTop;
    public  int unselectedEmojiMarginLeft;
    public int unselectedEmojiMarginRight;

    public int selectedEmojiHeight;//选中表情的高度

    /**
     *
     */
    public  float emojiAnimationSpeed;

    /**
     * 选中监听
     */
    OnEmojiSelectedListener onEmojiSelectedListener;



    /***************** 构造函数默认值 ********************/

    private EmojiConfig(IActivityWithEmoji target)
    {
        //界面容器
        this.target = target;

        //浮层的延时控制 和动画
        touchDownDelay=100;
        touchUpDelay=500;
        emojiViewOpenedAnimation =AnimationUtils.loadAnimation(getContext(), R.anim.emoji_default_in_animation);
        emojiViewClosedAnimation =AnimationUtils.loadAnimation(getContext(), R.anim.emoji_default_out_animation);

        //背景Image
        backgroundImageResource =0;
        backgroundImageResource=R.drawable.emoji_default_background_drawable;
        backgroundViewHeight=dpToPx(50);
        backgroundViewMarginBottom=dpToPx(10);

        //线性容器
        emojiImagesContainerHeight=dpToPx(130);//线性容器的高度
        emojiViewMarginLeft=dpToPx(15);//线性容器的左右padding
        emojiViewMarginRight=dpToPx(15);

        //表情items
        selectedEmojiWeight=4;
        selectedEmojiMarginBottom=dpToPx(15);
        selectedEmojiMarginTop=0;
        selectedEmojiMarginLeft=dpToPx(15);
        selectedEmojiMarginRight=dpToPx(15);

        unselectedEmojiWeight=1;
        unselectedEmojiMarginBottom=0;
        unselectedEmojiMarginTop=dpToPx(30);
        unselectedEmojiMarginLeft=dpToPx(15);
        unselectedEmojiMarginRight=dpToPx(15);

        selectedEmojiHeight=dpToPx (85);//选中表情的高度。动画中使用

        emojiAnimationSpeed=0.2f;

        //默认的ItemView生成器
        cellViewFactory= EmojiCellView.WithImage::new;
    }



    /***************** 初始化 ********************/

    public void setup ()
    {
        if (emojiView==null){
            emojiLikePopup = new EmojiLikePopup(new WeakReference<Activity>(getActivity ()));
            emojiView = emojiLikePopup.emojiLikeView;
        }

        if (target==null)
            throw new EmojiException("Target not set. Set it with EmojiConfig.with(target)");
        else if (triggerView==null)
            throw new EmojiException("Trigger view not set. Do it with EmojiConfig.with(target).on(triggerView)");
        else if (emojis==null)
            throw new EmojiException("Emojis not set");
        else if (emojis.size()<=1)
            throw new EmojiException("Please add more emojis");
        else if (emojiView==null)
            throw new EmojiException("EmojiLikeView not set. Use open method.");
        else {
            //[事件]构建EmojiConfig.setUp时，会将构建好的Config传入detector---->内部构建EmojiTriggerManager加入缓存,所有的界面容器事件会转给emojiTriggerManagers
            target.configureEmojiLike(this);
            //[视图]构建itemViews
            emojiView.configure(this);
        }
    }




    /***************** get | set  **************************/


    /**
     * 界面容器
     */
    public static EmojiConfig with (ActivityWithEmoji target)
    {
        return new EmojiConfig(target);
    }
    public static EmojiConfig with (Context target)
    {
        if (target instanceof IActivityWithEmoji)
            return new EmojiConfig((IActivityWithEmoji)target);
        throw new EmojiException("please implement IActivityWithEmoji on your activity");
    }
    private Context getContext ()
    {
        if (target instanceof IActivityWithEmoji)
            return (Context)target;
        return null;
    }

    private Activity getActivity ()
    {
        if (target instanceof Activity)
            return (Activity)target;
        return null;
    }

    /**
     * 控件
     */
    public EmojiConfig on (View triggerView)
    {
        this.triggerView=triggerView;
        return this;
    }

    public EmojiConfig open (EmojiLikeView emojiView)
    {
        this.emojiView=emojiView;
        return this;
    }


    /**
     * 浮层的延时控制 和动画
     */
    //set the time delay from the touch down event and the moment the emoji view is showed
    public EmojiConfig setTouchDownDelay(int touchDownDelay) {
        this.touchDownDelay = touchDownDelay;
        return this;
    }
    // set the time delay between the touch up event and the moment the emoji view is hided
    public EmojiConfig setTouchUpDelay(int touchUpDelay) {
        this.touchUpDelay = touchUpDelay;
        return this;
    }
    public EmojiConfig setEmojiViewOpenedAnimation(Animation emojiViewOpenedAnimation) {
        this.emojiViewOpenedAnimation = emojiViewOpenedAnimation;
        return this;
    }
    public EmojiConfig setEmojiViewClosedAnimation(Animation emojiViewClosedAnimation) {
        this.emojiViewClosedAnimation = emojiViewClosedAnimation;
        return this;
    }

    /**
     * 背景Image
     */
    public EmojiConfig setBackgroundViewHeight(int backgroundViewHeight) {
        this.backgroundViewHeight = backgroundViewHeight;
        return this;
    }
    public EmojiConfig setBackgroundImageResource(int backgroundImageResource) {
        this.backgroundImageResource = backgroundImageResource;
        return this;
    }
    public EmojiConfig setBackgroundViewMarginBottom(int backgroundViewMarginBottom) {
        this.backgroundViewMarginBottom = backgroundViewMarginBottom;
        return this;
    }

    /**
     * 表情items
     */
    public EmojiConfig setEmojis (List<EmojiEntity> emojis)
    {
        this.emojis=emojis;
        return this;
    }

    public EmojiConfig addEmoji(EmojiEntity emoji)
    {
        if (this.emojis==null)
            this.emojis=new LinkedList<>();
        this.emojis.add(emoji);
        return this;
    }

    public EmojiConfig setEmojiImagesContainerHeight(int emojiImagesContainerHeight) {
        this.emojiImagesContainerHeight = emojiImagesContainerHeight;
        return this;
    }
    public EmojiConfig setEmojiViewMarginLeft(int emojiViewMarginLeft) {
        this.emojiViewMarginLeft = emojiViewMarginLeft;
        return this;
    }
    public EmojiConfig setEmojiViewMarginRight(int emojiViewMarginRight) {
        this.emojiViewMarginRight = emojiViewMarginRight;
        return this;
    }

    public EmojiConfig setSelectedEmojiWeight(int selectedEmojiWeight) {
        this.selectedEmojiWeight = selectedEmojiWeight;
        return this;
    }
    public EmojiConfig setSelectedEmojiMarginBottom(int selectedEmojiMarginBottom) {
        this.selectedEmojiMarginBottom = selectedEmojiMarginBottom;
        return this;
    }
    public EmojiConfig setSelectedEmojiMarginTop(int selectedEmojiMarginTop) {
        this.selectedEmojiMarginTop = selectedEmojiMarginTop;
        return this;
    }
    public EmojiConfig setSelectedEmojiMarginLeft(int selectedEmojiMarginLeft) {
        this.selectedEmojiMarginLeft = selectedEmojiMarginLeft;
        return this;
    }
    public EmojiConfig setSelectedEmojiMarginRight(int selectedEmojiMarginRight) {
        this.selectedEmojiMarginRight = selectedEmojiMarginRight;
        return this;
    }

    public EmojiConfig setUnselectedEmojiWeight(int unselectedEmojiWeight) {
        this.unselectedEmojiWeight = unselectedEmojiWeight;
        return this;
    }
    public EmojiConfig setUnselectedEmojiMarginBottom(int unselectedEmojiMarginBottom) {
        this.unselectedEmojiMarginBottom = unselectedEmojiMarginBottom;
        return this;
    }
    public EmojiConfig setUnselectedEmojiMarginTop(int unselectedEmojiMarginTop) {
        this.unselectedEmojiMarginTop = unselectedEmojiMarginTop;
        return this;
    }
    public EmojiConfig setUnselectedEmojiMarginLeft(int unselectedEmojiMarginLeft) {
        this.unselectedEmojiMarginLeft = unselectedEmojiMarginLeft;
        return this;
    }
    public EmojiConfig setUnselectedEmojiMarginRight(int unselectedEmojiMarginRight) {
        this.unselectedEmojiMarginRight = unselectedEmojiMarginRight;
        return this;
    }

    public EmojiConfig setSelectedEmojiHeight(int selectedEmojiHeight) {
        this.selectedEmojiHeight = selectedEmojiHeight;
        return this;
    }


    /**
     *
     */
    public EmojiConfig setEmojiAnimationSpeed(float emojiAnimationSpeed) {
        this.emojiAnimationSpeed = emojiAnimationSpeed;
        return this;
    }


    /**
     *
     */
    public EmojiConfig setEmojiCellViewFactory(IEmojiCellViewFactory factory)
    {
        this.cellViewFactory=factory;
        return this;
    }

    /**
     * 选中监听
     */
    public EmojiConfig setOnEmojiSelectedListener (OnEmojiSelectedListener listener)
    {
        this.onEmojiSelectedListener=listener;
        return this;
    }

    /***************** utils **************************/

    private int dpToPx(int i)
    {
        Resources r = getContext ().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
        return (int)px;
    }
}
