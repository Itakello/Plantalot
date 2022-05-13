package com.plantalot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.plantalot.R;
import com.plantalot.utils.Utils;

import java.util.Arrays;
import java.util.List;


public class OrtaggioFragment extends Fragment {
	
	private long dropdownDismissTime = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ortaggio_fragment, container, false);
		setupToolbar(view);
		setupDropdown(view);
		setupContent(view);
		return view;
	}
	
	private void setupToolbar(@NonNull View view) {
		MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
		AppCompatActivity activity = (AppCompatActivity) getActivity();
		
		if (activity != null) {
			activity.setSupportActionBar(toolbar);
		}
		
		final ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

//		toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
//				getContext(),
//				view.findViewById(R.id.home_backdrop_frontlayer),
//				new AccelerateDecelerateInterpolator(),
//				R.drawable.ic_round_menu_24,
//				R.drawable.ic_round_close_24,
//				drawer.getMeasuredHeight()));
	}
	
	private void setupContent(@NonNull View view) {
//		TextView description = view.findViewById(R.id.ortaggio_bl_description);
//        Utils.makeTextViewResizable(description, 3, true);
//		Utils.makeTextViewResizable(description, 3, true);
		
		ExpandableTextView expTv1 = view.findViewById(R.id.expand_text_view);
		expTv1.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris convallis nisi sed mi cursus maximus eget ac enim. Aenean a sodales lectus. Aenean erat ex, luctus a feugiat et, bibendum nec nibh. Nullam eget risus leo. Nulla facilisi. Proin iaculis consectetur elit et tempor. Pellentesque lacus metus, pulvinar et viverra eget, lobortis nec dui. In euismod eu magna facilisis suscipit. Sed ut imperdiet diam. Integer ut neque turpis. Aenean tortor mauris, convallis sed pellentesque at, interdum vel odio. Vivamus nec mollis nisl. Nulla eleifend congue venenatis. Etiam eget ex pulvinar, iaculis diam a, hendrerit velit. Morbi aliquet id mauris dapibus fermentum. Pellentesque pretium finibus blandit. Vivamus ut orci in lacus elementum suscipit eget sed purus. Integer augue lectus, consectetur sit amet velit quis, auctor dignissim lacus. Integer suscipit pulvinar justo a condimentum. Nam tincidunt pretium risus in ullamcorper. Aliquam efficitur, enim vitae consectetur sagittis, nunc leo porttitor dolor, et tristique metus ipsum sed erat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut tellus nisl, dictum in justo nec, volutpat laoreet libero.");

	}
	
	private void setupDropdown(@NonNull View view) {
		View listPopupWindowButton = view.findViewById(R.id.ortaggio_fl_dropdown);
		View scrim = view.findViewById(R.id.ortaggio_fl_scrim);
		ImageView listPopupWindowButtonIcon = view.findViewById(R.id.ortaggio_fl_dropdown_icon);
		ListPopupWindow listPopupWindow = new ListPopupWindow(getContext(), null, com.google.android.material.R.attr.listPopupWindowStyle);
		listPopupWindow.setAnchorView(listPopupWindowButton);
		List<String> items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4"
//				, "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4", "Item 4"
		);
		ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.ortaggio_fl_dropdown_item, items);
		listPopupWindow.setAdapter(adapter);
		
		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkg_dropdown_window));
		listPopupWindow.setVerticalOffset(0);
		listPopupWindow.setHeight(Math.min(items.size() * Utils.dp2px(40, getContext()), Utils.dp2px(600, getContext())));  // FIXME!!!


//		// Set list popup's item click listener
//		listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
//		  // Respond to list popup window item click.
//
//		  // Dismiss popup
//		  listPopupWindow.dismiss()
//		}
		
		listPopupWindowButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (System.currentTimeMillis() - dropdownDismissTime > 200) {  // FIXME!!!
					listPopupWindow.show();
					listPopupWindowButtonIcon.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
					scrim.animate().alpha(1.0f).setDuration(100);
				}
			}
		});
		
		listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				dropdownDismissTime = System.currentTimeMillis();
				listPopupWindowButtonIcon.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
				scrim.animate().alpha(0.0f).setDuration(100);
			}
		});
	}
	
	// Show appbar right menu
	@Override
	public void onPrepareOptionsMenu(@NonNull final Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.ortaggio_fl_toolbar_menu, menu);
	}
	
}
