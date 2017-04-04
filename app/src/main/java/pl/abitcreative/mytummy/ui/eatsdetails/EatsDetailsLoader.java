package pl.abitcreative.mytummy.ui.eatsdetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.*;
import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class EatsDetailsLoader extends AsyncTaskLoader<EatsDetailsLoader.PlaceAndPicture> {

  public static class PlaceAndPicture {
    public  Place                    place;
    public  Bitmap                   bitmap;
    private PlacePhotoMetadataBuffer photoBuffer;
    private PlaceBuffer              placeBuffer;

    public void release() {
      photoBuffer.release();
      placeBuffer.release();
    }
  }

  private final GoogleApiClient client;
  private       EatsEntry       eatsEntry;

  public EatsDetailsLoader(Context context, GoogleApiClient client, EatsEntry eatsEntry) {
    super(context);
    this.eatsEntry = eatsEntry;
    this.client = client;
  }

  @Override
  protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }

  @Override
  public PlaceAndPicture loadInBackground() {
    PlaceBuffer buffer = Places.GeoDataApi.getPlaceById(client, eatsEntry.getPlaceId()).await();

    if (buffer.getCount() > 0) {
      PlaceAndPicture placeAndPicture = new PlaceAndPicture();
      Place place = buffer.get(0);
      Bitmap b = null;
      PlacePhotoMetadataResult photoResutlt = Places.GeoDataApi.getPlacePhotos(client, eatsEntry.getPlaceId()).await();
      placeAndPicture.placeBuffer = buffer;


      if (photoResutlt.getStatus().isSuccess()) {
        PlacePhotoMetadataBuffer photoMeta = photoResutlt.getPhotoMetadata();
        placeAndPicture.photoBuffer = photoMeta;
        if (photoMeta.getCount() > 0) {
          PlacePhotoMetadata photo = photoMeta.get(0);
          b = photo.getPhoto(client).await().getBitmap();
        }

      }

      placeAndPicture.place = place;
      placeAndPicture.bitmap = b;
      return placeAndPicture;

    }


    return null;
  }


}
