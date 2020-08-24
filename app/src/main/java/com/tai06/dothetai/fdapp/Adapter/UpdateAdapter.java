package com.tai06.dothetai.fdapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tai06.dothetai.fdapp.Activity.ViewMoreActivity;
import com.tai06.dothetai.fdapp.AdminActivity.UpdateSanPhamActivity;
import com.tai06.dothetai.fdapp.OOP.Sanpham;
import com.tai06.dothetai.fdapp.R;

import java.util.List;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {

    private List<Sanpham> mList;
    private Context mContext;

    public UpdateAdapter(List<Sanpham> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UpdateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateAdapter.ViewHolder holder, int position) {
        Sanpham sanpham = mList.get(position);
        Picasso.get().load(sanpham.getImage()).into(holder.img_product);
        holder.name_product.setText(sanpham.getTen_sp());
        holder.price.setText("Giá sản phẩm: "+sanpham.getGia_sp()+"VNĐ");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView name_product,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            name_product = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sanpham sanpham = mList.get(getAdapterPosition());
                    ((UpdateSanPhamActivity)mContext).showSanpham(sanpham);
                }
            });
        }
    }
}
