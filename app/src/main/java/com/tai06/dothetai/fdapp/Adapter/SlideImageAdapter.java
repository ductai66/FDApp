package com.tai06.dothetai.fdapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.OOP.SlideImage;
import com.tai06.dothetai.fdapp.R;

import java.util.List;

public class SlideImageAdapter extends PagerAdapter {

    private List<SlideImage> mList;
    private Fragment mFrag;

    public SlideImageAdapter(List<SlideImage> mList, Fragment mFrag) {
        this.mList = mList;
        this.mFrag = mFrag;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object); // view == object
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        LayoutInflater inflater = (LayoutInflater) mFrag.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.slide_item, container, false);
        View view = LayoutInflater.from(mFrag.getContext()).inflate(R.layout.slide_item, container, false);
        ImageView imageView = view.findViewById(R.id.img_item_slide);
        Picasso.get().load(mList.get(position).getImage()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
