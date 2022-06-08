package tic.tac.toe

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

class PreGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        //hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        val warning = AlertDialog.Builder(this@PreGameActivity)
            warning.setTitle("Warning!")
            warning.setMessage("Are you sure you want to exit the app?")
            warning.setCancelable(false)
            warning.setIcon(R.drawable.ic_warning_symbol)
            warning.setPositiveButton(android.R.string.yes){warning, which ->
                finish()
                exitProcess(0)
            }
            warning.setNegativeButton(android.R.string.no){warning, which ->
            }


        val startGameButtonClick = findViewById<Button>(R.id.startGameButton)
        startGameButtonClick.setOnClickListener {
            val intent = Intent(this@PreGameActivity, PlayerNameActivity::class.java)
            startActivity(intent)
            finish() //prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }
        val exitGameButtonClick = findViewById<Button>(R.id.exitGameButton)
        exitGameButtonClick.setOnClickListener {
            warning.show()
        }
    }
}