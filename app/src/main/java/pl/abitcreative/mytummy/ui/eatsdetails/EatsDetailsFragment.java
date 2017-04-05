package pl.abitcreative.mytummy.ui.eatsdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class EatsDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<EatsDetailsLoader.PlaceAndPicture>, GoogleApiClient.OnConnectionFailedListener {

  private static final int    PLACE_LOADER      = 1;
  private static final String EATS_ENTRY_BUNDLE = "EATS_ENTRY";

  @BindView(R.id.eats_time)
  TextView eatsTime;
  @BindView(R.id.eats_name)
  TextView eatsName;

  @BindView(R.id.eats_rating)
  RatingBar eatsRating;

  @BindView(R.id.eats_image)
  ImageView eatsImage;

  @BindView(R.id.content)
  ViewGroup content;
  @BindView(R.id.empty)
  ViewGroup empty;

  @BindView(R.id.loading)
  ViewGroup loading;


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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_eats_detail, container, false);
    ButterKnife.bind(this, v);
    if (eatsEntry != null) {
      showContent();
    }

    return v;
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
    eatsTime.setText(dateFormat.format(new Date(eatsEntry.getTimeStamp())));
    eatsName.setText(eatsEntry.getPlaceName());

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
    EatsEntry entry = args.getParcelable(EATS_ENTRY_BUNDLE);
    return new EatsDetailsLoader(getContext(), client, entry);
  }


  @Override
  public void onLoadFinished(Loader<EatsDetailsLoader.PlaceAndPicture> loader, EatsDetailsLoader.PlaceAndPicture data) {
    if (data != null) {
      eatsRating.setRating(data.place.getRating());
      eatsImage.setImageBitmap(data.bitmap);
      showContent();
    } else {
      showEmpty();
    }

  }

  @Override
  public void onLoaderReset(Loader<EatsDetailsLoader.PlaceAndPicture> loader) {
    showLoading();
  }

  private void showLoading() {
    loading.setVisibility(View.VISIBLE);
    empty.setVisibility(View.GONE);
    content.setVisibility(View.GONE);
  }

  private void showEmpty() {
    empty.setVisibility(View.VISIBLE);
    loading.setVisibility(View.GONE);
    content.setVisibility(View.GONE);
  }

  private void showContent() {
    empty.setVisibility(View.GONE);
    loading.setVisibility(View.GONE);
    content.setVisibility(View.VISIBLE);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(getContext(), connectionResult.toString(), Toast.LENGTH_LONG).show();

  }
}
