package com.stringconcat.ddd.shop.rest.order

import APPLICATION_HAL_JSON
import MockGetOrderById
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import io.kotest.matchers.collections.shouldHaveSize
import notFoundTypeUrl
import orderDetails
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [GetOrderByIdEndpointTest.TestConfiguration::class])
internal class GetOrderByIdEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getOrderById: MockGetOrderById

    @Test
    fun `order not found`() {
        getOrderById.response = GetOrderByIdUseCaseError.OrderNotFound.left()
        val url = "/rest/shop/v1/order/${orderId().value}"
        mockMvc.get(url)
            .andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isNotFound() }
                    content {
                        jsonPath("$.type") { notFoundTypeUrl() }
                        jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                    }
                }
            }
    }

    @Test
    fun `returned successfully`() {
        val details = orderDetails()
        details.items.shouldHaveSize(1)
        val itemDetails = details.items[0]

        getOrderById.response = details.right()
        val url = "/rest/shop/v1/order/${details.id.value}"

        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)
                    jsonPath("$.id") { value(details.id.value) }
                    jsonPath("$.address.street") { value(details.address.street) }
                    jsonPath("$.address.building") { value(details.address.building) }
                    jsonPath("$.totalPrice") { value(details.total.value) }
                    jsonPath("$.items.length()") { value(1) }
                    jsonPath("$.items[0].mealId") { value(itemDetails.mealId.value) }
                    jsonPath("$.items[0].count") { value(itemDetails.count.value) }
                    jsonPath("$.version") { value(details.version.value) }
                }
            }
        getOrderById.verifyInvoked(details.id)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)

        @Bean
        fun getOrderById() = MockGetOrderById()
    }
}