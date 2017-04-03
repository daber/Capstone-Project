package pl.abitcreative.mytummy.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import pl.abitcreative.mytummy.R;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class WidgetProvider extends AppWidgetProvider {
  public static final String REFRESH_WIDGET_INTIENT         = "pl.abitcreative.mytummy.widget.REFRESH_WIDGET";
  public static final String REFRESH_WIDGET_LAST_MEAL_EXTRA = "pl.abitcreative.mytummy.widget.LAST_MEAL";


  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(REFRESH_WIDGET_INTIENT)) {
      int[] array = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
      AppWidgetManager manager = AppWidgetManager.getInstance(context);
      String text = intent.getStringExtra(REFRESH_WIDGET_LAST_MEAL_EXTRA);
      onDataReceived(context, manager, array, text);

    }

    super.onReceive(context, intent);
  }

  private void onDataReceived(Context context, AppWidgetManager manager, int[] array, String text) {
    for (int i : array) {
      RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
      if (text != null) {
        rv.setViewVisibility(R.id.not_logged_in, View.GONE);
        rv.setViewVisibility(R.id.logged_in, View.VISIBLE);
        rv.setTextViewText(R.id.last_meal, text);
        manager.updateAppWidget(i, rv);
      }
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);

    launchIntentServiceToRefresh(context, appWidgetIds);
  }

  private void launchIntentServiceToRefresh(Context context, int[] appWidgetsIds) {
    Intent i = new Intent(context, WidgetService.class);
    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetsIds);
    context.startService(i);
  }
}
