package com.tai06.dothetai.fdapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tai06.dothetai.fdapp.OOP.SlideIntro;
import com.tai06.dothetai.fdapp.R;

import java.util.List;

public class SlideIntroAdapter extends PagerAdapter {

    private List<SlideIntro> mList;
    private Context mContext;

    public SlideIntroAdapter(List<SlideIntro> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_intro, container, false);
        ImageView image = view.findViewById(R.id.image_intro);
        image.setImageResource(mList.get(position).getImage());
        TextView title = view.findViewById(R.id.title_intro);
        title.setText(mList.get(position).getTitle());
        TextView content = view.findViewById(R.id.content_intro);
        content.setText(mList.get(position).getContent());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
