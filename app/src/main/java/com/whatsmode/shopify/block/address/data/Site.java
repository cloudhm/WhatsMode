package com.whatsmode.shopify.block.address.data;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by tom on 17-11-30.
 */

public class Site implements IPickerViewData {

    public String data;

    public Site(String data) {
        this.data = data;
    }

    @Override
    public String getPickerViewText() {
        return data;
    }
}
