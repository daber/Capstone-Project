package pl.abitcreative.mytummy.ui.eatsdetails;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.loader.content.AsyncTaskLoader;

import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class EatsDetailsLoader extends AsyncTaskLoader<EatsDetailsLoader.PlaceAndPicture> {

    public static class PlaceAndPicture {
        public Place place;
        public Bitmap bitmap;

        public void release() {
            bitmap.recycle();
        }
    }

    private PlaceAndPicture result;

    private final PlacesClient client;
    private EatsEntry eatsEntry;

    public EatsDetailsLoader(Context context, PlacesClient client, EatsEntry eatsEntry) {
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
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.RATING);
        FetchPlaceRequest request = FetchPlaceRequest.builder(eatsEntry.getPlaceId(), placeFields).build();
        PlaceAndPicture pAndP = null;
        try {
            FetchPlaceResponse response = Tasks.await(client.fetchPlace(request));

            if (response.getPlace() == null) {
                return null;
            }
            pAndP = new PlaceAndPicture();
            Place place = response.getPlace();
            pAndP.place = place;
            if (place.getPhotoMetadatas().size() > 0) {
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();
                FetchPhotoResponse photoResponse = Tasks.await(client.fetchPhoto(photoRequest));
                Bitmap bitmap = photoResponse.getBitmap();


                pAndP.place = place;
                pAndP.bitmap = bitmap;

            }
        } catch (ExecutionException | InterruptedException e) {
            //ignore
        }
        return pAndP;
    }


    @Override
    protected void onReset() {
        super.onReset();
        if (result != null) {
            result.release();
            result = null;
        }
    }
}
