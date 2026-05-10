package androidx.media3.ui.overlayView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.ui.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedIndicator extends LinearLayout {

  private RecyclerView rvSpeed;
  private SpeedAdapter adapter;

  public SpeedIndicator(@NonNull Context context) {
    this(context, null);
  }

  public SpeedIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    initViews(context);
  }

  private void initViews(Context context) {
    setOrientation(LinearLayout.VERTICAL);

    rvSpeed = new RecyclerView(context);
    rvSpeed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    adapter = new SpeedAdapter();
    rvSpeed.setAdapter(adapter);
    rvSpeed.setBackgroundResource(R.drawable.speed_rv_bg);

    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER_HORIZONTAL;
    addView(rvSpeed, params);
  }

  public void updateData(String[] strings) {
    List<String> list = new ArrayList<>(Arrays.asList(strings));
    adapter.updateData(list);
  }

  public void updatePosition(int position) {
    adapter.updatePosition(position);
  }
}
