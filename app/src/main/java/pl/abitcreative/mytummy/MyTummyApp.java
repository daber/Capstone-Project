package pl.abitcreative.mytummy;

import android.app.Application;

import com.google.android.libraries.places.api.Places;

import pl.abitcreative.mytummy.dependecy.AppComponent;
import pl.abitcreative.mytummy.dependecy.AppModule;
import pl.abitcreative.mytummy.dependecy.DaggerAppComponent;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class MyTummyApp extends Application {
  public AppComponent getAppComponent() {
    return appComponent;
  }

  protected AppComponent appComponent;

  @Override
  public void onCreate() {

    Places.initialize(this, BuildConfig.PLACES_API_KEY);
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    super.onCreate();
  }

}
