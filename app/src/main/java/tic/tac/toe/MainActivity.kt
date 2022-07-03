package tic.tac.toe

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

var hasTurn = isCodeMaker

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // tag for logging
    private val TAG = "TicTacToe"

    private lateinit var p1WinStreak: TextView
    private lateinit var p2WinStreak: TextView
    private lateinit var turnDisplay: TextView
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

    // makes sure that players switch their turns after setting their marks
    // Player1 = true; Player2 = false
    //var hasTurn = true

    /* KEEPING TRACK OF THE GAME PROGRESS/STATE:
    * If player one owns a field => player1 = 1
    * If player one owns a field => player2 = 2
    * If the box is empty and no one owns it yet => emptyField = 0
    * */
    // initialize all boxes as empty fields
    var gameState = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

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
        turnDisplay = findViewById(R.id.TurnDisplay)
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

        //Initialize Database
        FirebaseDatabase.getInstance().reference.child("data").child(code).addChildEventListener(object : ChildEventListener{
            // add new data to the database
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var data = snapshot.value
                if(hasTurn == true){
                    hasTurn == false
                    moveOnline(data.toString(), hasTurn)
                } else {
                    hasTurn = true
                    moveOnline(data.toString(), hasTurn)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                playAgain()
                hasTurn = isCodeMaker
                if(isCodeMaker){
                    FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
                }
                Toast.makeText(this@MainActivity, "Game has been reset", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

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

        //initialize the text view with player 1 having the first turn
        turnDisplay.text = "It's " + player1Name.text.toString() + "'s turn!"

        val newGameButtonOnClick = findViewById<Button>(R.id.Button_NewGame)
        newGameButtonOnClick.setOnClickListener {
            p1counter = 0
            p2counter = 0
            turnDisplay.text = "It's " + player1Name.text.toString() + "'s turn!"
            updatePlayerWinstreak()
            playAgain()
        }
        val homeButtonOnClick = findViewById<Button>(R.id.Button_Home)
        homeButtonOnClick.setOnClickListener {
            val intent = Intent(this@MainActivity, PreGameActivity::class.java)
            removeCode()
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
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(0)
                    }
                }
                R.id.Button_1 -> {
                    if (!(button_1.text.toString()).equals("")) {
                        return
                    } else {
                        button_1.text = "X"
                        button_1.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(1)
                    }
                }
                R.id.Button_2 -> {
                    if (!(button_2.text.toString()).equals("")) {
                        return
                    } else {
                        button_2.text = "X"
                        button_2.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(2)
                    }
                }
                R.id.Button_3 -> {
                    if (!(button_3.text.toString()).equals("")) {
                        return
                    } else {
                        button_3.text = "X"
                        button_3.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(3)
                    }
                }
                R.id.Button_4 -> {
                    if (!(button_4.text.toString()).equals("")) {
                        return
                    } else {
                        button_4.text = "X"
                        button_4.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(4)
                    }
                }
                R.id.Button_5 -> {
                    if (!(button_5.text.toString()).equals("")) {
                        return
                    } else {
                        button_5.text = "X"
                        button_5.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(5)
                    }
                }
                R.id.Button_6 -> {
                    if (!(button_6.text.toString()).equals("")) {
                        return
                    } else {
                        button_6.text = "X"
                        button_6.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(6)
                    }
                }
                R.id.Button_7 -> {
                    if (!(button_7.text.toString()).equals("")) {
                        return
                    } else {
                        button_7.text = "X"
                        button_7.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(7)
                    }
                }
                R.id.Button_8 -> {
                    if (!(button_8.text.toString()).equals("")) {
                        return
                    } else {
                        button_8.text = "X"
                        button_8.setTextColor(Color.parseColor("#EBFFFB"))
                        gameState[gameStatus] = 1
                        roundCounter++
                        //updateDatabase(8)
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
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(9)
                    }
                }
                R.id.Button_1 -> {
                    if (!(button_1.text.toString()).equals("")) {
                        return
                    } else {
                        button_1.text = "O"
                        button_1.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(10)
                    }
                }
                R.id.Button_2 -> {
                    if (!(button_2.text.toString()).equals("")) {
                        return
                    } else {
                        button_2.text = "O"
                        button_2.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(11)
                    }
                }
                R.id.Button_3 -> {
                    if (!(button_3.text.toString()).equals("")) {
                        return
                    } else {
                        button_3.text = "O"
                        button_3.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(12)
                    }
                }
                R.id.Button_4 -> {
                    if (!(button_4.text.toString()).equals("")) {
                        return
                    } else {
                        button_4.text = "O"
                        button_4.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(13)
                    }
                }
                R.id.Button_5 -> {
                    if (!(button_5.text.toString()).equals("")) {
                        return
                    } else {
                        button_5.text = "O"
                        button_5.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(14)
                    }
                }
                R.id.Button_6 -> {
                    if (!(button_6.text.toString()).equals("")) {
                        return
                    } else {
                        button_6.text = "O"
                        button_6.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(15)
                    }
                }
                R.id.Button_7 -> {
                    if (!(button_7.text.toString()).equals("")) {
                        return
                    } else {
                        button_7.text = "O"
                        button_7.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(16)
                    }
                }
                R.id.Button_8 -> {
                    if (!(button_8.text.toString()).equals("")) {
                        return
                    } else {
                        button_8.text = "O"
                        button_8.setTextColor(Color.parseColor("#FF5858"))
                        gameState[gameStatus] = 2
                        roundCounter++
                        //updateDatabase(17)
                    }
                }
            }
        }
        if (checkWinner()){
            if(hasTurn) {
                // If player 1 wins
                p1counter++
                updatePlayerWinstreak()
                val warning = AlertDialog.Builder(this@MainActivity)
                warning.setTitle(player1Name.text.toString() + " is the Winner!")
                warning.setMessage("Congratulations!")
                warning.setCancelable(true)
                warning.setIcon(R.drawable.ic_winner_symbol)
                warning.show()
                playAgain()
                hasTurn = false // player 2 starts next turn
                //hasTurn = isCodeMaker
                if(isCodeMaker){
                    FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
                }
            }else {
                // If player 2 wins
                p2counter++
                updatePlayerWinstreak()
                val warning = AlertDialog.Builder(this@MainActivity)
                warning.setTitle(player2Name.text.toString() + " is the Winner!")
                warning.setMessage("Congratulations!")
                warning.setCancelable(true)
                warning.setIcon(R.drawable.ic_winner_symbol)
                warning.show()
                playAgain()
                hasTurn = true // player 1 starts next turn
                //hasTurn = isCodeMaker
                if(isCodeMaker){
                    FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
                }
            }
            //If everyone places their mark and no win condition is met
        }else if(roundCounter == 9){
            val warning = AlertDialog.Builder(this@MainActivity)
            warning.setTitle("DRAW!")
            warning.setMessage("It's a draw")
            warning.setCancelable(true)
            warning.setIcon(R.drawable.ic_draw_symbol)
            warning.show()
            playAgain()
            hasTurn = true
            //hasTurn = isCodeMaker
            if(isCodeMaker){
                FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
            }
        }else{ //switch turns
            hasTurn = !hasTurn
        }

        // changes the display depending on which player has the current turn
        if (hasTurn){
            turnDisplay.text = "It's " + player1Name.text.toString() + "'s turn!"
        } else {
            turnDisplay.text = "It's " + player2Name.text.toString() + "'s turn!"
        }
    }

    // after each click compares current game state with the winning preconditions.
    // If a winner is found, returns a boolean for further functions above (e.g. notifications, starting a new game)
    private fun checkWinner(): Boolean {
        var isWinner = false

        for(winningPosition: IntArray in winningConditions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                gameState[winningPosition[0]] != 0){ isWinner = true
            }
        }
        return isWinner
        }

    // adds up the rounds won counter to their assigned textView
    private fun updatePlayerWinstreak(){
        p1WinStreak.text = p1counter.toString()
        p2WinStreak.text = p2counter.toString()
    }

    //resets the game (reset roundCounter to zero, bring all the buttons back to neutral and clear the marks)
    private fun playAgain(){
        roundCounter = 0

        for (i in gameState.indices) {
            gameState[i] = 0
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

    // send data online to the database
    fun moveOnline(data : String, hasTurn : Boolean){
        if(hasTurn){
            var buttonSelected : Button?
            buttonSelected = when(data.toInt()){
                1->button_0
                2->button_1
                3->button_2
                4->button_3
                5->button_4
                6->button_5
                7->button_6
                8->button_6
                9->button_8
                else -> {
                    button_0
                }
            }
            buttonSelected.text = "0"
        }
    }

    //remove the code from the DB
    fun removeCode(){
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }
    // delete data from database if button for back is pressed
    override fun onBackPressed() {
        removeCode()
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
        }
        exitProcess(0)
    }

    // Update the cellId in the Database
    fun updateDatabase(cellId : Int){
        FirebaseDatabase.getInstance().reference.child("data").child(code).push().setValue(cellId)
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
