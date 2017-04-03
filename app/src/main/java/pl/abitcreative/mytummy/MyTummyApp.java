package pl.abitcreative.mytummy;

import android.app.Application;
import pl.abitcreative.mytummy.dependecy.AppComponent;
import pl.abitcreative.mytummy.dependecy.AppModule;
import pl.abitcreative.mytummy.dependecy.DaggerAppComponent;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class MyTummyApp extends Application {
  protected AppComponent appComponent;

  @Override
  public void onCreate() {
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    super.onCreate();
  }
}
