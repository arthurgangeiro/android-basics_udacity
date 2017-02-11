package br.com.arthurgrangeiro.thecrow;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final EditText seachItem = (EditText) findViewById(R.id.search_term);

        seachItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String userSearch = v.getText().toString();
                startNewFragment(userSearch);
                return false;
            }
        });
    }

    private void startNewFragment(String userSearch) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("userSearch", userSearch);

        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragm, fragment);
        fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.add(R.id.fragm, fragment);
        fragmentTransaction.commit();
    }
}
