package com.challange.atomictracker.core.data.ws

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Transport over Postman's raw echo WebSocket: connect, send text, await echoed text, and reconnect with backoff.
 */
@Singleton
class PostmanEchoWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient
) {

    /**
     * Keeps reconnecting after each disconnect or transport failure. [block] runs for each successful socket open.
     * Stops when the calling coroutine is cancelled.
     */
    suspend fun connect(
        url: String = DEFAULT_URL,
        onConnectionChange: (connected: Boolean) -> Unit,
        block: suspend (EchoWebSocketSession) -> Unit,
    ) {
        var backoffMs = INITIAL_RECONNECT_DELAY_MS
        while (true) {
            try {
                openSession(url, onConnectionChange, block)
                backoffMs = INITIAL_RECONNECT_DELAY_MS
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onConnectionChange(false)
                delay(backoffMs)
                backoffMs = (backoffMs * 2)
                    .coerceAtMost(MAX_RECONNECT_DELAY_MS)
            }
        }
    }

    /**
     * Opens one WebSocket session, reports [onConnectionChange], then runs [block] with a [EchoWebSocketSession].
     * Suspends until the socket closes or fails; cancels the in-flight [block] when the scope is cancelled.
     */
    suspend fun openSession(
        url: String = DEFAULT_URL,
        onConnectionChange: (connected: Boolean) -> Unit,
        block: suspend (EchoWebSocketSession) -> Unit,
    ) = coroutineScope {
        val sessionDone = CompletableDeferred<Unit>()
        val echoes = Channel<String>(Channel.UNLIMITED)
        val webSocketRef = AtomicReference<WebSocket?>(null)
        val sendMutex = Mutex()

        fun endSession(error: Throwable?) {
            onConnectionChange(false)
            echoes.close()
            when {
                error != null && sessionDone.isActive -> sessionDone.completeExceptionally(error)
                sessionDone.isActive -> sessionDone.complete(Unit)
            }
        }

        val session = object : EchoWebSocketSession {
            override suspend fun sendTextAwaitEcho(text: String, timeoutMs: Long): String? {
                val ws = webSocketRef.get() ?: return null
                if (!sendMutex.withLock { ws.send(text) }) return null
                return withTimeoutOrNull(timeoutMs) { echoes.receive() }
            }
        }

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocketRef.set(webSocket)
                onConnectionChange(true)
                launch {
                    try {
                        block(session)
                    } finally {
                        webSocket.cancel()
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                echoes.trySend(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.cancel()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                endSession(error = null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                endSession(error = t)
            }
        }

        val webSocket = okHttpClient.newWebSocket(Request.Builder().url(url).build(), listener)

        try {
            sessionDone.await()
        } finally {
            webSocket.cancel()
            webSocketRef.set(null)
        }
    }

    companion object {
        const val DEFAULT_URL = "wss://ws.postman-echo.com/raw"
        const val INITIAL_RECONNECT_DELAY_MS = 2_000L
        const val MAX_RECONNECT_DELAY_MS = 60_000L
    }
}

interface EchoWebSocketSession {
    suspend fun sendTextAwaitEcho(text: String, timeoutMs: Long): String?
}
