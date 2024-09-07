package com.suelee.maps.ui.searh;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suelee.maps.R;
import com.suelee.maps.data.FlowerShop;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    private List<FlowerShop> flowerShopList;


    public SearchAdapter() {
    }

    public void setFlowerShopList(List<FlowerShop> flowerShopList) {
        this.flowerShopList = flowerShopList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_one, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        FlowerShop flowerShop = flowerShopList.get(i);
        itemViewHolder.tvName.setText(flowerShop.getMark().getName());
        itemViewHolder.tvType.setText(flowerShop.getType());
        itemViewHolder.tvService.setText(flowerShop.getServices());
        itemViewHolder.tvReviews.setText(flowerShop.getReviews());
    }

    @Override
    public int getItemCount() {
        return flowerShopList == null ? 0 : flowerShopList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvType;
        TextView tvService;
        TextView tvReviews;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvService = itemView.findViewById(R.id.tvService);
            tvReviews = itemView.findViewById(R.id.tvReviews);
        }
    }
}
