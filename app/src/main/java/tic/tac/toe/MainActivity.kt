package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    // tag for logging
    private val TAG = "TicTacToe"

    //private lateinit var taskView: TextView

    private lateinit var p1WinStreak: TextView
    private lateinit var p2WinStreak: TextView
    private lateinit var winnerDisplay: TextView
    private lateinit var buttonNG: Button
    //private lateinit val buttons:  arrayListOf<Int>(9)

    //Counter for the win streak of player1 and player2
    var p1counter = 0
    var p2counter = 0

    //Counter that keeps track of the current round, maximum being 9 as there are only 9 fields in the game
    var roundCounter = 0

    //makes sure that players switch their turns after their turn ends (after setting your X/O)
    var hasTurn = false

    /*KEEPING TRACK OF THE GAME PROGRESS/STATE:
    * If player one owns a field => player1 = 0
    * If player one owns a field => player2 = 1
    * If the box is empty and no one owns it yet => emptyField = 2
    * */

    // initialize all boxes as empty fields
    val gameSate = arrayListOf<Int>(2, 2, 2, 2, 2, 2, 2, 2, 2)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hides the top bar that says "Three in a row"
        //supportActionBar?.hide()

        p1WinStreak = findViewById(R.id.PlayerOneWinStreak)
        p2WinStreak = findViewById(R.id.PlayerTwoWinStreak)
        winnerDisplay = findViewById(R.id.WinnerDisplay)
        buttonNG = findViewById(R.id.Button_NewGame)

        /*val newGameButtonOnClick = findViewById<Button>(R.id.Button_NewGame)
        newGameButtonOnClick.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayerNameActivity::class.java)
            startActivity(intent)
            finish() //prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }*/
        val homeButtonOnClick = findViewById<Button>(R.id.Button_Home)
        homeButtonOnClick.setOnClickListener {
            val intent = Intent(this@MainActivity, PreGameActivity::class.java)
            startActivity(intent)
            finish() //prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }
    }
}