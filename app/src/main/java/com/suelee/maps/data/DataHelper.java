package com.suelee.maps.data;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private static List<FlowerShop> flowerShopList = new ArrayList<>();

    private static final List<FlowerShop> searchFlowerShopList = new ArrayList<>();

    public static List<FlowerShop> getFlowerShopList() {
        return flowerShopList;
    }
    public static List<FlowerShop> getSearchFlowerShopList() {
        return searchFlowerShopList;
    }


}
