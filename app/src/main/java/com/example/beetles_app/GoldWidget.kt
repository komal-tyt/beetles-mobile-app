package com.example.beetles_app

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.beetles_app.data.GoldApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class GoldWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_gold_layout)

        // Ставим заглушку, пока грузится
        views.setTextViewText(R.id.widget_price_text, "Загрузка...")
        appWidgetManager.updateAppWidget(appWidgetId, views)

        // Запускаем фоновую загрузку курса
        CoroutineScope(Dispatchers.IO).launch {
            val price = GoldApi.getGoldPrice(context)
            val priceText = "${String.format(Locale.US, "%.2f", price)} ₽"

            // Обновляем UI в главном потоке
            views.setTextViewText(R.id.widget_price_text, priceText)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}