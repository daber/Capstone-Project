package pl.abitcreative.mytummy;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import pl.abitcreative.mytummy.dependecy.ActivityComponent;
import pl.abitcreative.mytummy.dependecy.ActivityModule;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

  public ActivityComponent activityComponent;

  protected void onCreate(@Nullable Bundle savedInstanceState) {
    MyTummyApp app = (MyTummyApp) getApplicationContext();
    activityComponent = app.getAppComponent().plus(new ActivityModule(this, this));
    super.onCreate(savedInstanceState);

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(this, R.string.failed_to_connect_to_google_services, Toast.LENGTH_LONG).show();
  }
}
