package com.zhihu.daily.meizu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class DynamicView extends View
{
  public DynamicView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    setMeasuredDimension(i, i);
  }
}