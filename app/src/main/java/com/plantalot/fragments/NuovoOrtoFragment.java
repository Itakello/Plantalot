package com.plantalot.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plantalot.components.NuovoOrtoNumberSelector;
import com.plantalot.R;
import com.plantalot.adapters.NuovoOrtoOptionsAdapter;
import com.plantalot.classes.Orto;
import com.plantalot.components.OrtoView;
import com.plantalot.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Triple;


public class NuovoOrtoFragment extends Fragment {
	
	private List<Triple<String, String, View>> options;
	private Orto orto;
	private View view;
	private Context context;
	private TableLayout table;
	private int tableW, tableH;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getContext();
		orto = new Orto(context);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.nuovo_orto_fragment, container, false);
		
		table = view.findViewById(R.id.nuovo_orto_table);
		View tableFrame = view.findViewById(R.id.nuovo_orto_table_frame);
		tableFrame.post(() -> {
			tableW = tableFrame.getWidth() - Utils.dp2px(24, context);   // FIXME margin
			tableH = tableFrame.getHeight() - Utils.dp2px(12, context);  // FIXME margin
			updateTable();
		});
		
		setupOptions();
		RecyclerView optionsRecycler = view.findViewById(R.id.nuovo_orto_options_recycler);
		optionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
		NuovoOrtoOptionsAdapter optionsAdapter = new NuovoOrtoOptionsAdapter(getActivity(), options, orto, view);
		optionsRecycler.setAdapter(optionsAdapter);
		
		com.plantalot.utils.DividerItemDecoration dividerItemDecoration = new com.plantalot.utils.DividerItemDecoration(optionsRecycler.getContext(), RecyclerView.VERTICAL, false);
		optionsRecycler.addItemDecoration(dividerItemDecoration);
		
		Button save_btn = view.findViewById(R.id.save_orto);
		save_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_goto_home));
		
		return view;
	}
	
	public void updateTable() {
		int ortoW = orto.getAiuoleDim().x * orto.getAiuoleCount().x;
		int ortoH = orto.getAiuoleDim().y * orto.getAiuoleCount().y;
		int aiuolaW;
		int aiuolaH;
		
		if ((double) ortoW / (double) ortoH > (double) tableW / (double) tableH) {
			aiuolaW = tableW / orto.getAiuoleCount().x;
			aiuolaH = aiuolaW * orto.getAiuoleDim().y / orto.getAiuoleDim().x;
		} else {
			aiuolaH = tableH / orto.getAiuoleCount().y;
			aiuolaW = aiuolaH * orto.getAiuoleDim().x / orto.getAiuoleDim().y;
		}
		
		table.removeAllViews();
		for (int i = 0; i < orto.getAiuoleCount().y; i++) {
			TableRow row = new TableRow(context);
			for (int j = 0; j < orto.getAiuoleCount().x; j++) {
				row.addView(new OrtoView(context, aiuolaW, aiuolaH));
			}
			table.addView(row);
		}
	}
	
	private void setupOptions() {
		Resources res = context.getResources();
		options = new ArrayList<>(Arrays.asList(
				new Triple<>(
						res.getString(R.string.nome_orto),
						orto.getNome(),
						null),
				new Triple<>(
						res.getString(R.string.dimensioni),
						orto.getAiuoleDim().toString(),
						new NuovoOrtoNumberSelector(
								this, context, orto.getAiuoleDim(), 20, 1000,
								R.drawable.ic_iconify_sprout_24, R.drawable.ic_round_casino_24)),
				new Triple<>(
						res.getString(R.string.numero_aiuole),
						orto.getAiuoleCount().toString(),
						new NuovoOrtoNumberSelector(
								this, context, orto.getAiuoleCount(), 1, 8,
								R.drawable.ic_iconify_sprout_24, R.drawable.ic_round_casino_24)),
				new Triple<>(
						res.getString(R.string.esposizione),
						orto.getEsposizione().toString(),
						null),
				new Triple<>(
						res.getString(R.string.orientamento),
						orto.getOrientamento().toString(),
						null)
		));
	}
	
}
