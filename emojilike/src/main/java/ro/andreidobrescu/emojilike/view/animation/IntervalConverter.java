package ro.andreidobrescu.emojilike.view.animation;

//shifts and scales point x from interval [startTarget,endTarget] to corresponding point from interval [c,d]
//https://math.stackexchange.com/a/914843
public class IntervalConverter {
    private float current, startTarget, endTarget;

    private IntervalConverter(float current)
    {
        this.current = current;
    }

    public static IntervalConverter convertNumber(float current)
    {
        return new IntervalConverter(current);
    }

    public IntervalConverter fromInterval(float startTarget, float endTarget)
    {
        this.startTarget=startTarget;
        this.endTarget=endTarget;
        return this;
    }

    public float toInterval(float c, float d)
    {
        return c+((d-c)/(endTarget-startTarget))*(current-startTarget);
    }
}
