package com.example.ajaytiwari.parselogin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by ajay.tiwari on 05-11-2015.
 */
public class ContactsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<ParseUser> parseUsersList;
    ProgressDialog dialog;
    int search_value = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Contacts");
        dialog = new ProgressDialog(ContactsActivity.this);
        dialog.setMessage("Loading Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("location", "null");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    parseUsersList = objects;
                    mAdapter = new RVAdapter(objects);
                    mRecyclerView.setAdapter(mAdapter);
                    dialog.dismiss();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_filter) {
            show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                List<ParseUser> filteredList = new ArrayList<>();
                for (int index = 0; index < parseUsersList.size(); index++) {
                    if (search_value == 1) {
                        if (parseUsersList.get(index).getString("name").contains(newText)) {
                            filteredList.add(parseUsersList.get(index));

                        }
                    }

                    if (search_value == 2) {
                        if (String.valueOf(parseUsersList.get(index).getString("batch")).contains(newText)) {
                            filteredList.add(parseUsersList.get(index));

                        }
                    }
                    if (search_value == 3) {
                        if (parseUsersList.get(index).getString("profession").contains(newText)) {
                            filteredList.add(parseUsersList.get(index));

                        }
                    }
                }
                mAdapter = new RVAdapter(filteredList);
                mRecyclerView.setAdapter(mAdapter);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    public void show() {

        final Dialog d = new Dialog(ContactsActivity.this);

        d.setContentView(R.layout.filter_dialog);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final CheckBox name_chk = (CheckBox) d.findViewById(R.id.name_fltr);
        final CheckBox batch_chk = (CheckBox) d.findViewById(R.id.batch_fltr);
        final CheckBox profession_chk = (CheckBox) d.findViewById(R.id.profession_fltr);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_chk.isChecked()) {
                    search_value = 1;
                }
                if (batch_chk.isChecked()) {
                    search_value = 2;
                }

                if (profession_chk.isChecked()) {
                    search_value = 3;
                }
                d.dismiss();
            }
        });
        d.show();


    }
}
