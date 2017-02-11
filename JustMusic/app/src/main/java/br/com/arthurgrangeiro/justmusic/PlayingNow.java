package br.com.arthurgrangeiro.justmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayingNow extends AppCompatActivity {

    private int viewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_now);

        viewId = getIntent().getExtras().getInt("id");
        final String mySong = getIntent().getExtras().getString("song");
        final String myArtist = getIntent().getExtras().getString("artist");

        final TextView play = (TextView) findViewById(R.id.now_playing_play);
        play.setText(getResources().getString(R.string.pause));
        final TextView song = (TextView) findViewById(R.id.now_playing_song);
        song.setText(mySong);
        final TextView artist = (TextView) findViewById(R.id.now_playing_artist);
        artist.setText(myArtist);
        ImageView imageView = (ImageView) findViewById(R.id.now_playing_image);
        defineSongImage(imageView);

        Button buttonBack = (Button) findViewById(R.id.now_playing_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNowPlaying();
            }
        });

        Button buttonPlay = (Button) findViewById(R.id.now_playing_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNowPlaying(play);
            }


        });

        Button buttonNext = (Button) findViewById(R.id.now_playing_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextNowPlaying();
            }
        });

        TextView details = (TextView) findViewById(R.id.now_playing_details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetails(mySong, myArtist);
            }
        });
    }

    /**
     * Checks which song is playing and selects the appropriate image.
     *
     * @param imageView
     */
    private void defineSongImage(ImageView imageView) {
        switch (viewId) {
            case R.id.song_one:
                imageView.setImageResource(R.drawable.centuries);
                break;
            case R.id.song_two:
                imageView.setImageResource(R.drawable.immortal);
                break;
            case R.id.song_three:
                imageView.setImageResource(R.drawable.hall_of_fame);
                break;
            case R.id.song_four:
                imageView.setImageResource(R.drawable.broken_string);
                break;
            case R.id.song_five:
                imageView.setImageResource(R.drawable.i_see_fire);
                break;
        }
    }

    /**
     * Check which song is currently playing, pause it, and skip to the previous song.
     */
    private void backNowPlaying() {
        Intent intent = new Intent(this, PlayingNow.class);
        switch (viewId) {
            case R.id.song_one:
                intent.putExtra("song", getResources().getString(R.string.i_see_fire));
                intent.putExtra("artist", getResources().getString(R.string.ed_sheeran));
                intent.putExtra("id", R.id.song_five);
                break;
            case R.id.song_two:
                intent.putExtra("song", getResources().getString(R.string.centuries));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                intent.putExtra("id", R.id.song_one);
                break;
            case R.id.song_three:
                intent.putExtra("song", getResources().getString(R.string.immortals));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                intent.putExtra("id", R.id.song_two);
                break;
            case R.id.song_four:
                intent.putExtra("song", getResources().getString(R.string.hall_of_fame));
                intent.putExtra("artist", getResources().getString(R.string.the_script));
                intent.putExtra("id", R.id.song_three);
                break;
            case R.id.song_five:
                intent.putExtra("song", getResources().getString(R.string.broken_strings));
                intent.putExtra("artist", getResources().getString(R.string.james_morrison));
                intent.putExtra("id", R.id.song_four);
                break;
        }
        startActivity(intent);
    }

    /**
     * If the music is playing, it will be paused, if it is paused, it will play again.
     * The screen is updated according to the state of the music.
     *
     * @param textView
     */
    private void playNowPlaying(TextView textView) {
        if (textView.getText().equals(getResources().getString(R.string.pause))) {
            textView.setText(getResources().getString(R.string.play));
        } else {
            textView.setText(getResources().getString(R.string.pause));
        }
    }

    /**
     * Check which song is playing, stop it and start the next song.
     */
    private void nextNowPlaying() {
        Intent intent = new Intent(this, PlayingNow.class);
        switch (viewId) {
            case R.id.song_one:
                intent.putExtra("song", getResources().getString(R.string.immortals));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                intent.putExtra("id", R.id.song_two);
                break;
            case R.id.song_two:
                intent.putExtra("song", getResources().getString(R.string.hall_of_fame));
                intent.putExtra("artist", getResources().getString(R.string.the_script));
                intent.putExtra("id", R.id.song_three);
                break;
            case R.id.song_three:
                intent.putExtra("song", getResources().getString(R.string.broken_strings));
                intent.putExtra("artist", getResources().getString(R.string.james_morrison));
                intent.putExtra("id", R.id.song_four);
                break;
            case R.id.song_four:
                intent.putExtra("song", getResources().getString(R.string.i_see_fire));
                intent.putExtra("artist", getResources().getString(R.string.ed_sheeran));
                intent.putExtra("id", R.id.song_five);
                break;
            case R.id.song_five:
                intent.putExtra("song", getResources().getString(R.string.centuries));
                intent.putExtra("artist", getResources().getString(R.string.fall_out_boys));
                intent.putExtra("id", R.id.song_one);
                break;
        }
        startActivity(intent);
    }

    /**
     * Go to the page that displays all available information about the song.
     *
     * @param song
     * @param artist
     */

    private void goToDetails(String song, String artist) {
        Intent intent = new Intent(this, SongDetails.class);
        intent.putExtra("song", song);
        intent.putExtra("artist", artist);
        startActivity(intent);
    }
}
