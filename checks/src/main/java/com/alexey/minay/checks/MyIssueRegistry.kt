package com.alexey.minay.checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class MyIssueRegistry: IssueRegistry() {
    override val issues: List<Issue> = listOf(GlobalScopeDetector.ISSUE)
}