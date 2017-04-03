package pl.abitcreative.mytummy.ui.eatslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import pl.abitcreative.mytummy.R;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsListActivity extends FragmentActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_eats_list);
  }
}
