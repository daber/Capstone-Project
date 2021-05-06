package pl.abitcreative.mytummy.ui.eatsdetails;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.databinding.FragmentEatsDetailBinding;
import pl.abitcreative.mytummy.model.EatsEntry;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class EatsDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<EatsDetailsLoader.PlaceAndPicture>, GoogleApiClient.OnConnectionFailedListener {

  private static final int    PLACE_LOADER      = 1;
  private static final String EATS_ENTRY_BUNDLE = "EATS_ENTRY";


  FragmentEatsDetailBinding binding;

  @Inject
  GoogleApiClient client;

  private EatsEntry eatsEntry;
  @Inject
  java.text.DateFormat dateFormat;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    BaseActivity baseActivity = (BaseActivity) context;
    baseActivity.activityComponent.inject(this);


  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      EatsEntry entry = savedInstanceState.getParcelable(EATS_ENTRY_BUNDLE);
      setEatsEntry(entry);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(EATS_ENTRY_BUNDLE, eatsEntry);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = FragmentEatsDetailBinding.inflate(inflater);
    if (eatsEntry != null) {
      showContent();
    }

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    displayEats();
  }

  private void displayEats() {
    if (getView() == null) {

      return;
    }
    if (eatsEntry == null) {
      showEmpty();
      return;
    }
    binding.eatsTime.setText(dateFormat.format(new Date(eatsEntry.getTimeStamp())));
    binding.eatsName.setText(eatsEntry.getPlaceName());

    startEatsLoader();

  }

  public void setEatsEntry(EatsEntry eatsEntry) {
    this.eatsEntry = eatsEntry;
    displayEats();

  }

  private void startEatsLoader() {
    Bundle b = new Bundle();
    b.putParcelable(EATS_ENTRY_BUNDLE, eatsEntry);
    getLoaderManager().restartLoader(PLACE_LOADER, b, this);
  }

  @Override
  public Loader<EatsDetailsLoader.PlaceAndPicture> onCreateLoader(int id, Bundle args) {
    showLoading();
    EatsEntry entry = args.getParcelable(EATS_ENTRY_BUNDLE);
    return new EatsDetailsLoader(getContext(), client, entry);
  }


  @Override
  public void onLoadFinished(Loader<EatsDetailsLoader.PlaceAndPicture> loader, EatsDetailsLoader.PlaceAndPicture data) {
    if (data != null) {
      binding.eatsRating.setRating(data.place.getRating());
      showContent();
      revealImage(data);

    } else {
      showEmpty();
    }

  }

  private void revealImage(EatsDetailsLoader.PlaceAndPicture data) {
    binding.eatsImage.setVisibility(View.GONE);
    binding.eatsImage.setImageBitmap(data.bitmap);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

      int cx = binding.eatsImage.getWidth() / 2;
      int cy = binding.eatsImage.getHeight() / 2;

      float finalRadius = (float) Math.hypot(cx, cy);

      Animator anim = ViewAnimationUtils.createCircularReveal(binding.eatsImage, cx, cy, 0, finalRadius);
      binding.eatsImage.setVisibility(View.VISIBLE);
      anim.start();
    }else{
      binding.eatsImage.setVisibility(View.VISIBLE);
    }



  }

  @Override
  public void onLoaderReset(Loader<EatsDetailsLoader.PlaceAndPicture> loader) {
    showLoading();
  }

  private void showLoading() {
    binding.loading.setVisibility(View.VISIBLE);
    binding.empty.setVisibility(View.GONE);
    binding.content.setVisibility(View.GONE);
  }

  private void showEmpty() {
    binding.empty.setVisibility(View.VISIBLE);
    binding.loading.setVisibility(View.GONE);
    binding.content.setVisibility(View.GONE);
  }

  private void showContent() {
    binding.empty.setVisibility(View.GONE);
    binding.loading.setVisibility(View.GONE);
    binding.content.setVisibility(View.VISIBLE);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(getContext(), connectionResult.toString(), Toast.LENGTH_LONG).show();

  }
}
