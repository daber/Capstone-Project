package pl.abitcreative.mytummy.dependecy;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import dagger.Module;

/**
 * Created by mdabrowski on 04/04/17.
 */
@Module
public class ActivityModule {
  private final GoogleApiClient.OnConnectionFailedListener googleConnectionListener;
  private AppCompatActivity supportActivity;

  public ActivityModule(AppCompatActivity activity,GoogleApiClient.OnConnectionFailedListener listener) {
    supportActivity = activity;
    googleConnectionListener = listener;
  }




}
