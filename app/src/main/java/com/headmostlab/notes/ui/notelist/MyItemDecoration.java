package com.headmostlab.notes.ui.notelist;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.headmostlab.notes.R;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private final Context context;

    public MyItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, context.getResources().getDimensionPixelSize(R.dimen.view_margin));
    }
}
