package pl.abitcreative.mytummy.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import pl.abitcreative.mytummy.model.EatsEntry;

import java.util.Iterator;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class WidgetService extends IntentService {

  FirebaseAuth     auth = FirebaseAuth.getInstance();
  FirebaseDatabase db   = FirebaseDatabase.getInstance();


  public WidgetService() {
    super("WidgetService");
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
          sendBroadcastWithLastMeal("LastEat at the " + entry.getPlaceName(), ids);
        }

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


  }

  private void sendBroadcastWithLastMeal(String text, int[] ids) {
    Intent i = new Intent(WidgetProvider.REFRESH_WIDGET_INTIENT);
    i.putExtra(WidgetProvider.REFRESH_WIDGET_LAST_MEAL_EXTRA, text);
    i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

    sendBroadcast(i);
  }
}
