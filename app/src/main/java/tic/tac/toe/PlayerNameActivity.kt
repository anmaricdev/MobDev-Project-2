package tic.tac.toe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayerNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_name)

        //hides the top bar that says "Three in a row"
        supportActionBar?.hide()
    }
}