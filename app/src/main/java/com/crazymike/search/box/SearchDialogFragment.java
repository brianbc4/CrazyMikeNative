package com.crazymike.search.box;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.crazymike.R;
import com.crazymike.api.NetworkService;
import com.crazymike.api.URL;
import com.crazymike.base.BaseDialogFragment;
import com.crazymike.main.MainActivity;
import com.crazymike.models.SearchHotKey;
import com.crazymike.util.DialogTool;
import com.crazymike.util.PreferencesKey;
import com.crazymike.util.PreferencesTool;
import com.crazymike.widget.WrapContentGridLayoutManager;

public class SearchDialogFragment extends BaseDialogFragment implements SearchContract.View, SearchAdapter.OnHotKeyClickListener {

    private static final String TAG = "SearchDialogFragment";

    private SearchPresenter presenter;
    private Listener listener;

    public interface Listener {

        void OnHotKeyClick(SearchHotKey searchHotKey);

        void OnGetSearchKey(String search);
    }

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment dialogFragment = new SearchDialogFragment();
        Bundle bundle = new Bundle();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container, false);

        /* back */
        (view.findViewById(R.id.back)).setOnClickListener(v -> dismiss());

        /* search */
        EditText keyword = (EditText) view.findViewById(R.id.keyword);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        keyword.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                onGetSearchKey(keyword.getText().toString());
            }
            return true;
        });

        /* Input */
        Button searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> onGetSearchKey(keyword.getText().toString()));

        /* RecyclerView */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new WrapContentGridLayoutManager(context, 3));
        recyclerView.setAdapter(new SearchAdapter(context, presenter.getSearchHotKeyList(), this));

        return view;
    }

    @Override
    public void onGetSearchKey(String searchKey) {
        if (searchKey.length() > 0) {
            listener.OnGetSearchKey(searchKey);
            dismiss();
        }
    }

    @Override
    public void onHotKeyClick(SearchHotKey searchHotKey) {
        listener.OnHotKeyClick(searchHotKey);
        dismiss();
    }
}
