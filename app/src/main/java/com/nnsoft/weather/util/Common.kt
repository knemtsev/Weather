package com.nnsoft.weather.util

import android.util.Base64
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

val TAG="WEATHER"

object Common {
    private const val TO_MINUTES_DIVIDER=60000
    fun timeInMinutes(): Long = GregorianCalendar.getInstance().timeInMillis / TO_MINUTES_DIVIDER
    fun minutes2Millis(timeInMin:Long) = timeInMin* TO_MINUTES_DIVIDER

    fun minutes2Date(timeImMin:Long): Date {
        val cal=GregorianCalendar.getInstance()
        cal.timeInMillis = minutes2Millis(timeImMin)
        return cal.time
    }
    fun minutes2DateS(timeImMin:Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(minutes2Date(timeImMin))
    }


    fun locRound(loc: Double?): Float {
        return if (loc != null)
            (loc * 100).roundToInt().toFloat() / 100
        else 0F
    }


    fun UUIDBase64(): String {
        val uuid: UUID = UUID.randomUUID()
        val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)
        return Base64.encodeToString(bb.array(), Base64.URL_SAFE + Base64.NO_WRAP).trim('=')
    }
}