package com.plantalot.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.R;
import com.plantalot.components.OrtaggioSpecs;

import java.util.ArrayList;
import java.util.List;

// Riempe la card con le icone
public class OrtaggioSpecsAdapter extends RecyclerView.Adapter<OrtaggioSpecsAdapter.ViewHolder> {
	
	private final List<Pair<OrtaggioSpecs, OrtaggioSpecs>> mData = new ArrayList<>();
	
	public OrtaggioSpecsAdapter(@NonNull List<OrtaggioSpecs> data) {
		for (int i = 0; i < data.size(); i++) {
			if (i < data.size() - 1 && !data.get(i).isLarge() && !data.get(i + 1).isLarge()) {
				this.mData.add(new Pair<>(data.get(i), data.get(i + 1)));
				i++;
			} else {
				this.mData.add(new Pair<>(data.get(i), null));
			}
		}
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ortaggio_bl_specs_row, viewGroup, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		Pair<OrtaggioSpecs, OrtaggioSpecs> specs = mData.get(position);
		specs.first.bind(viewHolder.specsItem1);
		if (specs.second != null) {
			specs.second.bind(viewHolder.specsItem2);
		} else {
			viewHolder.specsItem2.setVisibility(View.GONE);
		}
		
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
		
		View specsItem1, specsItem2;
		
		ViewHolder(View view) {
			super(view);
			specsItem1 = view.findViewById(R.id.ortaggio_bl_specs_row_item1);
			specsItem2 = view.findViewById(R.id.ortaggio_bl_specs_row_item2);
		}
	}
	
}
