package pl.abitcreative.mytummy.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class WidgetService extends IntentService {


  public WidgetService() {
    super("WidgetService");
  }


  @Override
  protected void onHandleIntent(@Nullable Intent received) {
    int[] ids = received.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    Intent i = new Intent(WidgetProvider.REFRESH_WIDGET_INTIENT);
    i.putExtra(WidgetProvider.REFRESH_WIDGET_LAST_MEAL_EXTRA, "LAAAAAST MEAL");
    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

    sendBroadcast(i);
  }
}
