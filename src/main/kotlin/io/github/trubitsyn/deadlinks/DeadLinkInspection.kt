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

package io.github.trubitsyn.deadlinks

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.htmlInspections.HtmlLocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

class DeadLinkInspection : HtmlLocalInspectionTool() {

    companion object {
        private const val DESCRIPTION_TEMPLATE = "Dead link"
        private const val ATTR_HREF = "href"
    }

    override fun checkAttribute(attribute: XmlAttribute, holder: ProblemsHolder, isOnTheFly: Boolean) {
        if (attribute.nameElement?.text == ATTR_HREF) {
            attribute.valueElement?.value?.let { link ->
                if (link.isUrl()) {
                    link.checkIsDeadLinkWithProgress {
                        holder.registerProblem(attribute.valueElement!!, DESCRIPTION_TEMPLATE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                    }
                }
            }
        }
    }
}