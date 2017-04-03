package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.os.ConditionVariable;
import android.support.v4.content.AsyncTaskLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import pl.abitcreative.mytummy.model.EatsEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListLoader extends AsyncTaskLoader<List<EatsEntry>> {

  private FirebaseDatabase db;
  private String           uuid;

  public EatsListLoader(Context context, String uuid) {
    super(context);
    this.uuid = uuid;
    db = FirebaseDatabase.getInstance();

  }

  @Override
  protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }

  @Override
  public List<EatsEntry> loadInBackground() {

    final ConditionVariable var = new ConditionVariable(false);
    final List<EatsEntry> ret = new ArrayList<>();

    String path = "/" + uuid;
    db.getReference(path).orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot c : dataSnapshot.getChildren()) {
          EatsEntry e = c.getValue(EatsEntry.class);
          ret.add(e);
        }
        var.open();
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        var.open();
      }
    });
    var.block();

    return ret;

  }
}
