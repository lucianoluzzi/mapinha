package nl.com.lucianoluzzi.mapinha.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import kotlin.coroutines.EmptyCoroutineContext

@ExperimentalCoroutinesApi
class CoroutineScopeExtension : BeforeAllCallback,
    AfterAllCallback,
    ParameterResolver {

    private val dispatcher = TestCoroutineDispatcher()
    private val coroutineScope = TestCoroutineScope(EmptyCoroutineContext + dispatcher)

    override fun beforeAll(context: ExtensionContext) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(context: ExtensionContext) {
        coroutineScope.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean =
        parameterContext.parameter?.type == TestCoroutineScope::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any = coroutineScope
}