package pl.abitcreative.mytummy.ui.eatslist;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.abitcreative.mytummy.databinding.EatsListItemBinding;
import pl.abitcreative.mytummy.model.EatsEntry;

/**
 * Created by mdabrowski on 03/04/17.
 */

class EatsHolder extends RecyclerView.ViewHolder {
  EatsListItemBinding binding;
  public EatsEntry entry;

  public EatsHolder(EatsListItemBinding binding) {
    super(binding.getRoot());
    this.binding = binding;

  }
}
