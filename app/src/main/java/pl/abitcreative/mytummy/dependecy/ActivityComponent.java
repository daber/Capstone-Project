package pl.abitcreative.mytummy.dependecy;

import dagger.Subcomponent;
import pl.abitcreative.mytummy.ui.eatsdetails.EatsDetailsFragment;
import pl.abitcreative.mytummy.ui.eatslist.EatsListFragment;
import pl.abitcreative.mytummy.ui.login.LoginFragment;

/**
 * Created by mdabrowski on 04/04/17.
 */

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {


  void inject(LoginFragment loginFragment);

  void inject(EatsDetailsFragment eatsDetailsFragment);

  void inject(EatsListFragment eatsListFragment);
}
