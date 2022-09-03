package com.adragon.minesweeper

import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


var row = 1
var col = 1
val grid = ArrayList<Cell>()
var gameEnds = false
var totalMines = 0

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var menu:Menu
    var minesLeft = 0
    private var minesString = "Mines left -"
    private var flagSwitch = false
    private lateinit var buttons: ArrayList<ImageButton>
    lateinit var gridView: GridView
    lateinit var zoomView: ZoomView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntents()
        restart()
    }

    private fun getIntents(){
        val intent = intent.extras
        if (intent != null) {
            col = intent.get("col") as Int
            row = intent.get("row") as Int
            totalMines = intent.get("totalMines") as Int
        }
    }

    fun restart() {
        grid.clear()
        for (i in 0 until row)
            for (j in 0 until col)
                grid.add(Cell(i, j))
       setMines()
        setNeighbors()
        gameEnds = false
        flagSwitch = false
        minesLeft = totalMines

        buttons = createButtons()
        gridView = setGridView()
        zoomView = setZoomView()
        setContentView(zoomView)
        gridView.adapter = CellAdapter(buttons)
    }

    private fun buttonsStuff(){
        val revealedDrawable = ContextCompat.getDrawable(this, R.drawable.revealed_buttonshape)
        for (i in 0 until row) {
            for (j in 0 until col) {
                when {
                    grid[i * col + j].revealed -> {
                        buttons[i * col + j].background = revealedDrawable
                        buttons[i * col + j].setImageResource(
                            if (grid[i * col + j].isMine) {
                                R.drawable.tile_mine500
                            } else getNumImg(grid[i * col + j].neighbors)
                        )
                    }
                    (gameEnds && grid[i * col + j].flagged && !grid[i * col + j].isMine)
                    -> buttons[i * col + j].setImageResource(R.drawable.tile_wrong_500)
                    grid[i * col + j].flagged
                    -> buttons[i * col + j].setImageResource(R.drawable.tile_flag500)
                    !grid[i * col + j].flagged
                    -> buttons[i * col + j].setImageResource(R.drawable.nothing_selected)
                }

            }
        }
    }

    override fun onClick(v: View?) {
        runOnUiThread {
            if (!gameEnds){
                val position = v?.id as Int
                when{
                    !flagSwitch -> {
                        if (!grid[position].flagged) {
                            when {
                                grid[position].isMine -> {
                                    gameOver(this)
                                    buttons[position].background =
                                        ContextCompat.getDrawable(this, R.color.red)
                                }
                                (!grid[position].flagged && !grid[position].revealed) -> {
                                    grid[position].reveal()
                                    if (checkWin())
                                        winStuff(this)
                                }
                                (!grid[position].flagged && grid[position].revealed) ->
                                    grid[position].revealOnNumClick(this)
                            }
                        }
                    }
                    flagSwitch -> {
                        if  (!grid[position].flagged && grid[position].revealed)
                            grid[position].revealOnNumClick(this)
                        buttons[position].performLongClick()
                    }
                }
                buttonsStuff()
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val position = v?.id as Int
        if (!grid[position].revealed && !gameEnds){
            if (grid[position].flagged) minesLeft -= 1 else minesLeft += 1
            grid[position].flagged = !grid[position].flagged
            if (checkWin())
                winStuff(this)
            buttonsStuff()
            menu.findItem(R.id.MinesLeft).title = "$minesString $minesLeft"
        }
        return true
    }

    private fun getWindowSize(): Point {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        return Point(width, height)
    }

    private fun createButtons(): ArrayList<ImageButton> {
        val w = getWindowSize().x
        val size = if (col<row)
            w / col
        else w / row

        val layoutParams = ConstraintLayout.LayoutParams(size, size)
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

        val drawable = ContextCompat.getDrawable(this, R.drawable.buttonshape)
        val buttons = ArrayList<ImageButton>(row * col)
        for (i in 0 until row * col) {
            val button = ImageButton(this)
            button.id = i
            button.background = drawable
            button.layoutParams = layoutParams

            button.scaleType = ImageView.ScaleType.CENTER_CROP
            button.setOnLongClickListener(this)
            button.setOnClickListener(this)
            registerForContextMenu(button)
            buttons.add(button)
        }
        return buttons
    }

    private fun setGridView(): GridView {
        val gridView = GridView(this)
        gridView.numColumns = col
        val height = ConstraintLayout.LayoutParams.MATCH_PARENT
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val layoutParams = ConstraintLayout.LayoutParams(width, height)
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        gridView.layoutParams = layoutParams
        return gridView
    }

    private fun setZoomView(): ZoomView{
        val zoomView = ZoomView(this)
        val height = ConstraintLayout.LayoutParams.MATCH_PARENT
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val layoutParams = ConstraintLayout.LayoutParams(width, height)
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        zoomView.layoutParams = layoutParams
        val constrainLayout = ConstraintLayout(this)
        zoomView.addView(gridView)
        setContentView(constrainLayout)
        return zoomView
    }

    private fun getNumImg(num: Int): Int {
        return when (num) {
            1 -> R.drawable.tile_1_500
            2 -> R.drawable.tile_2_500
            3 -> R.drawable.tile_3_500
            4 -> R.drawable.tile_4_500
            5 -> R.drawable.tile_5_500
            6 -> R.drawable.tile_6_500
            7 -> R.drawable.tile_7_500
            8 -> R.drawable.tile_8_500
            else -> {
                return ContextCompat.getColor(this, R.color.purple_200)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu, menu)
        val nothing = ContextCompat.getDrawable(this, R.drawable.tile_flag_off500)
        menu.findItem(R.id.MinesLeft).title = "$minesString $totalMines"
        menu.findItem(R.id.flagSwitcher).icon = nothing
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.MinesLeft).title = "$minesString $totalMines"
        val nothing = ContextCompat.getDrawable(this, R.drawable.tile_flag_off500)
        val flag = ContextCompat.getDrawable(this, R.drawable.tile_flag500)
        menu.findItem(R.id.flagSwitcher).icon = if (flagSwitch) flag else nothing
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.restartItem -> {
                if (!gameEnds) {
                    val dialog = RestartDialogFragment(menu)
                    val manager = supportFragmentManager
                    dialog.show(manager, "restart")
                } else {
                    restart()
                    onPrepareOptionsMenu(menu)
                }
            }
            R.id.flagSwitcher -> {
                flagSwitch = !flagSwitch
                onPrepareOptionsMenu(menu)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
