package com.alexey.minay.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*
import java.util.*

@Suppress("UnstableApiUsage")
class GlobalScopeDetector: Detector(), Detector.UastScanner  {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf<Class<out UElement>>(USimpleNameReferenceExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {

            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
                if (node.identifier == "GlobalScope") {
                    context.report(
                        issue = ISSUE,
                        scope = node,
                        location = context.getLocation(node),
                        message = BRIEF
                    )
                }
            }
        }
    }

    companion object {

        private const val BRIEF = "brief description"
        private const val EXPLANATION = "explanation"
        private const val ID = "id"

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