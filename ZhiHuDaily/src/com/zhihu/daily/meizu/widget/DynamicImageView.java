package com.zhihu.daily.meizu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class DynamicImageView extends ImageView
{
  public DynamicImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (getDrawable() != null)
    {
      int i = View.MeasureSpec.getSize(paramInt1);
      setMeasuredDimension(i, i);
      return;
    }
    super.onMeasure(paramInt1, paramInt2);
  }
}
