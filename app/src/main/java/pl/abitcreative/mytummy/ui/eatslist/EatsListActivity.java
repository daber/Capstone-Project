package pl.abitcreative.mytummy.ui.eatslist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.databinding.ActivityEatsListBinding;
import pl.abitcreative.mytummy.model.EatsEntry;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetailsFragment;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetaisActivity;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, EatsListFragment.EatsSelected {
    private static final int RC_PICK_PLACE = 1;
    private static final int REQ_LOCATION = 2;

    private AddPlaceAsyncTask task;
    private boolean isBigScreen = false;
    private EatsDetailsFragment detailsFragment;
    private EatsListFragment listFragment;
    private ActivityEatsListBinding binding;
    private int selectedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEatsListBinding.inflate(LayoutInflater.from(this));
        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPlace();
            }
        });
        setContentView(binding.getRoot());
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

    //  @OnClick(R.id.fab)
    protected void pickPlace() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            preparePickIntent();

        } else {
            // You can directly ask for the permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQ_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void preparePickIntent() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        RectangularBounds bounds = null;
        if (bestProvider != null) {
            Location l = locationManager.getLastKnownLocation(bestProvider);
            if(l != null) {
                double lat = l.getLatitude();
                double lon = l.getLongitude();
                // around 6 km
                double eps = 0.05;
                LatLngBounds llBounds = LatLngBounds.builder().include(new LatLng(lat - eps, lon - eps)).include(new LatLng(lat + eps, lon + eps)).build();
                bounds = RectangularBounds.newInstance(llBounds);
            }
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.RATING);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setLocationRestriction(bounds)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, RC_PICK_PLACE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PICK_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                addEntry(place);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
        if (selectedPosition == position) {
            onEatsSelected(-1, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPlace();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
