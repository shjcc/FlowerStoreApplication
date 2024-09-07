package com.suelee.maps.ui.searh;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.suelee.maps.data.DataHelper;
import com.suelee.maps.data.FlowerShop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchViewModel extends ViewModel {


    private final MutableLiveData<List<FlowerShop>> mData;

    public SearchViewModel() {
        mData = new MutableLiveData<>();
        mData.setValue(DataHelper.getFlowerShopList());
    }

    public MutableLiveData<List<FlowerShop>> getData() {
        return mData;
    }

    public void search(String content) {
        if (TextUtils.isEmpty(content)) {
            mData.setValue(DataHelper.getFlowerShopList());
            DataHelper.getSearchFlowerShopList().clear();
            DataHelper.getSearchFlowerShopList().addAll(DataHelper.getFlowerShopList());
        } else {
            List<FlowerShop> searchList = new ArrayList<>();
            for (FlowerShop flowerShop : Objects.requireNonNull(mData.getValue())) {
                if (flowerShop.getMark().getName().contains(content)
                        || flowerShop.getType().contains(content)
                        || flowerShop.getServices().contains(content)
                        || flowerShop.getReviews().contains(content)) {
                    searchList.add(flowerShop);

                }
            }
            DataHelper.getSearchFlowerShopList().clear();
            DataHelper.getSearchFlowerShopList().addAll(searchList);
            mData.setValue(searchList);
        }
    }
}