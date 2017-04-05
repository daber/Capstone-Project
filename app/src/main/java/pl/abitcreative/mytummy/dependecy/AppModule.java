package pl.abitcreative.mytummy.dependecy;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import dagger.Module;
import dagger.Provides;
import pl.abitcreative.mytummy.MyTummyApp;
import pl.abitcreative.mytummy.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mdabrowski on 03/04/17.
 */

@Module
public class AppModule {

  private MyTummyApp app;

  public AppModule(MyTummyApp app) {
    this.app = app;
  }

  @Provides
  @AppScope
  public MyTummyApp providesApplication() {
    return app;
  }

  @Provides
  @AppScope
  public GoogleApiClient provideGoogleApi(GoogleSignInOptions gso) {
    GoogleApiClient client = new GoogleApiClient.Builder(app)
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
    client.connect();
    return client;
  }

  @Provides
  @AppScope
  public GoogleSignInOptions provideGoogleSignInOptions() {
    return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().requestIdToken(app.getString(R.string.default_web_client_id))
        .build();

  }


  @Provides
  @AppScope
  public DateFormat provideDateFormat() {
    return new SimpleDateFormat(" dd MMM HH:mm", Locale.getDefault());
  }
}
