package ro.andreidobrescu.emojilike;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.List;

import ro.andreidobrescu.emojilike.entity.EmojiEntity;
import ro.andreidobrescu.emojilike.touchdetector.EmojiTriggerManager;
import ro.andreidobrescu.emojilike.view.EmojiCellView;
import ro.andreidobrescu.emojilike.view.animation.ViewWeightAnimation;
import ro.andreidobrescu.emojilikelib.R;

/**
 * Created by using on 7/4/2016.
 */
public class EmojiLikeView extends RelativeLayout implements View.OnTouchListener {
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
        this(context, null);
    }

    public EmojiLikeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiLikeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundColor(Color.TRANSPARENT);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.emoji_like_view_layout, this, true);

    }
    /*****************  初始化 *********************/

    public void configure(EmojiConfig config) {
        this.config = config;
        this.emojiCellViews = new LinkedList<>();
        this.selectedEmoji = -1;

        emojiCellsContainer = findViewById(R.id.emoji_container);

        //3. 表情items
        for (int i = 0; i < config.emojis.size(); i++) {
            //creating emoji cell views
            EmojiEntity emoji = config.emojis.get(i);

            EmojiCellView cellView = config.cellViewFactory.newInstance(getContext());
            cellView.setLayoutParams(getDefaultLayoutParams(i));
            cellView.onWeightAnimated(0);
            cellView.setEmoji(emoji);
            cellView.setTag(emoji);

            cellView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v instanceof EmojiCellView){
                        config.onEmojiSelectedListener.onEmojiSelected((EmojiEntity)v.getTag());
                    }
                }
            });

            this.emojiCellsContainer.addView(cellView);
            this.emojiCellViews.add(cellView);
        }

        setOnTouchListener(this);
    }
    /**
     * 构建itemViews
     */
    public void configure1(EmojiConfig config) {
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
            //params.setMargins(config.backgroundViewMarginLeft, 0, config.backgroundViewMarginRight, config.backgroundViewMarginBottom);
            emojiBackgroundView.setLayoutParams(params);

            emojiBackgroundView.setVisibility(View.VISIBLE);
            this.addView(emojiBackgroundView);
        }

        //2. 表情容器
        this.emojiCellsContainer = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, config.emojiImagesContainerHeight);
        //params.setMargins(config.backgroundViewMarginLeft, 0, config.backgroundViewMarginRight, config.backgroundViewMarginBottom);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.emojiCellsContainer.setLayoutParams(params);
        this.emojiCellsContainer.setVisibility(View.VISIBLE);
        this.addView(emojiCellsContainer);

        //3. 表情items
        for (int i = 0; i < config.emojis.size(); i++) {
            //creating emoji cell views
            EmojiEntity emoji = config.emojis.get(i);

            EmojiCellView cellView = config.cellViewFactory.newInstance(getContext());
            cellView.setLayoutParams(getDefaultLayoutParams(i));
            cellView.onWeightAnimated(0);
            cellView.setEmoji(emoji);
            cellView.setTag(emoji);

            cellView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v instanceof EmojiCellView){
                        config.onEmojiSelectedListener.onEmojiSelected((EmojiEntity)v.getTag());
                    }
                }
            });

            this.emojiCellsContainer.addView(cellView);
            this.emojiCellViews.add(cellView);
        }

        setOnTouchListener(this);

        this.bringToFront();
    }

    //表情item的默认布局
    private LinearLayout.LayoutParams getDefaultLayoutParams(int viewIndex) {
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int left = config.unselectedEmojiMarginLeft;
        int top = config.unselectedEmojiMarginTop;
        int bottom = config.unselectedEmojiMarginBottom;
        if (viewIndex == 0)
            params.setMargins(config.emojiImagesContainerPaddingLeft, 0, left, bottom);
        else if (viewIndex == config.emojis.size() - 1)
            params.setMargins(0, 0, config.emojiImagesContainerPaddingRight, bottom);
        else
            params.setMargins(0, 0, left, bottom);

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            onTouchDown(x, y);
            return true;
        }else if (action == MotionEvent.ACTION_MOVE) {
            onTouchMove(x, y);
            return true;
        }else if (action == MotionEvent.ACTION_UP) {
            onTouchUp(x, y);
            return true;
        }

        return false;
    }


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
        int maxY = getHeight();
        int minY = (int)getY();
        int maxX = getWidth();
        int minX = (int) getX();
        int index = (int) (((x - minX) / (float) maxX) * config.emojis.size());

        //if (x < minX || x > maxX + minX || y<minY || minY > minY+maxY) {
        if (!EmojiTriggerManager.intersectView(this, (int)x, (int)y)) {
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
            growView(view, index, view.getScaleX(), 2, 21, 55, config.emojiAnimationSpeed, true);
        }
    }


    private void growView(EmojiCellView view, int index, float initWeight, float maxWeight, int bottommargin, int imageWidth, float step, boolean shouldSelect) {
        ValueAnimator animator = ValueAnimator.ofInt(0,1000);
        animator.setDuration(100);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        final int viewBottommargin = params.bottomMargin;

        ImageView imageView= view.findViewById(R.id.imageView);
        LinearLayout.LayoutParams imageViewParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        final int imageViewWidth = imageViewParams.width;

       params.bottomMargin = dpToPx(bottommargin);
        view.setLayoutParams(params);
        int size = dpToPx(imageWidth);
        imageViewParams.width = size;
        imageViewParams.height = size;

        /*animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int fraction = (int) animation.getAnimatedValue();
                Log.e("yhf", fraction + "");

               // float scale = (float)initWeight + ((float)(fraction * (maxWeight - initWeight)));
               // view.setScaleX(scale);
              //  view.setScaleY(scale);

                params.bottomMargin = viewBottommargin + (int)(fraction * (dpToPx(bottommargin) - viewBottommargin) / 1000);
                view.setLayoutParams(params);

                //int size = imageViewWidth + ((int)(fraction * (dpToPx(imageWidth) - imageViewWidth) / 1000));
                //imageViewParams.width = size;
               // imageViewParams.height = size;


                // view.requestLayout();
              //  view.invalidate();
               // emojiCellsContainer.requestLayout();
             //   emojiCellsContainer.invalidate();

            }
        });

        animator.start();*/
    }

    private void growView1(EmojiCellView view, int index, float initWeight, float maxWeight, float step, boolean shouldSelect) {
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
            shrinkView(view, index, view.getScaleX(), 1f, -config.emojiAnimationSpeed, false);
        }
    }

    private void shrinkView(EmojiCellView view, int index, float initWeight, float maxWeight, float step, boolean forSelected) {
        growView(view, index, initWeight, maxWeight, 8, 36, step, forSelected);
    }


    public float getWeight(View view) {
        return ((LinearLayout.LayoutParams) view.getLayoutParams()).weight;
    }


    /********************* get|set ***********************/

    public boolean isShowed() {
        return this.emojiCellsContainer.getVisibility() == View.VISIBLE;
    }

    public int dpToPx(int i) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
        return (int) px;
    }
}
