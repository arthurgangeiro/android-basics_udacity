package br.com.arthurgrangeiro.justmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SongDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        TextView title = (TextView) findViewById(R.id.details_title);
        title.setText(getIntent().getExtras().getString("song"));
        TextView subtitle = (TextView) findViewById(R.id.details_subtitle);
        subtitle.setText("Artist: " + getIntent().getExtras().getString("artist"));
        Button buttonBack = (Button) findViewById(R.id.details_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
