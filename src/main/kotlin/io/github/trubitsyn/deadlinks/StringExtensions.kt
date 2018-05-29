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

import com.intellij.openapi.progress.ProgressManager
import java.net.HttpURLConnection
import java.net.URL

fun String.checkIsDeadLinkWithProgress(onDeadLinkFound: () -> Unit) {
    ProgressManager.getInstance().executeProcessUnderProgress({
        if (isDeadLink()) {
            onDeadLinkFound()
        }
    }, ProgressManager.getGlobalProgressIndicator())
}

fun String.isUrl(): Boolean {
    return startsWith("http://") || startsWith("https://") || startsWith("//")
}

fun String.isDeadLink(): Boolean {
    return try {
        val connection = URL(this).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        val code = connection.responseCode
        connection.disconnect()
        code != 200
    } catch (e: Exception) {
        true
    }
}