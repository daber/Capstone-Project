package pl.abitcreative.mytummy.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import pl.abitcreative.mytummy.MyTummyApp;
import pl.abitcreative.mytummy.model.EatsEntry;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.Iterator;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class WidgetService extends IntentService {

  FirebaseAuth     auth = FirebaseAuth.getInstance();
  FirebaseDatabase db   = FirebaseDatabase.getInstance();

  @Inject
  DateFormat dateFormat;

  public WidgetService() {
    super("WidgetService");
  }


  @Override
  public void onCreate() {
    super.onCreate();
    MyTummyApp app = (MyTummyApp) getApplicationContext();
    app.getAppComponent().inject(this);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent received) {
    final int[] ids = received.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
    if (auth.getCurrentUser() == null) {
      return;
    }
    String path = "/" + auth.getCurrentUser().getUid();
    DatabaseReference ref = db.getReference(path);

    ref.orderByChild("timeStamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        if (iterator.hasNext()) {
          EatsEntry entry = iterator.next().getValue(EatsEntry.class);
          sendBroadcastWithLastMeal(entry.getPlaceName(), entry.getTimeStamp(), ids);
        }

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


  }

  private void sendBroadcastWithLastMeal(String place, long timestamp, int[] ids) {
    Intent i = new Intent(WidgetProvider.REFRESH_WIDGET_INTIENT);
    i.putExtra(WidgetProvider.REFRESH_WIDGET_LAST_MEAL_TIME_EXTRA, dateFormat.format(timestamp));
    i.putExtra(WidgetProvider.REFRESH_WIDGET_LAST_MEAL_PLACE_EXTRA, place);
    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

    sendBroadcast(i);
  }
}
