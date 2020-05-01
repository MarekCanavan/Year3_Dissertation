package com.example.diss_cbt_application.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 *Class that extends ItemDecoration for the styling of RecyclerView
 *For this application only the Vertical Height is being changed*/
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    /**
     * Constructor sets the local variable to the value parsed in when class is called
     *
     * @param verticalSpaceHeight - the height we want the spacing to be*/
    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    /**
     * Set the top and bottom spacing to be the value parsed in when class is called
     *
     * @param view - the view the spacing is occuring on
     * @param parent - the recyclerview the spacing is occuring on
     * @param state - state of the recycler view */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
        outRect.top = verticalSpaceHeight;
    }
}