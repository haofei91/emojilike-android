package ro.andreidobrescu.emojilike;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.List;

import ro.andreidobrescu.emojilike.entity.EmojiEntity;
import ro.andreidobrescu.emojilike.view.EmojiCellView;
import ro.andreidobrescu.emojilike.view.animation.ViewWeightAnimation;

/**
 * Created by using on 7/4/2016.
 */
public class EmojiLikeView extends RelativeLayout {
    /**
     * 传入参数
     */
    private EmojiConfig config;

    /**
     * View
     */
    private ImageView emojiBackgroundView;//背景
    private LinearLayout emojiCellsContainer;//线性布局容器，承放表情Item
    private List<EmojiCellView> emojiCellViews;//缓存表情Item
    /**
     * 中间变量
     */
    private int selectedEmoji = 0;//当前选中位置

    /*****************  构造函数 *********************/

    public EmojiLikeView(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    public EmojiLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    public EmojiLikeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    /*****************  初始化 *********************/

    /**
     * 构建itemViews
     */
    public void configure(EmojiConfig config) {
        this.config = config;
        this.emojiCellViews = new LinkedList<>();
        this.selectedEmoji = -1;

        //1. 背景
        if (config.backgroundImageResource != 0) {
            //creating the background image view
            emojiBackgroundView = new ImageView(getContext());

            emojiBackgroundView.setImageResource(config.backgroundImageResource);
            emojiBackgroundView.setScaleType(ImageView.ScaleType.FIT_XY);

            RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, config.backgroundViewHeight);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(config.emojiViewMarginLeft, 0, config.emojiViewMarginRight, config.backgroundViewMarginBottom);
            emojiBackgroundView.setLayoutParams(params);

            emojiBackgroundView.setVisibility(View.INVISIBLE);
            this.addView(emojiBackgroundView);
        }

        //2. 表情容器
        this.emojiCellsContainer = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, config.emojiImagesContainerHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.emojiCellsContainer.setLayoutParams(params);
        this.emojiCellsContainer.setVisibility(View.INVISIBLE);
        this.addView(emojiCellsContainer);

        //3. 表情items
        for (int i = 0; i < config.emojis.size(); i++) {
            //creating emoji cell views
            EmojiEntity emoji = config.emojis.get(i);

            EmojiCellView cellView = config.cellViewFactory.newInstance(getContext());
            cellView.setLayoutParams(getDefaultLayoutParams(i));
            cellView.onWeightAnimated(0);
            cellView.setEmoji(emoji);

            this.emojiCellsContainer.addView(cellView);
            this.emojiCellViews.add(cellView);
        }

        this.bringToFront();
    }

    //表情item的默认布局
    private LinearLayout.LayoutParams getDefaultLayoutParams(int viewIndex) {
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        float weight = config.unselectedEmojiWeight;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, height, weight);

        int left = config.unselectedEmojiMarginLeft;
        int top = config.unselectedEmojiMarginTop;
        int bottom = config.unselectedEmojiMarginBottom;
        int right = config.unselectedEmojiMarginRight;
        if (viewIndex == 0)
            params.setMargins(config.emojiViewMarginLeft + left, top, right, bottom);
        else if (viewIndex == config.emojis.size() - 1)
            params.setMargins(left, top, right + config.emojiViewMarginRight, bottom);
        else
            params.setMargins(left, top, right, bottom);
        return params;
    }


    /*****************  动作 *********************/

    /**
     * used to show contents the view
     */
    public void show() {
        //if (this.emojiCellsContainer.getVisibility() == View.VISIBLE) return;

        if (config.emojiViewOpenedAnimation != null)
            this.startAnimation(config.emojiViewOpenedAnimation);

        if (this.emojiBackgroundView != null)
            this.emojiBackgroundView.setVisibility(View.VISIBLE);

        this.emojiCellsContainer.setVisibility(View.VISIBLE);

        if (this.config.emojiLikePopup != null) {
            this.config.emojiLikePopup.showAsDropDown(this.config.triggerView);
        }
    }

    /**
     * used to hide contents of the view
     */
    public void hide() {
        /*if (this.emojiCellsContainer.getVisibility() != View.VISIBLE) return;

        if (config.emojiViewClosedAnimation != null) {
            this.startAnimation(config.emojiViewClosedAnimation);

            config.emojiViewClosedAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (emojiBackgroundView != null)
                        emojiBackgroundView.setVisibility(View.INVISIBLE);
                    emojiCellsContainer.setVisibility(View.INVISIBLE);
                    if (config.emojiLikePopup != null) {
                        config.emojiLikePopup.dismiss();
                    }

                    for (int i = 0; i < emojiCellViews.size(); i++) {
                        EmojiCellView cellView = emojiCellViews.get(i);
                        cellView.setLayoutParams(getDefaultLayoutParams(i));
                        cellView.onWeightAnimated(0);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            if (this.emojiBackgroundView != null)
                this.emojiBackgroundView.setVisibility(View.INVISIBLE);
            this.emojiCellsContainer.setVisibility(View.INVISIBLE);
            if (this.config.emojiLikePopup != null) {
                this.config.emojiLikePopup.dismiss();
            }
        }*/


    }

    /****************************** 对界面触摸时间的响应 *************************/

    /**
     * 响应点下事件
     * 1. 调用onTouchMove。放大选中的表情
     */
    public void onTouchDown(final float x, final float y) {
        /*onTouchMove(x, y);

        //hackish bugfix
        new Handler().postDelayed(() ->
        {
            onTouchMove(x, y);
            new Handler().postDelayed(() -> onTouchMove(x, y), 50);
        }, 50);*/
    }

    /**
     * 响应抬起事件，触发选中回掉。  ps: 表情Views的布局复位在hide中进行
     */
    public void onTouchUp(float x, float y) {
        if (this.isShowed()) {
            if (config.onEmojiSelectedListener != null) {
                if (selectedEmoji >= 0 && selectedEmoji < this.config.emojis.size()) {
                    if (x != -1 && y != -1) {
                        if (selectedEmoji != -1) {
                            config.onEmojiSelectedListener.onEmojiSelected(this.config.emojis.get(selectedEmoji));
                        }
                    }
                }
            }
        }
    }

    /**
     * 响应Move事件。放大选中布局
     */
    public void onTouchMove(float x, float y) {
        int maxX = getWidth();
        int minX = (int) getX();
        int index = (int) (((x - minX) / (float) maxX) * config.emojis.size());

        if (x < minX || x > maxX + minX) {
            //out of the emoji zone
            for (int i = 0; i < config.emojis.size(); i++)
                setUnselectedEmoji(i);
            selectedEmoji = -1;
        } else {
            if (index < 0) index = 0;
            if (index >= config.emojis.size()) {
                //超过就是最后一个
                index = config.emojis.size() - 1;
            }
            setSelectedLikeOnIndex(index);
        }
    }


    /**
     * 放大指定位置的表情
     */
    private void setSelectedLikeOnIndex(int selectedIndex) {

        for (int i = 0; i < selectedIndex; i++)
            setUnselectedEmoji(i);
        for (int i = selectedIndex + 1; i < config.emojis.size(); i++)
            setUnselectedEmoji(i);

        setSelectedEmoji(selectedIndex);
    }

    private void setSelectedEmoji(int index) {
        if (index >= 0 && index < emojiCellViews.size()) {
            selectedEmoji = index;
            EmojiCellView view = emojiCellViews.get(selectedEmoji);
            float weight = getWeight(view);
            growView(view, index, weight, 4, config.emojiAnimationSpeed, true);
        }
    }


    private void growView(EmojiCellView view, int index, float initWeight, float maxWeight, float step, boolean shouldSelect) {
        Animation a = new ViewWeightAnimation(view, index, initWeight, maxWeight, step, shouldSelect, config);
        view.startAnimation(a);
    }

    /**
     * 缩小指定View
     */
    private void setUnselectedEmoji(int index) {
        if (index >= 0 && index < emojiCellViews.size()) {
            EmojiCellView view = emojiCellViews.get(index);
            float weight = getWeight(view);
            shrinkView(view, index, weight, 1f, -config.emojiAnimationSpeed, false);
        }
    }

    private void shrinkView(EmojiCellView view, int index, float initWeight, float maxWeight, float step, boolean forSelected) {
        growView(view, index, initWeight, maxWeight, step, forSelected);
    }


    public float getWeight(View view) {
        return ((LinearLayout.LayoutParams) view.getLayoutParams()).weight;
    }


    /********************* get|set ***********************/

    public boolean isShowed() {
        return this.emojiCellsContainer.getVisibility() == View.VISIBLE;
    }
}
