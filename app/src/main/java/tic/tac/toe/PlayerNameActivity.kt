package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class PlayerNameActivity : AppCompatActivity() {

    private lateinit var player1: EditText
    private lateinit var player2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_name)

        //hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        //connecting to UI elements
        player1 = findViewById(R.id.editPlayerOneName)
        player2 = findViewById(R.id.editPlayerTwoName)

        val finishButtonOnClick = findViewById<Button>(R.id.finishButton)
        finishButtonOnClick.setOnClickListener {
            val player1name: String = player1.text.toString()
            val player2name: String = player2.text.toString()

            //put both names into a bundle so they can be passed together to the next activity
            val extras = Bundle()
            extras.putString("PLAYER1_NAME", player1name)
            extras.putString("PLAYER2_NAME", player2name)

            //sending the names to the MainActivity so they can be used for display purposes
            val intent = Intent(this@PlayerNameActivity, MainActivity::class.java)
            intent.putExtra("Player_NAMES", extras)
            startActivity(intent)
            finish()
        }
    }
}