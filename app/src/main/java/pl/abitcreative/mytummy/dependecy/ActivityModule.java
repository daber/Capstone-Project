package pl.abitcreative.mytummy.dependecy;

import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
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




}
