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

    // running animation loop properties
    private int runDurationMsec = 1000;
    private int runProgressMsec = 0;
    private int runTotalFrames = 8;
    private int runCurrentFrameIdx = 0;

    // landscape scrolling
    private float backgroundScrolling = 0;
    private Bitmap groundBitmap;
    private Bitmap treeBitmap;

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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 1;
        options.inScaled = false;

        runningSprite = BitmapFactory.decodeResource(getResources(), R.drawable.character_running, options);
        groundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ground, options);
        treeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree, options);

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

        backgroundScrolling = (backgroundScrolling + (float) frameDuration / 2.0f) % 1024;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();

        canvas.drawColor(Color.WHITE); // clear screen
        canvas.scale((float) getWidth() / 320.0f, (float) getHeight() / 320.0f);

        drawBackground(canvas);
        drawCharacter(canvas);

        postInvalidate();
    }

    private void drawCharacter(Canvas canvas) {
        float spriteDstScale = 0.5f;
        int characterX = 60;
        Rect spriteSrc = new Rect(spriteDimensions.width() * runCurrentFrameIdx, 0, spriteDimensions.width() * runCurrentFrameIdx + spriteDimensions.width(), spriteDimensions.height());
        RectF spriteDst = new RectF(characterX, 216 - spriteDimensions.height() * spriteDstScale, characterX + spriteDimensions.width() * spriteDstScale, 216);
        canvas.drawBitmap(runningSprite, spriteSrc, spriteDst, null);
    }

    private void drawBackground(Canvas canvas) {
        // [0-1024]
        int offsetLeft = (int) Math.floor(backgroundScrolling);
        int screenWidth = 320;
        int offsetTop = 216;

        if (offsetLeft + screenWidth > 1024) {
            // two-part draw
            int right = Math.min(1024, offsetLeft + screenWidth);
            int screenRemainder = right - offsetLeft;
            Rect src = new Rect(offsetLeft, 0, right, 104); // in bitmap space
            Rect dst = new Rect(0, offsetTop, screenRemainder, offsetTop + 104); // in screen space
            canvas.drawBitmap(groundBitmap, src, dst, null);

            src = new Rect(0, 0, 320 - screenRemainder, 104); // in bitmap space
            dst = new Rect(screenRemainder, offsetTop, 320, offsetTop + 104); // in screen space
            canvas.drawBitmap(groundBitmap, src, dst, null);

        } else {
            // single draw
            Rect src = new Rect(offsetLeft, 0, offsetLeft + screenWidth, 104); // in bitmap space
            Rect dst = new Rect(0, offsetTop, screenWidth, offsetTop + 104); // in screen space
            canvas.drawBitmap(groundBitmap, src, dst, null);
        }
    }
}
