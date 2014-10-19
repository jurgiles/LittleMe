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
import android.util.Log;
import android.view.View;

public class CharacterView extends View {
    private Bitmap runningSprite; // running loop spritesheet
    private Rect spriteDimensions;
    private long lastFrameTimestamp = 0;

    // running animation loop properties
    private int runDurationMsec = 1000;
    private int runProgressMsec = 0;
    private int runTotalFrames = 8;
    private int runCurrentFrameIdx = 0;

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
        long now = SystemClock.elapsedRealtime();
        long frameDuration = now - lastFrameTimestamp;
        lastFrameTimestamp = now;

        // skip frames over 1 second in length
        if (frameDuration > 1000)
            return;

        runProgressMsec = (runProgressMsec + (int) frameDuration) % runDurationMsec;
        runCurrentFrameIdx = (int) Math.floor((float) runTotalFrames * (float) runProgressMsec / (float) runDurationMsec);
//        Log.d("CharacterView", String.format("frame idx: %d (frame duration=%d msec | total=%d msec)", runCurrentFrameIdx, frameDuration, runProgressMsec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();

        float spriteDstScale = 0.5f;
        Rect spriteSrc = new Rect(spriteDimensions.width() * runCurrentFrameIdx, 0, spriteDimensions.width() * runCurrentFrameIdx + spriteDimensions.width(), spriteDimensions.height());
        RectF spriteDst = new RectF(0, 0, spriteDimensions.width() * spriteDstScale, spriteDimensions.height() * spriteDstScale);

        canvas.drawColor(Color.WHITE); // clear screen
        canvas.drawBitmap(runningSprite, spriteSrc, spriteDst, null); // draw sprite

        postInvalidate();
    }
}
