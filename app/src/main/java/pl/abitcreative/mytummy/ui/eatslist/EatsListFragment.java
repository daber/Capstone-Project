package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

import javax.inject.Inject;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EatsEntry>> {
  private EatsAdapter         adapter;
  private LinearLayoutManager layoutManager;
  private List<EatsEntry>     data;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView((R.id.empty))
  ViewGroup    empty;
  @BindView((R.id.loading))
  ViewGroup    loading;
  private int selectedPositon = -1;

  private static final String SELECTED_POSITION = "SELECTED_POSITION";

  public interface EatsSelected {
    void onEatsSelected(int position, EatsEntry entry);

    void onEatsRemoved(int position, EatsEntry entry);
  }

  @Inject
  DateFormat dateFormat;

  private static final String           USER_ID     = "USER_ID";
  private static final int              EATS_LOADER = 1;
  private              FirebaseDatabase db          = FirebaseDatabase.getInstance();

  private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      int pos = viewHolder.getAdapterPosition();
      EatsEntry entry = data.get(pos);
      deleteEntry(entry);
      if (listener != null) {
        listener.onEatsRemoved(pos, entry);
      }
    }
  };

  private void deleteEntry(EatsEntry entry) {
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    db.getReference("/" + userId).child(entry.getId()).removeValue();

  }

  private ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);

  private EatsSelected listener;


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof EatsSelected) {
      listener = (EatsSelected) context;
    }
    BaseActivity baseActivity = (BaseActivity) getContext();
    baseActivity.activityComponent.inject(this);
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      selectedPositon = savedInstanceState.getInt(SELECTED_POSITION, -1);
    }
  }

  private Loader<List<EatsEntry>> eatsLoader;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_eats_list, container, false);
    ButterKnife.bind(this, v);
    layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layoutManager);
    touchHelper.attachToRecyclerView(recyclerView);
    ButterKnife.bind(this, v);
    showEmpty();
    return v;
  }

  private void showEmpty() {
    recyclerView.setVisibility(View.GONE);
    loading.setVisibility(View.GONE);
    empty.setVisibility(View.VISIBLE);
  }


  private void showLoading() {
    recyclerView.setVisibility(View.GONE);
    loading.setVisibility(View.VISIBLE);
    empty.setVisibility(View.GONE);
  }

  private void showContent() {
    recyclerView.setVisibility(View.VISIBLE);
    loading.setVisibility(View.GONE);
    empty.setVisibility(View.GONE);
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
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(SELECTED_POSITION, selectedPositon);
  }


  @Override
  public Loader<List<EatsEntry>> onCreateLoader(int id, Bundle args) {
    Loader<List<EatsEntry>> loader = new EatsListLoader(getContext(), args.getString(USER_ID));
    showLoading();
    return loader;
  }

  @Override
  public void onLoadFinished(Loader<List<EatsEntry>> loader, List<EatsEntry> data) {
    adapter = new EatsAdapter(data, listener, dateFormat);
    this.data = data;
    adapter.selectItemPos(selectedPositon);
    recyclerView.setAdapter(adapter);
    if (data == null || data.isEmpty()) {
      showEmpty();
    } else {
      showContent();
    }

  }

  @Override
  public void onLoaderReset(Loader<List<EatsEntry>> loader) {
    adapter = null;
    recyclerView.setAdapter(null);
  }

  public void selectPosition(int pos) {
    selectedPositon = pos;
    if (adapter != null) {
      adapter.selectItemPos(pos);
    }

  }
}
