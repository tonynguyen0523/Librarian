package com.example.android.librarian;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Base url of book data search
     */
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Adapter for list of book results
     */
    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initializing adapter
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Initialize list view
        ListView resultListView = (ListView) findViewById(R.id.result_listview);
        resultListView.setAdapter(mAdapter);

        // Empty text view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        resultListView.setEmptyView(mEmptyStateTextView);

        mEmptyStateTextView.setText(R.string.press_search_icon);

        // Set loading spinner visibility GONE so it would not show up unless need to onClick
        View loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.activity_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.clear();

                // Replace white space to create valid url
                String newTextFix = query.replace(" ", "%20");
                String completeUrl = BASE_URL + newTextFix;

                // Checking if there is internet connection first
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is internet
                if (networkInfo != null && networkInfo.isConnected()) {

                    // Show progress bar while loading data
                    View loadingSpinner = findViewById(R.id.loading_spinner);
                    loadingSpinner.setVisibility(View.VISIBLE);

                    // Make the empty text view GONE
                    mEmptyStateTextView.setVisibility(View.GONE);

                    // Initiate AsyncTask
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(completeUrl);

                } else {
                    // If no internet connection
                    View loadingSpinner = findViewById(R.id.loading_spinner);
                    loadingSpinner.setVisibility(View.GONE);

                    // Show empty text view
                    mEmptyStateTextView.setText(R.string.no_internet_text);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // If search bar is blank show instruction
                if (newText.length() == 0) {
                    mAdapter.clear();
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.press_search_icon);
                }
                return false;
            }
        });
        return true;
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... url) {

            // If no url then return early
            if (url.length < 1 || url[0] == null) {
                return null;
            }
            // Get book info from the zero param
            List<Book> result = QueryUtils.fetchBookInfo(url[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> info) {

            mAdapter.clear();

            // Set loading spinner visibility view gone to display list view clearly
            View loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);

            // If data is not null and not empty display result
            if (info != null && !info.isEmpty()) {
                mAdapter.addAll(info);
            } else {
                // Else display empty text view instruction
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText(R.string.valid_request);
            }
        }
    }

}
