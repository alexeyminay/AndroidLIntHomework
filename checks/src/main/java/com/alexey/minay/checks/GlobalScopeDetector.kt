package com.alexey.minay.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.getContainingUClass

@Suppress("UnstableApiUsage")
class GlobalScopeDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf<Class<out UElement>>(USimpleNameReferenceExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {

            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
                if (node.identifier == GLOBAL_SCOPE) {
                    context.report(
                        issue = ISSUE,
                        scope = node,
                        location = context.getLocation(node),
                        message = BRIEF,
                        quickfixData = createFix(context, node)
                    )
                }
            }
        }
    }

    private fun createFix(
        context: JavaContext,
        node: USimpleNameReferenceExpression
    ): LintFix? {
        val containArtifact = context.evaluator.dependencies?.getAll()?.any {
            it.artifactAddress.contains("androidx.lifecycle:lifecycle-viewmodel-kt")
        }

        if (containArtifact != true) return null

        val viewModelElement = node.getContainingUClass()?.javaPsi
            ?.find(
                iterator = { it.superClass },
                predicate = { psiClass ->
                    psiClass.name == "ViewModel" &&
                            context.evaluator.getPackage(psiClass)?.qualifiedName == "androidx.lifecycle"
                }
            ) ?: return null

        return fix()
            .replace()
            .text(GLOBAL_SCOPE)
            .with("viewModelScope1")
            .shortenNames()
            .reformat(true)
            .build()
    }

    companion object {

        private const val BRIEF = "brief description"
        private const val EXPLANATION = "explanation"
        private const val ID = "GlobalScopeUsage"
        private const val GLOBAL_SCOPE = "GlobalScope"

        val ISSUE = Issue.create(
            id = ID,
            briefDescription = BRIEF,
            explanation = EXPLANATION,
            category = Category.LINT,
            priority = 3,
            severity = Severity.ERROR,
            implementation = Implementation(GlobalScopeDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

    }

}