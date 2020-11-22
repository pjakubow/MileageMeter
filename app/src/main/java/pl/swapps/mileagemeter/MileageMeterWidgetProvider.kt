package pl.swapps.mileagemeter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.util.*

class MileageMeterWidgetProvider : AppWidgetProvider() {

    private val calculator =
        MileageCalculator(20_000, 3, ZonedDateTime.parse("2019-08-22T12:15:30+02:00[Europe/Warsaw]"))

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach { appWidgetId -> update(context, appWidgetManager, appWidgetId) }
    }

    private fun update(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int) {
        val views = RemoteViews(
            context?.packageName,
            R.layout.widget_layout
        ).apply {
            val mileage = calculator.calculateCurrentTarget(ZonedDateTime.now())
            val nf = NumberFormat.getInstance(Locale.getDefault())
            setTextViewText(R.id.widgetMainText, nf.format(mileage.targetMileage))
        }
        appWidgetManager?.updateAppWidget(appWidgetId, views)
    }
}