package com.google.engedu.ghost;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostText;
    private TextView status;
    private String currentword = "";
    private SimpleDictionary simpleDictionary;
    private Button reset;
    private Button challenge;
    private static String current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        reset = (Button)findViewById(R.id.reset);
        challenge = (Button)findViewById(R.id.challenge);

        ghostText = (TextView) findViewById(R.id.ghostText);
        status = (TextView)findViewById(R.id.gameStatus);


        try {

            simpleDictionary = new SimpleDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(current, currentword);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void click(View view){
        if(view.getId() == R.id.reset){
            status.setText("User turn");
            currentword="";
            onStart(null);
        }else if(view.getId() == R.id.challenge){
            if(simpleDictionary.isWord(currentword) && currentword.length()>=4) {
                status.setText("User wins");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("User wins");
                builder.create().show();
                currentword="";
                status.setText("user turn");
                ghostText.setText("");
                onStart(null);
            }
            else {
                String next = simpleDictionary.getAnyWordStartingWith(currentword);
                if(next==null) {
                    status.setText("User wins over computer");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("User wins");
                    builder.create().show();
                    currentword="";
                    status.setText("user turn");
                    ghostText.setText("");
                    onStart(null);
                }
                else {
                    if (currentword == "")
                        status.setText("You are here to play or not ??");
                    else {
                        status.setText("Computer wins");
                        ghostText.setText(next);
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
       // Toast.makeText(GhostActivity.this, "im here " + String.valueOf(keyCode) + (char) event.getUnicodeChar(), Toast.LENGTH_SHORT).show();
        if(keyCode >=29 && keyCode<=54){
            currentword+=(char) event.getUnicodeChar();

            ghostText.setText(currentword);
            computerTurn();
            return true;
        }else
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {

        status.setText("computer turn");
       //Toast.makeText(GhostActivity.this, "Im IN COMPUTER" + currentword, Toast.LENGTH_SHORT).show();

        if (simpleDictionary.isWord(currentword) && currentword.length() >= 4) {
            status.setText("Computer wins..");

            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setMessage("Computer wins\n" + currentword + " is a word");
            builder.create().show();

            currentword="";
            status.setText("user turn");
            ghostText.setText("");
            onStart(null);
        }
        else {

            String next = simpleDictionary.getAnyWordStartingWith(currentword);

            Toast.makeText(GhostActivity.this, "this is the next text" + next, Toast.LENGTH_SHORT).show();

            if (next == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("U lost..No such word exist..U lost");
                builder.create().show();
                status.setText("No such word exist..U lost");
                //status.setText("User turn");

            }
            else {
                currentword += next.charAt(currentword.length());
                ghostText.setText(currentword);
                status.setText("User turn");
            }

            // Do computer turn stuff then make it the user's turn again

            //status.setText(USER_TURN);
        }

    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {

        currentword = "";
        userTurn = random.nextBoolean();
        ghostText.setText("");


        if (userTurn) {
            status.setText(USER_TURN);
        } else {
            status.setText(COMPUTER_TURN);
            Toast.makeText(GhostActivity.this,"chance is " + userTurn + " computer plays.." ,Toast.LENGTH_SHORT).show();

            computerTurn();
        }
        return true;
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        currentword = savedInstanceState.getString(current);
    }
}
