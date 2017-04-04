package pl.abitcreative.mytummy.ui.eatslist;

import android.os.AsyncTask;
import android.os.ConditionVariable;
import android.support.annotation.NonNull;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import pl.abitcreative.mytummy.model.EatsEntry;

import java.util.Date;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class AddPlaceAsyncTask extends AsyncTask<Place, Void, Boolean> implements OnSuccessListener<Void>, OnFailureListener {
  private FirebaseDatabase  db          = FirebaseDatabase.getInstance();
  private FirebaseUser      currentUser = FirebaseAuth.getInstance().getCurrentUser();
  private ConditionVariable condition   = new ConditionVariable(false);
  private Boolean           ret         = false;

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


    db.getReference("/" + currentUser.getUid()).push().setValue(entry).addOnSuccessListener(this).addOnFailureListener(this);
    condition.block();
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
