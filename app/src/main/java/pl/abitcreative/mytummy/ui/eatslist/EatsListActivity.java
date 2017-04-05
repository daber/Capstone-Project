package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetailsFragment;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetaisActivity;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, EatsListFragment.EatsSelected {
  private static final int    RC_PICK_PLACE    = 1;

  private AddPlaceAsyncTask task;
  private boolean isBigScreen = false;
  private EatsDetailsFragment detailsFragment;
  private EatsListFragment    listFragment;

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  private int selectedPosition;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_eats_list);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

  }

  @Override
  protected void onResumeFragments() {
    super.onResumeFragments();
    detectBigScreen();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.add_restaurant) {
      pickPlace();
      return true;
    }
    return false;
  }

  @OnClick(R.id.fab)
  protected void pickPlace() {


    try {
      Intent intent = new PlacePicker.IntentBuilder()
          .build(this);
      startActivityForResult(intent, RC_PICK_PLACE);
    } catch (GooglePlayServicesRepairableException e) {
      e.printStackTrace();
    } catch (GooglePlayServicesNotAvailableException e) {
      e.printStackTrace();
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RC_PICK_PLACE) {
      if (resultCode == RESULT_OK) {
        Place place = PlacePicker.getPlace(data, this);
        String toastMsg = String.format("Place: %s", place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        addEntry(place);
      }
    }
  }

  private void addEntry(Place place) {
    cancelTask();
    task = new AddPlaceAsyncTask(getApplicationContext());
    task.execute(place);

  }

  @Override
  protected void onStop() {
    super.onStop();
    cancelTask();
  }

  private void cancelTask() {
    if (task != null) {
      task.cancel(true);
    }
  }


  @Override
  public void onEatsSelected(int position, EatsEntry entry) {
    selectedPosition = position;
    if (isBigScreen) {
      detailsFragment.setEatsEntry(entry);
      listFragment.selectPosition(position);


    } else {
      launchDetails(entry);
    }
  }

  @Override
  public void onEatsRemoved(int position, EatsEntry entry) {
    if(selectedPosition == position){
      onEatsSelected(-1,null);
    }
  }

  private void launchDetails(EatsEntry entry) {
    Intent i = new Intent(this, EatsDetaisActivity.class);
    i.putExtra(EatsDetaisActivity.EXTRA_EATS_PLACE, entry);
    startActivity(i);
  }

  private void detectBigScreen() {
    detailsFragment = (EatsDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.eats_details_fragment);
    listFragment = (EatsListFragment) getSupportFragmentManager().findFragmentById(R.id.eats_list_fragment);
    if (detailsFragment != null) {
      isBigScreen = true;
    } else {
      isBigScreen = false;
    }
  }
}
