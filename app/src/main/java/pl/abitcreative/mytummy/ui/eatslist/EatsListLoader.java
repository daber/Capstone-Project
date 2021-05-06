package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.os.ConditionVariable;

import androidx.loader.content.AsyncTaskLoader;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListLoader extends AsyncTaskLoader<List<EatsEntry>> implements ChildEventListener {

    private final String path;
    private final DatabaseReference reference;
    private FirebaseDatabase db;
    private String uuid;

    public EatsListLoader(Context context, String uuid) {
        super(context);
        this.uuid = uuid;
        db = FirebaseDatabase.getInstance();
        path = "/" + uuid;
        reference = db.getReference(path);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
        reference.addChildEventListener(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        reference.removeEventListener(this);
    }

    @Override
    public List<EatsEntry> loadInBackground() {

        final ConditionVariable var = new ConditionVariable(false);
        final List<EatsEntry> ret = new ArrayList<>();


        db.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    EatsEntry e = c.getValue(EatsEntry.class);
                    ret.add(0, e);
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        forceLoad();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        forceLoad();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        forceLoad();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        forceLoad();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
