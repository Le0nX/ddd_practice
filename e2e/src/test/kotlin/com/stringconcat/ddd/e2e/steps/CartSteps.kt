package com.stringconcat.ddd.e2e.steps

import com.stringconcat.ddd.e2e.MealId
import com.stringconcat.ddd.e2e.OrderId
import com.stringconcat.ddd.e2e.checkSuccess
import com.stringconcat.ddd.e2e.telnetResponse
import com.stringconcat.dev.course.app.TestTelnetClient
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.qameta.allure.Step
import org.koin.core.KoinComponent
import ru.fix.kbdd.asserts.asString

class CartSteps : KoinComponent {

    @Step
    suspend fun `Add meal to cart`(client: TestTelnetClient, mealId: MealId) {
        client.writeCommand("add ${mealId.value}")
        client.checkSuccess()
    }

    @Step
    suspend fun `Create an order`(client: TestTelnetClient): OrderId {
        client.writeCommand("checkout street 123")

        val response = client.telnetResponse().asString()
        response shouldMatch "Order #\\d+ has been created"
        response shouldContain "Please follow this URL"

        val match = Regex("#\\d+ ").find(response)!!.value
        val id = match.substring(1, match.length - 1)

        id.isEmpty() shouldBe false

        return OrderId(id.toLong())
    }
}