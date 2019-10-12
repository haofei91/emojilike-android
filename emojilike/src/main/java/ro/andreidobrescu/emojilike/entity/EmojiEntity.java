package ro.andreidobrescu.emojilike.entity;

/**
 * Created by using on 7/4/2016.
 * emoji model class
 */
public class EmojiEntity
{
    private int drawable;

    private String description;

    private Object tag;

    public EmojiEntity(int drawable, String description) {
        this.drawable = drawable;
        this.description = description;
    }

    public EmojiEntity(int drawable, String description, Object tag) {
        this.drawable = drawable;
        this.description = description;
        this.tag = tag;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
