package pl.abitcreative.mytummy.ui.eatslist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.abitcreative.mytummy.R;

/**
 * Created by mdabrowski on 03/04/17.
 */

class EatsHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.eats_name)
  TextView eatsName;

  public EatsHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
