package pl.abitcreative.mytummy.ui.eatslist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 03/04/17.
 */

class EatsHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.eats_name)
  TextView eatsName;
  @BindView(R.id.eats_time)
  TextView datetime;

  @BindView(R.id.container)
  LinearLayout container;
  public EatsEntry entry;

  public EatsHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
