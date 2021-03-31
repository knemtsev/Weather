package com.nnsoft.weather.util

import android.util.Base64
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.roundToInt

fun timeInMinutes():Long = GregorianCalendar.getInstance().timeInMillis/60000

fun locRound(loc: Double?) : Float {
    return if(loc!=null)
        (loc * 100).roundToInt().toFloat()/100
    else 0F
}


fun UUIDBase64(): String {
    val uuid: UUID = UUID.randomUUID()
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(uuid.mostSignificantBits)
    bb.putLong(uuid.leastSignificantBits)
    return Base64.encodeToString(bb.array(), Base64.URL_SAFE + Base64.NO_WRAP).trim('=')
}
