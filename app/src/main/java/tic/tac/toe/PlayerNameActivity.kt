package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

class PlayerNameActivity : AppCompatActivity() {

    private val TAG = "NAMES"

    private lateinit var player1: EditText
    private lateinit var player2: EditText
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_name)

        // hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        // connecting to UI elements
        player1 = findViewById(R.id.editPlayerOneName)
        player2 = findViewById(R.id.editPlayerTwoName)
        errorMessage = findViewById(R.id.error_message)

        // creating a warning pop up in case names are missing
        val warning = AlertDialog.Builder(this@PlayerNameActivity)
        warning.setTitle("NAMES MISSING!")
        warning.setMessage("Please enter the names of both players!")
        warning.setCancelable(true)
        warning.setIcon(R.drawable.ic_warning_symbol)

        val finishButtonOnClick = findViewById<Button>(R.id.finishButton)
        finishButtonOnClick.setOnClickListener {
            val player1name: String = player1.text.toString()
            val player2name: String = player2.text.toString()

            // variables that check if the editText fields are null or empty
            val isNullOrEmpty = player1name.isNullOrEmpty()
            val isNullOrEmpty2 = player2name.isNullOrEmpty()

            // checks if the name fields are null or empty, if true => shows a warning pop up (above)
            // if the names are correctly entered (case: false), continues to the MainActivity and passes
            // the names in their corresponding TextView variable
            if (isNullOrEmpty != true  && isNullOrEmpty2 != true ){
                val intent = Intent(this@PlayerNameActivity, MainActivity::class.java)
                intent.putExtra("Player1_NAME", player1name)
                intent.putExtra("Player2_NAME", player2name)
                Log.v(TAG, "Player 1NAMENAME: " + player1name)
                Log.v(TAG, "Player 2NAMENAME: " + player2name)
                startActivity(intent)
                finish()
            }else {
                warning.show()
                //errorMessage.setVisibility(View.VISIBLE)
            }
        }
    }
    override fun onResume(){
        super.onResume()
        Log.v(TAG, "On resume...")
    }
    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "On restart...")
    }
}