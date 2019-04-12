package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaopo.flying.photolayout.layout.slant.NumberSlantLayout;
import com.xiaopo.flying.photolayout.layout.straight.NumberStraightLayout;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.SquarePuzzleView;
import java.util.ArrayList;
import java.util.List;
//创建Adapter
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder> {

  private List<PuzzleLayout> layoutData = new ArrayList<>();
  private List<Bitmap> bitmapData = new ArrayList<>();
  private OnItemClickListener onItemClickListener;
//创建viewholder
// 引用XML传给viewholder
  @Override public PuzzleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    LayoutInflater.from指定写法
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle, parent, false);
    return new PuzzleViewHolder(itemView);
  }
//操作item
  @Override public void onBindViewHolder(PuzzleViewHolder holder, int position) {
    final PuzzleLayout puzzleLayout = layoutData.get(position);

    holder.puzzleView.setNeedDrawLine(true);
    holder.puzzleView.setNeedDrawOuterLine(true);
    holder.puzzleView.setTouchEnable(false);

    holder.puzzleView.setPuzzleLayout(puzzleLayout);
//对整个itemView进行监听
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onItemClickListener != null) {
          int theme = 0;
//          判断其左边对象是否为其右边类的实例，返回boolean类型的数据
          if (puzzleLayout instanceof NumberSlantLayout) {
            theme = ((NumberSlantLayout) puzzleLayout).getTheme();
          } else if (puzzleLayout instanceof NumberStraightLayout) {
            theme = ((NumberStraightLayout) puzzleLayout).getTheme();
          }
          onItemClickListener.onItemClick(puzzleLayout, theme);
        }
      }
    });

    if (bitmapData == null) return;

    final int bitmapSize = bitmapData.size();

    if (puzzleLayout.getAreaCount() > bitmapSize) {
      for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
        holder.puzzleView.addPiece(bitmapData.get(i % bitmapSize));
      }
    } else {
      holder.puzzleView.addPieces(bitmapData);
    }
  }
//统计条目
  @Override public int getItemCount() {
    return layoutData == null ? 0 : layoutData.size();
  }

  public void refreshData(List<PuzzleLayout> layoutData, List<Bitmap> bitmapData) {
    this.layoutData = layoutData;
    this.bitmapData = bitmapData;
//引起界面重绘，会保留原有位置、数据信息
    notifyDataSetChanged();
  }
//设置item监听
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public static class PuzzleViewHolder extends RecyclerView.ViewHolder {

    SquarePuzzleView puzzleView;

    public PuzzleViewHolder(View itemView) {
      super(itemView);
      puzzleView = (SquarePuzzleView) itemView.findViewById(R.id.puzzle);
    }
  }
//接口
  public interface OnItemClickListener {
    void onItemClick(PuzzleLayout puzzleLayout, int themeId);
  }
}
