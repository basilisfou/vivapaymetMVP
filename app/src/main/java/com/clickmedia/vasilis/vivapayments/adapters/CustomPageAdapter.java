package com.clickmedia.vasilis.vivapayments.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clickmedia.vasilis.vivapayments.R;

import java.util.ArrayList;



/**
 * This adapter is for the View Pager in Activity Details
 */
public class CustomPageAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] images;
    private ProgressBar mProgressBar;



    public CustomPageAdapter(Context pContext, ProgressBar progressBar, String[] images){
        this.mContext = pContext;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;

        this.mProgressBar = progressBar;



    }
    //This method should return the number of views available, i.e., number of pages to
    // be displayed/created in the ViewPager.
    @Override
    public int getCount() {
        return images.length;
    }
    /*
    checks whether the View passed to it (representing the page) is associated with that key or not.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        Glide.with(mContext).load(images[position])
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).into(imageView);

        container.addView(itemView);

        return itemView;
    }
    //Removes the page from the container for the given position
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
