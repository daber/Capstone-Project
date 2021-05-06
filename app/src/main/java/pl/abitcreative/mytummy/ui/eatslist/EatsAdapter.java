package pl.abitcreative.mytummy.ui.eatslist;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.databinding.EatsListItemBinding;
import pl.abitcreative.mytummy.model.EatsEntry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mdabrowski on 03/04/17.
 */

public class EatsAdapter extends RecyclerView.Adapter<EatsHolder> {
  public static final int NO_SELECTION = -1;

  private int                           selectedItem = NO_SELECTION;
  private EatsListFragment.EatsSelected listener     = null;
  private DateFormat dateFormat;

  private List<EatsEntry> entries;
  private Context         context;
  private ColorDrawable   selectionColor;
  private ColorDrawable   neutralColor;
  private EatsListItemBinding binding;

  public EatsAdapter(List<EatsEntry> eatsEntries, EatsListFragment.EatsSelected listener, DateFormat dateFormat) {
    this.dateFormat = dateFormat;
    this.entries = eatsEntries;
    this.listener = listener;
  }

  @Override
  public EatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    binding = EatsListItemBinding.inflate(inflater,parent,false);
    selectionColor = new ColorDrawable(context.getResources().getColor(R.color.colorPrimary));
    selectionColor.setAlpha(255 / 2);
    neutralColor = new ColorDrawable(context.getResources().getColor(android.R.color.white));
    return new EatsHolder(binding);
  }

  @Override
  public void onBindViewHolder(EatsHolder holder, final int position) {

    EatsEntry entry = entries.get(position);
    if (selectedItem == position) {
      holder.binding.container.setBackground(selectionColor);
    } else {
      holder.binding.container.setBackground(neutralColor);
    }

    holder.binding.eatsName.setText(entry.getPlaceName());
    holder.binding.eatsTime.setText(dateFormat.format(new Date(entry.getTimeStamp())));
    holder.binding.eatsName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onEatsSelected(position, entries.get(position));
        }
      }
    });
  }

  public void setEatsListener(EatsListFragment.EatsSelected eatsListener) {
    listener = eatsListener;
  }

  public void selectItemPos(int position) {
    if (selectedItem != NO_SELECTION) {
      notifyItemChanged(selectedItem);
    }
    selectedItem = position;
    if (position != NO_SELECTION) {
      notifyItemChanged(selectedItem);
    }
  }

  public EatsEntry getDataAtPosition(int pos) {
    if (pos < entries.size() && pos >= 0) {
      return entries.get(pos);
    }
    return null;
  }

  public int getSelectedPosition() {
    return selectedItem;
  }


  @Override
  public int getItemCount() {
    return entries.size();
  }
}
