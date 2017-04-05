package pl.abitcreative.mytummy.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.ui.login.LoginActivity;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class WidgetProvider extends AppWidgetProvider {
  public static final String REFRESH_WIDGET_INTIENT               = "pl.abitcreative.mytummy.widget.REFRESH_WIDGET";
  public static final String REFRESH_WIDGET_LAST_MEAL_TIME_EXTRA  = "pl.abitcreative.mytummy.widget.LAST_MEAL_TIME";
  public static final String REFRESH_WIDGET_LAST_MEAL_PLACE_EXTRA = "pl.abitcreative.mytummy.widget.LAST_MEAL_PLACE";


  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(REFRESH_WIDGET_INTIENT)) {
      int[] array = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
      AppWidgetManager manager = AppWidgetManager.getInstance(context);
      String place = intent.getStringExtra(REFRESH_WIDGET_LAST_MEAL_PLACE_EXTRA);
      String time = intent.getStringExtra(REFRESH_WIDGET_LAST_MEAL_TIME_EXTRA);
      onDataReceived(context, manager, array, place, time);

    }

    super.onReceive(context, intent);
  }

  private void onDataReceived(Context context, AppWidgetManager manager, int[] array, String place, String time) {
    for (int i : array) {
      RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
      Intent intent = new Intent(context, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
      rv.setOnClickPendingIntent(R.id.container, pi);
      if (place != null) {
        rv.setViewVisibility(R.id.not_logged_in, View.GONE);
        rv.setViewVisibility(R.id.logged_in, View.VISIBLE);
        rv.setTextViewText(R.id.eats_name, place);
        rv.setTextViewText(R.id.eats_time, time);
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

  public static void broadcastNewEntry(Context context) {
    Intent intent = new Intent(context, WidgetProvider.class);
    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
    int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    context.sendBroadcast(intent);
  }
}
