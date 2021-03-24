package com.stringconcat.ddd.shop.usecase.cart

import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CustomerId

interface CartExtractor {
    fun getCart(forCustomer: CustomerId): Cart?
}