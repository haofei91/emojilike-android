package ro.andreidobrescu.emojilike.view.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.view.EmojiCellView;

/**
 * Created by using on 7/4/2016.
 * shrink/grow emoji animation using weight
 */
public class ViewWeightAnimation extends Animation {
    private EmojiCellView view;
    private int index;

    private float initialWeight;
    private float currentWeight;
    private float targetWeight;

    private float step;
    private boolean shouldSelect;

    private EmojiConfig config;


    public ViewWeightAnimation(EmojiCellView view, int index, float initialWeight, float targetWeight, float step, boolean shouldSelect, EmojiConfig config) {
        this.view = view;
        this.index = index;

        this.initialWeight = initialWeight;
        currentWeight = initialWeight;
        this.targetWeight = targetWeight;

        this.step = step;
        this.shouldSelect = shouldSelect;

        this.config = config;
    }

    public void animate() {
        applyTransformation(0f, null);
    }

    /**
     * 子类实现此方法根据插值时间来应用其转化效果
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        currentWeight += step;//根据正向或者负向步长，计算当前的动画的执行成都

        if (step > 0 ? (currentWeight < targetWeight) : (currentWeight > targetWeight)) {

            int height = shouldSelect ? config.selectedEmojiHeight : LinearLayout.LayoutParams.MATCH_PARENT;
            float weight = currentWeight;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, height, weight);

            int left = shouldSelect ? config.selectedEmojiMarginLeft : config.unselectedEmojiMarginLeft;
            int top = shouldSelect ? config.selectedEmojiMarginTop : config.unselectedEmojiMarginTop;
            int bottom = shouldSelect ? config.selectedEmojiMarginBottom : config.unselectedEmojiMarginBottom;
            if (index == 0)
                params.setMargins(config.emojiImagesContainerPaddingLeft, top, left, bottom);
            else if (index == config.emojis.size() - 1)
                params.setMargins(0, top, config.emojiImagesContainerPaddingRight, bottom);
            else params.setMargins(0, top, left, bottom);

            view.setLayoutParams(params);

            if (shouldSelect && step > 0) {
                float normalizedCurrentWeight = IntervalConverter
                        .convertNumber(currentWeight)
                        .fromInterval(0, targetWeight)
                        .toInterval(initialWeight, targetWeight);

                float animationPercent = normalizedCurrentWeight / targetWeight;
                view.onWeightAnimated(animationPercent);
            } else {
                view.onWeightAnimated(0);
            }
        } else {
            cancel();
        }
    }

    @Override
    public boolean willChangeBounds() {
        //标识动画是否会影响View的边界，透明度动画不会影响，而 >100%的缩放动画会影响边界；
        // 注意，这里影响了边界也并不会导致View重新布局(layout)；不管View缩放到多大，都只会影响View的可视宽高
        return true;
    }
}
