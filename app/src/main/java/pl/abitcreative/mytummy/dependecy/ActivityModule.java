package pl.abitcreative.mytummy.dependecy;

import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import dagger.Module;
import dagger.Provides;
import pl.abitcreative.mytummy.R;

/**
 * Created by mdabrowski on 04/04/17.
 */
@Module
public class ActivityModule {
  private final GoogleApiClient.OnConnectionFailedListener googleConnectionListener;
  private       AppCompatActivity                          supportActivity;

  public ActivityModule(AppCompatActivity activity,GoogleApiClient.OnConnectionFailedListener listener) {
    supportActivity = activity;
    googleConnectionListener = listener;
  }


  @Provides
  @ActivityScope
  public GoogleSignInOptions provideGoogleSignInOptions() {
    return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().requestIdToken(supportActivity.getString(R.string.default_web_client_id))
        .build();

  }

  @Provides
  @ActivityScope
  public GoogleApiClient provideGoogleApi(GoogleSignInOptions gso) {
    return new GoogleApiClient.Builder(supportActivity)
        .addApi(Places.GEO_DATA_API)
        .enableAutoManage(supportActivity, googleConnectionListener)
        .addApi(Places.PLACE_DETECTION_API)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

}
