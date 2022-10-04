package com.alexey.minay.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

internal class GlobalScopeDetectorTest {

    @Test
    fun `first`() {
        lint()
            .allowMissingSdk()
            .files(
                kotlin(
                    """
                        package checks

                        var scope = GlobalScope

                        class TestClass {
                            fun onCreate() {
                                GlobalScope.launch{ }
                            }
                        }
                    """.trimIndent()
                )
            )
            .issues(GlobalScopeDetector.ISSUE)
            .run()
            .expectErrorCount(2)
    }

    @Test
    fun `second`() {
        lint()
            .allowMissingSdk()
            .files(
                kotlin(
                    """
                        package checks

                        class TestViewModel : TestParentViewModel() {
                            fun onCreate() {
                                GlobalScope.launch{ }
                            }
                        }
                    """.trimIndent()
                ),
                kotlin(
                    """
                        package checks
    
                        import androidx.lifecycle.ViewModel

                        class TestParentViewModel : ViewModel() {

                        }
                    """.trimIndent()
                ),
                kotlin(
                    """
                        package androidx.lifecycle

                        class ViewModel {

                        }
                    """.trimIndent()
                )
            )
            .issues(GlobalScopeDetector.ISSUE)
            .run()
            .expectErrorCount(1)
    }

}