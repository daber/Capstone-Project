package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.os.AsyncTask;
import android.os.ConditionVariable;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import pl.abitcreative.mytummy.model.EatsEntry;
import pl.abitcreative.mytummy.ui.widget.WidgetProvider;

import java.util.Date;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class AddPlaceAsyncTask extends AsyncTask<Place, Void, Boolean> implements OnSuccessListener<Void>, OnFailureListener {
  private final Context context;
  private FirebaseDatabase  db          = FirebaseDatabase.getInstance();
  private FirebaseUser      currentUser = FirebaseAuth.getInstance().getCurrentUser();
  private ConditionVariable condition   = new ConditionVariable(false);
  private Boolean           ret         = false;

  public AddPlaceAsyncTask(Context context) {
    this.context = context;
  }

  @Override
  protected Boolean doInBackground(Place... params) {
    Place place = params[0];
    if (currentUser == null) {
      return false;
    }
    EatsEntry entry = new EatsEntry();
    entry.setPlaceName(place.getName().toString());
    entry.setPlaceId(place.getId());
    entry.setTimeStamp(new Date().getTime());


    DatabaseReference pushed = db.getReference("/" + currentUser.getUid()).push();
    String key = pushed.getKey();
    entry.setId(key);
    pushed.setValue(entry).addOnSuccessListener(this).addOnFailureListener(this);
    condition.block();

    WidgetProvider.broadcastNewEntry(context);
    return ret;
  }


  @Override
  public void onSuccess(Void aVoid) {
    ret = true;
    condition.open();
  }

  @Override
  public void onFailure(@NonNull Exception e) {
    ret = false;
    condition.open();
  }
}
