package com.example.beetles_app.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Xml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object GoldApi {
    private const val PREFS_NAME = "gold_price_prefs"
    private const val KEY_GOLD_PRICE = "gold_price"
    private const val DEFAULT_PRICE = 7500f

    private var lastKnownPrice: Float = DEFAULT_PRICE

    suspend fun getGoldPrice(context: Context? = null): Float = withContext(Dispatchers.IO) {
        val prefs = context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Сначала пытаемся загрузить из сети
        try {
            val price = fetchFromNetwork()
            if (price > 0f) {
                lastKnownPrice = price
                prefs?.edit()?.putFloat(KEY_GOLD_PRICE, price)?.apply()
                return@withContext price
            }
        } catch (e: Exception) {
            Log.e("GoldApi", "Network error: ${e.message}")
        }

        // 2. Если сеть упала, берем из кэша (SharedPreferences)
        val cachedPrice = prefs?.getFloat(KEY_GOLD_PRICE, lastKnownPrice) ?: lastKnownPrice
        return@withContext cachedPrice
    }

    private fun fetchFromNetwork(): Float {
        // Пробуем текущий месяц (с 1 числа по сегодня)
        val currentMonthPrice = fetchPriceForMonth(0)
        if (currentMonthPrice > 0f) return currentMonthPrice

        // Если за текущий месяц пусто, пробуем предыдущий месяц
        return fetchPriceForMonth(-1)
    }

    private fun fetchPriceForMonth(monthOffset: Int): Float {
        try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()

            // Начало месяца
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.add(Calendar.MONTH, monthOffset)
            val date1 = format.format(calendar.time)

            // Конец месяца (или сегодняшний день)
            if (monthOffset == 0) {
                calendar.time = Calendar.getInstance().time
            } else {
                calendar.add(Calendar.MONTH, 1)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }
            val date2 = format.format(calendar.time)

            val urlString = "http://www.cbr.ru/scripts/xml_metall.asp?date_req1=$date1&date_req2=$date2"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            if (connection.responseCode == 200) {
                val stream: InputStream = connection.inputStream
                val parser = Xml.newPullParser()

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(stream, "UTF-8")

                var eventType = parser.eventType
                var currentCode = ""
                var currentBuy = ""
                var lastGoldPrice = 0f

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        val tagName = parser.name
                        if (tagName == "Record") {
                            currentCode = parser.getAttributeValue(null, "Code") ?: ""
                        } else if (tagName == "Buy") {
                            currentBuy = parser.nextText()
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (parser.name == "Record") {
                            if (currentCode == "1" && currentBuy.isNotEmpty()) {
                                lastGoldPrice = currentBuy.replace(",", ".").toFloatOrNull() ?: 0f
                            }
                        }
                    }
                    eventType = parser.next()
                }

                stream.close()
                connection.disconnect()
                return lastGoldPrice
            }
        } catch (e: Exception) {
            Log.e("GoldApi", "Error fetching month $monthOffset: ${e.message}")
        }
        return 0f
    }
}