package com.whatsmode.shopify.block.account;

import com.shopify.buy3.Storefront;

/**
 * Created by tom on 17-11-16.
 */

public class UserErrorFragment implements Storefront.UserErrorQueryDefinition {
    @Override
    public void define(Storefront.UserErrorQuery _queryBuilder) {
        _queryBuilder.field().message();
    }
}
