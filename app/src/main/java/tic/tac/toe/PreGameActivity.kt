package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button

class PreGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        //hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        val startGameButton = findViewById<Button>(R.id.startGameButton)
        startGameButton.setOnClickListener {
            val intent = Intent(this@PreGameActivity, PlayerNameActivity::class.java)
            startActivity(intent)
            finish() //prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }
    }
}