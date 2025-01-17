package com.stringconcat.ddd.shop.usecase.order.providers

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor

// можно сделать оптимизацию и загружать в юзкейсе сразу все
class MealPriceProviderImpl(private val extractor: MealExtractor) : MealPriceProvider {
    override fun getPrice(forMealId: MealId): Price {
        val meal = extractor.getById(forMealId)
        check(meal != null) {
            "Meal #$forMealId not found"
        }
        return meal.price
    }
}