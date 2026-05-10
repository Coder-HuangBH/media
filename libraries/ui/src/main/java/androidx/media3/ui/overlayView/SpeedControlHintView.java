package androidx.media3.ui.overlayView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.ui.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 速度控制提示视图
 * 包含半椭圆遮罩层、提示文本和速度选择 RecyclerView，三者共享透明度动画
 */
public class SpeedControlHintView extends FrameLayout {

  private static final float MAX_ALPHA = 0.5f;
  private static final float MIN_ALPHA = 0.3f;
  private static final long PULSE_DURATION = 800;

  private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Path backgroundPath = new Path();
  private float cornerRadius;

  private float currentAlpha = MAX_ALPHA;
  private boolean isTouchInHintArea = false;
  private boolean shouldLockSpeed = false;

  private TextView hintText;
  private RecyclerView rvSpeed;
  private SpeedAdapter adapter;
  private ValueAnimator pulseAnimator;

  public SpeedControlHintView(@NonNull Context context) {
    this(context, null);
  }

  public SpeedControlHintView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    setWillNotDraw(false);
    backgroundPaint.setColor(0xFF333333);
    backgroundPaint.setStyle(Paint.Style.FILL);

    // 内容容器：垂直排列（提示文本 + 速度选择器），居中显示
    LinearLayout contentLayout = new LinearLayout(context);
    contentLayout.setOrientation(LinearLayout.VERTICAL);
    contentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    LayoutParams contentParams = new LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    contentParams.gravity = Gravity.CENTER;
    addView(contentLayout, contentParams);

    // 提示文本
    hintText = new TextView(context);
    hintText.setText(R.string.long_press_change_speed);
    hintText.setTextSize(12);
    hintText.setTextColor(0xFFFFFFFF);
    LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    hintParams.gravity = Gravity.CENTER_HORIZONTAL;
    hintParams.topMargin = dp2px(context, 8);
//    hintParams.bottomMargin = dp2px(context, 8);
    contentLayout.addView(hintText, hintParams);

    // 速度选择 RecyclerView
    rvSpeed = new RecyclerView(context);
    rvSpeed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    adapter = new SpeedAdapter();
    rvSpeed.setAdapter(adapter);
//    rvSpeed.setBackgroundResource(R.drawable.speed_rv_bg);
    LinearLayout.LayoutParams rvParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    rvParams.gravity = Gravity.CENTER_HORIZONTAL;
    contentLayout.addView(rvSpeed, rvParams);

    applyAlpha(currentAlpha);
  }

  public void updateSpeedData(String[] strings) {
    List<String> list = new ArrayList<>(Arrays.asList(strings));
    adapter.updateData(list);
  }

  public void updateSpeedPosition(int position) {
    adapter.updatePosition(position);
  }

  public boolean shouldLockSpeed() {
    return shouldLockSpeed;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    cornerRadius = h / 2f;
    rebuildPath(w, h);
  }

  private void rebuildPath(float w, float h) {
    backgroundPath.reset();
    backgroundPath.moveTo(0, 0);
    backgroundPath.lineTo(w, 0);
    backgroundPath.lineTo(w, h - cornerRadius);
    backgroundPath.arcTo(new RectF(0, h - cornerRadius * 2, w, h), 0, 180);
    backgroundPath.lineTo(0, 0);
    backgroundPath.close();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawPath(backgroundPath, backgroundPaint);
    super.onDraw(canvas);
  }

  public void show() {
    setVisibility(View.VISIBLE);
    shouldLockSpeed = false;
    isTouchInHintArea = false;
    hintText.setText(R.string.long_press_change_speed);
    startPulse();
  }

  public void hide() {
    stopPulse();
    setVisibility(View.GONE);
  }

  public void updateTouchPosition(float screenTouchY, float viewTop, float viewBottom) {
    boolean wasInArea = isTouchInHintArea;
    isTouchInHintArea = screenTouchY >= viewTop && screenTouchY <= viewBottom;

    if (isTouchInHintArea != wasInArea) {
      if (isTouchInHintArea) {
        stopPulse();
        currentAlpha = MAX_ALPHA;
        applyAlpha(currentAlpha);
        shouldLockSpeed = true;
        hintText.setText(R.string.speed_lock_hint_release);
      } else {
        shouldLockSpeed = false;
        hintText.setText(R.string.long_press_change_speed);
        startPulse();
      }
    }
  }

  private void startPulse() {
    if (pulseAnimator != null && pulseAnimator.isRunning()) {
      return;
    }
    pulseAnimator = ValueAnimator.ofFloat(MAX_ALPHA, MIN_ALPHA);
    pulseAnimator.setDuration(PULSE_DURATION);
    pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
    pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
    pulseAnimator.setInterpolator(new LinearInterpolator());
    pulseAnimator.addUpdateListener(anim -> {
      if (!isTouchInHintArea) {
        currentAlpha = (float) anim.getAnimatedValue();
        applyAlpha(currentAlpha);
      }
    });
    pulseAnimator.start();
  }

  private void stopPulse() {
    if (pulseAnimator != null) {
      pulseAnimator.cancel();
      pulseAnimator = null;
    }
  }

  private void applyAlpha(float alpha) {
    backgroundPaint.setAlpha((int) (alpha * 255));
    hintText.setAlpha(alpha);
    rvSpeed.setAlpha(alpha);
    invalidate();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    stopPulse();
  }

  private static int dp2px(Context context, int dp) {
    return Math.round(context.getResources().getDisplayMetrics().density * dp + 0.5F);
  }
}
