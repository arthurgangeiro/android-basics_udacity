package br.com.arthurgrangeiro.justmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout song_one = (LinearLayout) findViewById(R.id.song_one);
        LinearLayout song_two = (LinearLayout) findViewById(R.id.song_two);
        LinearLayout song_three = (LinearLayout) findViewById(R.id.song_three);
        LinearLayout song_four = (LinearLayout) findViewById(R.id.song_four);
        LinearLayout song_five = (LinearLayout) findViewById(R.id.song_five);
        Button storeButton = (Button) findViewById(R.id.store_button);

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStore();
            }
        });

        song_one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlayinNowScreen(v);
            }
        });

        song_two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlayinNowScreen(v);
            }
        });

        song_three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlayinNowScreen(v);
            }
        });

        song_four.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlayinNowScreen(v);
            }
        });

        song_five.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPlayinNowScreen(v);
            }
        });
    }

    /**
     * Check which song was selected and start it.
     *
     * @param view
     */
    private void goToPlayinNowScreen(View view) {
        Intent intent = new Intent(this, PlayingNow.class);
        switch (view.getId()) {
            case R.id.song_one:
                intent.putExtra("song", getResources().getString(R.string.centuries));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                break;
            case R.id.song_two:
                intent.putExtra("song", getResources().getString(R.string.immortals));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                break;
            case R.id.song_three:
                intent.putExtra("song", getResources().getString(R.string.hall_of_fame));
                intent.putExtra("artist", getResources().getString(R.string.the_script));
                break;
            case R.id.song_four:
                intent.putExtra("song", getResources().getString(R.string.broken_strings));
                intent.putExtra("artist", getResources().getString(R.string.james_morrison));
                break;
            case R.id.song_five:
                intent.putExtra("song", getResources().getString(R.string.i_see_fire));
                intent.putExtra("artist", getResources().getString(R.string.james_morrison));
                break;
        }
        intent.putExtra("id", view.getId());
        startActivity(intent);
    }

    /**
     * Go to the store. The store is part of the app.
     */
    private void goToStore() {
        Intent intent = new Intent(this, Store.class);
        startActivity(intent);
    }
}
