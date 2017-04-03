package pl.abitcreative.mytummy.ui.eatslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.model.EatsEntry;

import java.util.List;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsAdapter extends RecyclerView.Adapter<EatsHolder> {

  private List<EatsEntry> entries;

  public EatsAdapter(List<EatsEntry> eatsEntries) {
    this.entries = eatsEntries;
  }

  @Override
  public EatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v = inflater.inflate(R.layout.eats_list_item, parent, false);
    return new EatsHolder(v);
  }

  @Override
  public void onBindViewHolder(EatsHolder holder, int position) {
    holder.eatsName.setText(entries.get(position).getPlaceName());

  }

  @Override
  public int getItemCount() {
    return entries.size();
  }
}
