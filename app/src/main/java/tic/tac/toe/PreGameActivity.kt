package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

class PreGameActivity : AppCompatActivity() {

    private val TAG = "PRE_GAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        // hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        // creating a warning pop up
        val warning = AlertDialog.Builder(this@PreGameActivity)
            warning.setTitle("Warning!")
            warning.setMessage("Are you sure you want to exit the app?")
            warning.setCancelable(false)
            warning.setIcon(R.drawable.ic_warning_symbol)
            warning.setPositiveButton(android.R.string.yes){warning, which ->
                //destroys the app completely after pressing yes
                finishAndRemoveTask()
                exitProcess(0)
            }
            warning.setNegativeButton(android.R.string.no){warning, which ->
            }


        // continues/switches to the next activity (CreateLobbyActivity)
        val startGameButtonClick = findViewById<Button>(R.id.startGameButton)
        startGameButtonClick.setOnClickListener {
            val intent = Intent(this@PreGameActivity, CreateLobbyActivity::class.java)
            startActivity(intent)
            finish()
        }
        // If the exit app button is pressed, the app sends a pop up that gives the user the option to
        // proceed to exit the app or, in case of a misinput, cancel the exit process and return to
        // the PreGameActivity screen
        val exitGameButtonClick = findViewById<Button>(R.id.exitGameButton)
        exitGameButtonClick.setOnClickListener {
            warning.show()
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