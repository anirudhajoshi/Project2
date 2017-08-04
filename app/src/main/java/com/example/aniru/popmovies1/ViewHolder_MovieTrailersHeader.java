package com.example.aniru.popmovies1;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aniru on 7/24/2017.
 */

public class ViewHolder_MovieTrailersHeader extends ViewHolder {

    private View vw_horizontaldivider;
    public View getVw_horizontaldivider() {
        return vw_horizontaldivider;
    }

    private TextView tv_TrailersHeading;
    public TextView getTv_TrailersHeading() {
        return tv_TrailersHeading;
    }

    public ViewHolder_MovieTrailersHeader(View itemView){
        super(itemView);

        vw_horizontaldivider = (View) itemView.findViewById(R.id.vw_horizontaldivider);
        tv_TrailersHeading = (TextView) itemView.findViewById(R.id.tv_TrailersHeading);
    }

}
