package com.weatheraggregator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.weatheraggregator.app.R;

@EViewGroup(R.layout.layout_search)
public class ViewSearch extends LinearLayout {

    public ViewSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById(R.id.edSearch)
    protected EditText editText;

    @ViewById(R.id.pb)
    protected ProgressBar progress;
    private ISearchCity searchListener;
    @ViewById(R.id.edSearch)
    protected EditText edSearch;

    @AfterViews
    protected void initView() {
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().toString() != null && v.getText().toString().length() > 3)
                        searchListener.searchCityResult(v.getText().toString());
                }
                return false;
            }
        });
    }

    public void setSearchListener(ISearchCity searcher) {
        searchListener = searcher;
    }

    public void progressVisibility(boolean isVisible) {
        if (isVisible) {
            progress.setVisibility(VISIBLE);
        } else {
            progress.setVisibility(GONE);
        }
    }

    public EditText getETSearch() {
        return edSearch;
    }

    public interface ISearchCity {
        public void searchCityResult(String cityName);
    }
}
