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

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

private const val port = 8080
const val fakeServerAddress = "http://localhost:$port"

fun fakeServerUp(action: () -> Unit) {
    respond(200, action)
}

fun fakeServerDown(action: () -> Unit) {
    respond(404, action)
}

private fun respond(code: Int, action: () -> Unit) {
    val server = HttpServer.create(InetSocketAddress(port), 0)
    server.createContext("/") { exchange -> exchange?.sendResponseHeaders(code, 0) }
    server.executor = null
    server.start()
    action()
    server.stop(0)
}