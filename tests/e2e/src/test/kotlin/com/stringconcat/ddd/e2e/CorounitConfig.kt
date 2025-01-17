package com.stringconcat.ddd.e2e

import com.stringconcat.ddd.e2e.steps.CartSteps
import com.stringconcat.ddd.e2e.steps.CrmSteps
import com.stringconcat.ddd.e2e.steps.MenuSteps
import com.stringconcat.ddd.e2e.steps.OrderSteps
import com.stringconcat.ddd.e2e.steps.UrlSteps
import kotlin.coroutines.CoroutineContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.fix.corounit.allure.createStepClassInstance
import ru.fix.corounit.engine.CorounitPlugin
import ru.fix.kbdd.rest.Rest

object CorounitConfig : CorounitPlugin {

    init {
        Rest.threadPoolSize = 10
    }

    override suspend fun beforeAllTestClasses(globalContext: CoroutineContext): CoroutineContext {
        val settings = Settings()
        startKoin {
            printLogger()
            modules(module {
                single { createStepClassInstance(UrlSteps::class) }
                single { createStepClassInstance(MenuSteps::class) }
                single { createStepClassInstance(CartSteps::class) }
                single { createStepClassInstance(OrderSteps::class) }
                single { createStepClassInstance(CrmSteps::class) }
                single { settings }
            })
        }

        return super.beforeAllTestClasses(globalContext)
    }

    override suspend fun afterAllTestClasses(globalContext: CoroutineContext) {
        super.afterAllTestClasses(globalContext)
    }
}