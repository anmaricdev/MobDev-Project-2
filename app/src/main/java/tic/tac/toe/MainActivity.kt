package tic.tac.toe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    // tag for logging
    private val TAG = "TicTacToe"

    //private lateinit var taskView: TextView

    private var p1WinStreak: TextView = findViewById(R.id.PlayerOneWinStreak)
    private var p2WinStreak: TextView = findViewById(R.id.PlayerTwoWinStreak)
    private var winnerDisplay: TextView = findViewById(R.id.WinnerDisplay)
    private val buttonNG: Button = findViewById(R.id.Button_NewGame)
    private val buttons = arrayListOf<Int>(9)

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

    }
}