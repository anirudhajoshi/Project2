package com.example.aniru.popmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by aniru on 7/21/2017.
 */

public class ViewHolder_MovieReviews extends RecyclerView.ViewHolder {

    public TextView getTv_moviereview() {
        return tv_moviereview;
    }

    public void setTv_moviereview(TextView tv_moviereview) {
        this.tv_moviereview = tv_moviereview;
    }

    private TextView tv_moviereview;

    public ViewHolder_MovieReviews(View itemView) {
        super(itemView);

        tv_moviereview = (TextView) itemView.findViewById(R.id.moviereview_list_item);
    }
}
