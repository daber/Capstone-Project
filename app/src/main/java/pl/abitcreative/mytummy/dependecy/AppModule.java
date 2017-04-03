package pl.abitcreative.mytummy.dependecy;

import dagger.Module;
import dagger.Provides;
import pl.abitcreative.mytummy.MyTummyApp;

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
}
