package com.hotcashew.littleme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class CharacterView extends View {

    private Bitmap runningSprite; // running loop spritesheet
    private Rect spriteDimensions;
    private long lastFrameTimestamp = 0;

    public CharacterView(Context context) {
        super(context);
        initialize();
    }

    public CharacterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CharacterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    void initialize() {
        runningSprite = BitmapFactory.decodeResource(getResources(), R.drawable.character_running);
        spriteDimensions = new Rect(0, 0, runningSprite.getWidth() / 8, runningSprite.getHeight() / 2);
    }

    // tick forward the animation
    void update() {
        long now = SystemClock.currentThreadTimeMillis();
        long frameDuration = now - lastFrameTimestamp;
        lastFrameTimestamp = now;

        // skip frames over 1 second in length
        if (frameDuration > 1000)
            return;

        // TODO: advance to next frame using time
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scale = 0.5f;
        Rect spriteSource = new Rect(0, 0, spriteDimensions.width(), spriteDimensions.height());
        RectF screenDestination = new RectF(0, 0, spriteDimensions.width() * scale, spriteDimensions.height() * scale);

        canvas.drawColor(Color.WHITE); // clear screen
        canvas.drawBitmap(runningSprite, spriteSource, screenDestination, null); // draw sprite

        postInvalidate();
    }
}
