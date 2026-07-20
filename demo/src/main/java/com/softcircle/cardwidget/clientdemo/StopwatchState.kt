package com.softcircle.cardwidget.clientdemo

/**
 * 进程内共享秒表：主界面与远程面板、面板重建后共用同一计时。
 */
object StopwatchState {
    @Volatile
    private var startedAtElapsedRealtime: Long = 0L

    @Volatile
    private var running: Boolean = false

    fun ensureRunning() {
        if (!running) resetAndStart()
    }

    fun resetAndStart() {
        startedAtElapsedRealtime = android.os.SystemClock.elapsedRealtime()
        running = true
    }

    fun elapsedMs(): Long {
        if (!running) return 0L
        return android.os.SystemClock.elapsedRealtime() - startedAtElapsedRealtime
    }
}
