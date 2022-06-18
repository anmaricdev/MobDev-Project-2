package tic.tac.toe

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // tag for logging
    private val TAG = "TicTacToe"

    private lateinit var p1WinStreak: TextView
    private lateinit var p2WinStreak: TextView
    private lateinit var winnerDisplay: TextView
    private lateinit var player1Name: TextView
    private lateinit var player2Name: TextView
    private lateinit var buttonNG: Button
    private lateinit var button_0: Button
    private lateinit var button_1: Button
    private lateinit var button_2: Button
    private lateinit var button_3: Button
    private lateinit var button_4: Button
    private lateinit var button_5: Button
    private lateinit var button_6: Button
    private lateinit var button_7: Button
    private lateinit var button_8: Button

    // Counter that counts the win streak of player1 and player2
    var p1counter = 0
    var p2counter = 0

    // Counter that keeps track of the current round, maximum being 9 as there are only 9 fields in the game
    var roundCounter = 0

    // makes sure that players switch their turns after their turn ends (after setting your X/O)
    // Player1 = true; Player2 = false
    var hasTurn = true

    // array that consists of all the possible positions to place an X or O (buttons of the UI)

   // private var buttons: Array<Button> = arrayOf()

    /* KEEPING TRACK OF THE GAME PROGRESS/STATE:
    * If player one owns a field => player1 = 0
    * If player one owns a field => player2 = 1
    * If the box is empty and no one owns it yet => emptyField = 2
    * */
    // initialize all boxes as empty fields
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    // multidimensional array to determine the winning conditions
    // source: https://stackoverflow.com/questions/34145495/2d-array-in-kotlin
    var winningConditions: Array<IntArray> = arrayOf(// Horizontal winning conditions
                                    intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
                                    // Vertical winning conditions
                                    intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
                                    // Diagonal winning conditions
                                    intArrayOf(0, 4, 8), intArrayOf(2, 4, 6))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hides the top bar that says "Three in a row"
        // supportActionBar?.hide()
        // connecting to UI elements
        p1WinStreak = findViewById(R.id.PlayerOneWinStreak)
        p2WinStreak = findViewById(R.id.PlayerTwoWinStreak)
        winnerDisplay = findViewById(R.id.WinnerDisplay)
        player1Name = findViewById(R.id.PlayerOne)
        player2Name = findViewById(R.id.PlayerTwo)
        buttonNG = findViewById(R.id.Button_NewGame)
        button_0 = findViewById(R.id.Button_0)
        button_1 = findViewById(R.id.Button_1)
        button_2 = findViewById(R.id.Button_2)
        button_3 = findViewById(R.id.Button_3)
        button_4 = findViewById(R.id.Button_4)
        button_5 = findViewById(R.id.Button_5)
        button_6 = findViewById(R.id.Button_6)
        button_7 = findViewById(R.id.Button_7)
        button_8 = findViewById(R.id.Button_8)

        // initializing the button array
        /*for (i in buttons.indices){
            val buttonID: String = "Button_" + i
            val resourceID: Int = resources.getIdentifier(buttonID, "id", packageName)
            buttons[i] = findViewById(resourceID)
            buttons[i].setOnClickListener(this)
            Log.v(TAG, "Button: " + buttons[i])
        }*/

        // checks if button is clicked
        button_0.setOnClickListener(this)
        button_1.setOnClickListener(this)
        button_2.setOnClickListener(this)
        button_3.setOnClickListener(this)
        button_4.setOnClickListener(this)
        button_5.setOnClickListener(this)
        button_6.setOnClickListener(this)
        button_7.setOnClickListener(this)
        button_8.setOnClickListener(this)

        // assigning the entered names from the PlayerNameActivity in here to be properly display in the UI
        val player1: String = intent.getStringExtra("Player1_NAME").toString()
        val player2: String = intent.getStringExtra("Player2_NAME").toString()

        player1Name.text = player1
        player2Name.text = player2

        val newGameButtonOnClick = findViewById<Button>(R.id.Button_NewGame)
        newGameButtonOnClick.setOnClickListener {
            p1counter = 0
            p2counter = 0
            winnerDisplay.text = "It's a TIE! Close game!"
            updatePlayerWinstreak()
            playAgain()
        }
        val homeButtonOnClick = findViewById<Button>(R.id.Button_Home)
        homeButtonOnClick.setOnClickListener {
            val intent = Intent(this@MainActivity, PreGameActivity::class.java)
            startActivity(intent)
            finish() // prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }
    }

    override fun onClick(view: View) {
        // takes id from corresponding button (e.g. Button_1, Button_2 etc)
        val buttonID = view.resources.getResourceEntryName(view.id)

        // cuts the string in front of the last char and only takes the last char and saves it in gameStatus var (e.g from Button_1 to 1 -> cuts the Button_1)
        // this is important to change the gameState above (keep track on which player assigned what field to their mark in their turn)
        // e.g if p1 chooses the second row, 2nd position (middle field), it will change the corresponding field in the array to mark that it's not empty(2) anymore
        // and assigns it to p1
        var gameStatus = Integer.parseInt(buttonID.substring(buttonID.length - 1, buttonID.length))
        // check what player is playing

        // player1 turn
        if (hasTurn) {
            when (view.id) {
                R.id.Button_0 -> {
                    // if a button has been assigned by a player with their mark, it must not be changed by the other player
                    // if it's not empty, it will go out the onClick func (return)
                    if (!(button_0.text.toString()).equals("")) {
                        return
                    } else {
                        button_0.text = "X"
                        button_0.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_1 -> {
                    if (!(button_1.text.toString()).equals("")) {
                        return
                    } else {
                        button_1.text = "X"
                        button_1.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_2 -> {
                    if (!(button_2.text.toString()).equals("")) {
                        return
                    } else {
                        button_2.text = "X"
                        button_2.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_3 -> {
                    if (!(button_3.text.toString()).equals("")) {
                        return
                    } else {
                        button_3.text = "X"
                        button_3.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_4 -> {
                    if (!(button_4.text.toString()).equals("")) {
                        return
                    } else {
                        button_4.text = "X"
                        button_4.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_5 -> {
                    if (!(button_5.text.toString()).equals("")) {
                        return
                    } else {
                        button_5.text = "X"
                        button_5.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_6 -> {
                    if (!(button_6.text.toString()).equals("")) {
                        return
                    } else {
                        button_6.text = "X"
                        button_6.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_7 -> {
                    if (!(button_7.text.toString()).equals("")) {
                        return
                    } else {
                        button_7.text = "X"
                        button_7.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
                R.id.Button_8 -> {
                    if (!(button_8.text.toString()).equals("")) {
                        return
                    } else {
                        button_8.text = "X"
                        button_8.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 0
                        roundCounter++
                    }
                }
            }
            // Player2 turn
        } else {
            when (view.id) {
                R.id.Button_0-> {
                    if (!(button_0.text.toString()).equals("")) {
                        return
                    } else {
                        button_0.text = "O"
                        button_0.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_1 -> {
                    if (!(button_1.text.toString()).equals("")) {
                        return
                    } else {
                        button_1.text = "O"
                        button_1.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_2 -> {
                    if (!(button_2.text.toString()).equals("")) {
                        return
                    } else {
                        button_2.text = "O"
                        button_2.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_3 -> {
                    if (!(button_3.text.toString()).equals("")) {
                        return
                    } else {
                        button_3.text = "O"
                        button_3.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_4 -> {
                    if (!(button_4.text.toString()).equals("")) {
                        return
                    } else {
                        button_4.text = "O"
                        button_4.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_5 -> {
                    if (!(button_5.text.toString()).equals("")) {
                        return
                    } else {
                        button_5.text = "O"
                        button_5.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_6 -> {
                    if (!(button_6.text.toString()).equals("")) {
                        return
                    } else {
                        button_6.text = "O"
                        button_6.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_7 -> {
                    if (!(button_7.text.toString()).equals("")) {
                        return
                    } else {
                        button_7.text = "O"
                        button_7.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
                R.id.Button_8 -> {
                    if (!(button_8.text.toString()).equals("")) {
                        return
                    } else {
                        button_8.text = "O"
                        button_8.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 1
                        roundCounter++
                    }
                }
            }
        }
        if (checkWinner()){
            if(hasTurn) {
                p1counter++
                updatePlayerWinstreak()
                val warning = AlertDialog.Builder(this@MainActivity)
                warning.setTitle(player1Name.text.toString() + " is the Winner!")
                warning.setMessage("Congratulations!")
                warning.setCancelable(true)
                warning.setIcon(R.drawable.ic_winner_symbol)
                warning.show()
                playAgain()
            }else {
                p2counter++
                updatePlayerWinstreak()
                val warning = AlertDialog.Builder(this@MainActivity)
                warning.setTitle(player2Name.text.toString() + " is the Winner!")
                warning.setMessage("Congratulations!")
                warning.setCancelable(true)
                warning.setIcon(R.drawable.ic_winner_symbol)
                warning.show()
                playAgain()
            }
        }else if(roundCounter == 9){
            val warning = AlertDialog.Builder(this@MainActivity)
            warning.setTitle("DRAW!")
            warning.setMessage("It's a draw")
            warning.setCancelable(true)
            warning.setIcon(R.drawable.ic_draw_symbol)
            warning.show()
            playAgain()
        }else{
            hasTurn = !hasTurn
        }

        // changes the display depending on the state of the game (e.g. who is winning and if it's a draw)
        if (p1counter > p2counter){
            winnerDisplay.text = player1Name.text.toString() + " is winning!"
        } else if (p2counter > p1counter){
            winnerDisplay.text = player2Name.text.toString() + " is winning!"
        } else {
            winnerDisplay.text = "It's a TIE! Close game!"
        }
    }

    // after each click compares current game state with the winning preconditions.
    // If a winner is found, returns a boolean for further functions above (e.g. notifications, starting a new game)
    private fun checkWinner(): Boolean {
        var isWinner = false

        for(winningPosition: IntArray in winningConditions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                gameState[winningPosition[0]] != 2){ isWinner = true
            }
        }
        return isWinner
        }

    private fun updatePlayerWinstreak(){
        p1WinStreak.text = p1counter.toString()
        p2WinStreak.text = p2counter.toString()
    }

    private fun playAgain(){
        roundCounter = 0
        hasTurn = true

        for (i in gameState.indices) {
            gameState[i] = 2
            //buttons[i].text = ""
        }
        button_0.text =""
        button_1.text =""
        button_2.text =""
        button_3.text =""
        button_4.text =""
        button_5.text =""
        button_6.text =""
        button_7.text =""
        button_8.text =""
    }
}
