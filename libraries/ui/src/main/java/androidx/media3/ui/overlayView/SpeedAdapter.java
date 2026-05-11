package androidx.media3.ui.overlayView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.media3.ui.R;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SpeedAdapter extends RecyclerView.Adapter<SpeedAdapter.ViewHolder> {

  // 数据集合
  private final List<String> data = new ArrayList<>();

  private int selectedPosition = 0;

  private final int selectedColor = Color.parseColor("#FFFFFF");
  private final int unselectedColor = Color.parseColor("#B0FFFFFF");

  // 创建ViewHolder，加载item布局
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // 加载简单的item布局（仅包含一个TextView）
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rv_speed_item, parent, false);
    return new ViewHolder(view);
  }

  // 绑定数据到ViewHolder
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // 将对应位置的数据设置到TextView
    holder.textView.setText(data.get(position));

    boolean selected = selectedPosition == position;
    holder.textView.setTextColor(selected ? selectedColor : unselectedColor);
    holder.imageView.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
  }

  public void updateData(List<String> list) {
    data.clear();
    data.addAll(list);

    notifyDataSetChanged();
  }

  public void updatePosition(int position) {
    this.selectedPosition = position;
    notifyDataSetChanged();
  }

  // 返回数据数量
  @Override
  public int getItemCount() {
    return data.size();
  }

  // ViewHolder内部类，持有item视图
  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;

    public ViewHolder(View itemView) {
      super(itemView);
      // 获取item布局中的TextView
      textView = itemView.findViewById(R.id.tv_title);
      imageView = itemView.findViewById(R.id.iv_icon);
    }
  }
}
