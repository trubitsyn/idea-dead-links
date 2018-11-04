/*
 * Copyright 2018 Nikola Trubitsyn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trubitsyn.deadlinks

import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.junit.Test

class DeadLinkInspectionTest : LightPlatformCodeInsightFixtureTestCase() {

    @Test
    fun testDeadLinkDetected() {
        val inspection = DeadLinkInspection()
        myFixture.configureByText(HtmlFileType.INSTANCE, """
            <link rel="stylesheet" href=<warning descr="Dead link">"$fakeServerAddress"</warning>/>
        """.trimIndent())
        myFixture.enableInspections(inspection)
        fakeServerDown {
            myFixture.checkHighlighting(true, false, false)
        }
    }

    @Test
    fun testNoDeadLinkDetected_ok() {
        val inspection = DeadLinkInspection()
        myFixture.configureByText(HtmlFileType.INSTANCE, """
            <link rel="stylesheet" href="$fakeServerAddress"/>
        """.trimIndent())
        myFixture.enableInspections(inspection)
        fakeServerUp {
            myFixture.checkHighlighting(true, false, false)
        }
    }

    @Test
    fun testNoDeadLinkDetected_ignored() {
        val inspection = DeadLinkInspection()
        myFixture.configureByText(HtmlFileType.INSTANCE, """
            <link rel="stylesheet" href="style.css"/>
        """.trimIndent())
        myFixture.enableInspections(inspection)
        myFixture.checkHighlighting(true, false, false)
    }
}