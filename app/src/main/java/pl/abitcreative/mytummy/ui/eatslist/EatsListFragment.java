package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.MyTummyApp;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EatsEntry>> {
  public interface EatsSelected {
    void onEatsSelected(int position, EatsEntry entry);
  }

  @Inject
  DateFormat dateFormat;

  private static final String           USER_ID     = "USER_ID";
  private static final int              EATS_LOADER = 1;
  private              FirebaseDatabase db          = FirebaseDatabase.getInstance();

  private EatsSelected listener;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof EatsSelected) {
      listener = (EatsSelected) context;
    }
    BaseActivity baseActivity = (BaseActivity) getContext();
    baseActivity.activityComponent.inject(this);
  }

  private Loader<List<EatsEntry>> eatsLoader;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_eats_list, container, false);
    ButterKnife.bind(this, v);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    return v;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  @Override
  public void onResume() {
    super.onResume();
    initLoader();
  }

  private void initLoader() {
    Bundle b = new Bundle();
    b.putString(USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
    getLoaderManager().initLoader(EATS_LOADER, b, this);
  }

  @Override
  public Loader<List<EatsEntry>> onCreateLoader(int id, Bundle args) {
    Loader<List<EatsEntry>> loader = new EatsListLoader(getContext(), args.getString(USER_ID));
    return loader;
  }

  @Override
  public void onLoadFinished(Loader<List<EatsEntry>> loader, List<EatsEntry> data) {
    recyclerView.setAdapter(new EatsAdapter(data, listener, dateFormat));

  }

  @Override
  public void onLoaderReset(Loader<List<EatsEntry>> loader) {
    recyclerView.setAdapter(null);

  }
}
