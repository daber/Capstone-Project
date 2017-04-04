package pl.abitcreative.mytummy.dependecy;

import dagger.Component;

/**
 * Created by mdabrowski on 03/04/17.
 */

@Component(modules = {AppModule.class})
@AppScope

public interface AppComponent {

  ActivityComponent plus(ActivityModule module);

}
