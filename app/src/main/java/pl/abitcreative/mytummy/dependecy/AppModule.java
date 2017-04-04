package pl.abitcreative.mytummy.dependecy;

import dagger.Module;
import dagger.Provides;
import pl.abitcreative.mytummy.MyTummyApp;

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
  public DateFormat provideDateFormat() {
    return new SimpleDateFormat(" dd MMM HH:mm", Locale.getDefault());
  }
}
