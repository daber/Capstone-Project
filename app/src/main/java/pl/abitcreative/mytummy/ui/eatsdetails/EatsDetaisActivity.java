package pl.abitcreative.mytummy.ui.eatsdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
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
  @BindView(R.id.toolbar)
  Toolbar             toolbar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eatsEntry = getIntent().getParcelableExtra(EXTRA_EATS_PLACE);
    setContentView(R.layout.activity_details);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    detailsFragment = new EatsDetailsFragment();
    detailsFragment.setEatsEntry(eatsEntry);
    getSupportFragmentManager().beginTransaction().add(R.id.details_fragment,detailsFragment).commit();
  }
}
