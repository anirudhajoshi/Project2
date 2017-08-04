package com.example.aniru.popmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by aniru on 7/24/2017.
 */

public class ViewHolder_MovieReviewsHeader extends RecyclerView.ViewHolder {

    private View vw_horizontaldivider;

    public View getVw_horizontaldivider() {
        return vw_horizontaldivider;
    }

    private TextView tv_ReviewsHeading;

    public TextView getTv_ReviewssHeading() {
        return tv_ReviewsHeading;
    }

    public ViewHolder_MovieReviewsHeader(View itemView) {
        super(itemView);
        vw_horizontaldivider = (View) itemView.findViewById(R.id.vw_horizontaldivider);
        tv_ReviewsHeading = (TextView) itemView.findViewById(R.id.tv_ReviewHeading);
    }
}
