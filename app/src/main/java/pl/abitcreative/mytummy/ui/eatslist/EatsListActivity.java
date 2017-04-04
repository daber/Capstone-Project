package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetaisActivity;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, EatsListFragment.EatsSelected {
  private static final int RC_PICK_PLACE = 1;
  private AddPlaceAsyncTask task;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_eats_list);

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

  private void pickPlace() {


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
    task = new AddPlaceAsyncTask();
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
    Intent i = new Intent(this, EatsDetaisActivity.class);
    i.putExtra(EatsDetaisActivity.EXTRA_EATS_PLACE, entry);
    startActivity(i);
  }
}
