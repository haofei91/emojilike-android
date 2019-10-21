package ro.andreidobrescu.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ro.andreidobrescu.emojilike.entity.EmojiEntity;
import ro.andreidobrescu.emojilike.view.EmojiCellView;
import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.touchdetector.EmojiLikeTouchDetector;
import ro.andreidobrescu.emojilike.EmojiLikeView;
import ro.andreidobrescu.emojilike.touchdetector.IActivityWithEmoji;
import ro.andreidobrescu.emojilike.listener.OnEmojiSelectedListener;
import ro.andreidobrescu.sample.fragments.FragmentActivitySample;
import ro.andreidobrescu.sample.recycler.RecyclerActivitySample;

public class MainActivity extends AppCompatActivity implements OnEmojiSelectedListener {
    EmojiLikeTouchDetector emojiLikeTouchDetector;

    @BindView(R.id.likeButton)
    ImageView likeButton;

    @BindView(R.id.emojiView)
    EmojiLikeView emojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        emojiLikeTouchDetector=new EmojiLikeTouchDetector();

        EmojiConfig.with(this)
                .on(likeButton)
                .addEmoji(new EmojiEntity(R.drawable.like, "Like"))
                .addEmoji(new EmojiEntity(R.drawable.haha, "Haha"))
                .addEmoji(new EmojiEntity(R.drawable.kiss, "Kiss"))
                .addEmoji(new EmojiEntity(R.drawable.sad, "Sad"))
                .addEmoji(new EmojiEntity(R.drawable.p, ":P"))
                .setEmojiAnimationSpeed(0.2f)
                .setEmojiCellViewFactory(EmojiCellView.WithImageAndText::new)
                .setOnEmojiSelectedListener(this)
                .setup();
    }

    @Override
    public void onEmojiSelected(EmojiEntity emoji)
    {
        Toast.makeText(this, "Selected "+emoji.getDescription(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fragmentDemo)
    public void fragmentDemo ()
    {
        Intent i=new Intent(this, FragmentActivitySample.class);
        startActivity(i);
    }

    @OnClick(R.id.recyclerDemo)
    public void recyclerDemo ()
    {
        Intent i=new Intent(this, RecyclerActivitySample.class);
        startActivity(i);
    }


}
