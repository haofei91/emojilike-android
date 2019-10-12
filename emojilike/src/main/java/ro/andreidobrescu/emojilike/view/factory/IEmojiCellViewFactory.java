package ro.andreidobrescu.emojilike.view.factory;

import android.content.Context;

import ro.andreidobrescu.emojilike.view.EmojiCellView;

public interface IEmojiCellViewFactory
{
    public EmojiCellView newInstance(Context context);
}
