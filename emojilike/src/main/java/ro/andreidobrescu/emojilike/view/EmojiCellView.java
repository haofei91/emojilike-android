package ro.andreidobrescu.emojilike.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ro.andreidobrescu.emojilike.entity.EmojiEntity;
import ro.andreidobrescu.emojilikelib.R;

/**
 * 单个表情
 */
public abstract class EmojiCellView extends FrameLayout
{
    public EmojiCellView(@NonNull Context context)
    {
        super(context);

        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutId(), this, true);
    }

    /**
     * 布局xml
     */
    public abstract int getLayoutId();

    /**
     * 绑定表情，，在构建View时会调用和传入对应值
     */
    public abstract void setEmoji(EmojiEntity emoji);

    /**
     *
     * @param animationPercent
     */
    public void onWeightAnimated(float animationPercent) {}




    /********************** 纯图片类型  ***************************/

    public static class WithImage extends EmojiCellView
    {
        public WithImage(@NonNull Context context)
        {
            super(context);
        }

        @Override
        public int getLayoutId()
        {
            return R.layout.emoji_cell_with_image;
        }

        @Override
        public void setEmoji(EmojiEntity emoji)
        {
            ImageView imageView=findViewById(R.id.imageView);
            imageView.setImageResource(emoji.getDrawable());
        }
    }

    /********************** 图文类型类型  ***************************/


    public static class WithImageAndText extends EmojiCellView
    {
        public WithImageAndText(@NonNull Context context)
        {
            super(context);
        }

        @Override
        public int getLayoutId()
        {
            return R.layout.emoji_cell_with_image_and_text;
        }

        @Override
        public void setEmoji(EmojiEntity emoji)
        {
            ImageView imageView=findViewById(R.id.imageView);
            TextView descriptionLabel=findViewById(R.id.descriptionLabel);

            imageView.setImageResource(emoji.getDrawable());
            descriptionLabel.setText(emoji.getDescription());
        }

        @Override
        public void onWeightAnimated(float animationPercent)
        {
            TextView descriptionLabel=findViewById(R.id.descriptionLabel);
            descriptionLabel.setAlpha(animationPercent);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int)(animationPercent*getResources().getDimensionPixelSize(R.dimen.default_emoji_description_label_height)));
            params.bottomMargin=getResources().getDimensionPixelSize(R.dimen.default_emoji_description_label_bottom_margin);
            params.gravity=Gravity.CENTER_HORIZONTAL;
            descriptionLabel.setLayoutParams(params);
        }
    }
}
