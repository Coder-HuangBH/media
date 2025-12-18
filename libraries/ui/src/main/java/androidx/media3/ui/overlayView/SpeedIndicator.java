package androidx.media3.ui.overlayView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
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

public class SpeedIndicator extends LinearLayout {

  private SpeedAdapter adapter;

  public SpeedIndicator(@NonNull Context context) {
    this(context, null);
  }

  public SpeedIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    initViews(context);
  }

  private void initViews(Context context) {
    TextView tvFixedSpeedTip = new TextView(context);
    tvFixedSpeedTip.setTextSize(12);
    tvFixedSpeedTip.setTextColor(Color.WHITE);
    tvFixedSpeedTip.setText(R.string.long_press_uplift_fixed_speed);

    TextView tvSpeedsTip = new TextView(context);
    tvSpeedsTip.setTextSize(12);
    tvSpeedsTip.setTextColor(Color.WHITE);
    tvSpeedsTip.setText(R.string.long_press_change_speed);

    RecyclerView rvSpeed = new RecyclerView(context);
    rvSpeed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    adapter = new SpeedAdapter();
    rvSpeed.setAdapter(adapter);
    rvSpeed.setBackgroundResource(R.drawable.speed_rv_bg);
    setOrientation(LinearLayout.VERTICAL);
    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER_HORIZONTAL;

    addView(tvFixedSpeedTip, params);
    addView(rvSpeed, params);
    addView(tvSpeedsTip, params);
  }

  public void updateData(String[] strings) {
    List<String> list = new ArrayList<>(Arrays.asList(strings));
    adapter.updateData(list);
  }

  public void updatePosition(int position) {
    adapter.updatePosition(position);
  }
}
