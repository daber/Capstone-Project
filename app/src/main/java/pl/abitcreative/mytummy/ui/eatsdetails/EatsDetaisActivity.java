package pl.abitcreative.mytummy.ui.eatsdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 04/04/17.
 */

public class EatsDetaisActivity extends BaseActivity {
  public static final String    EXTRA_EATS_PLACE = "EATS_PLACE";
  private             EatsEntry eatsEntry        = null;
  private EatsDetailsFragment detailsFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eatsEntry = getIntent().getParcelableExtra(EXTRA_EATS_PLACE);
    setContentView(R.layout.activity_details);
    detailsFragment = (EatsDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
    detailsFragment.setEatsEntry(eatsEntry);
  }
}
