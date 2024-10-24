package com.adragon.minesweeper.data.model

import android.content.Context
import android.widget.Toast
import com.adragon.minesweeper.ui.activities.col
import com.adragon.minesweeper.ui.activities.gameEnds
import com.adragon.minesweeper.ui.activities.grid
import com.adragon.minesweeper.ui.activities.row
import com.adragon.minesweeper.ui.activities.totalMines
import java.util.*

class Cell(private val i: Int, private val j:Int){
    var revealed = false
    var isMine = false
    var flagged = false
    var neighbors = 0


    fun countNeighbors():Int{
        if (isMine)
            return -1
        var count = 0
        for (i in -1..1)
            for (j in -1..1)
                if (checkEdges(this.i+i,this.j+j) && grid[(this.i + i)* col +(this.j + j)].isMine)
                    count++
        return count
    }

    private fun floodFill() {
        for (ioff in -1..1) {
            for (joff in -1..1) {
                val i = this.i + ioff
                val j = this.j + joff
                if (checkEdges(i, j)) {
                    val neighbor = grid[i * col + j]
                    if (!neighbor.isMine && !neighbor.revealed && !neighbor.flagged)
                        neighbor.reveal()
                }
            }
        }
    }

    fun reveal() {
        if (!this.flagged) {
            this.revealed = true
            if (this.neighbors == 0)
                this.floodFill()
        }
    }

     fun revealOnNumClick(context: Context){
        var count = 0 // Count flagged mines in my 8 neighbors
        for (ioff in -1..1) {
            for (joff in -1..1) {
                val i = this.i + ioff
                val j = this.j + joff
                if (checkEdges(i,j)
                    && grid[i* col +j].flagged)
                    count++
            }
        }
        if (count == this.neighbors){
            for (ioff in -1..1) {
                for (joff in -1..1) {
                    val i = this.i + ioff
                    val j = this.j + joff
                    if (checkEdges(i,j) &&
                         !grid[i* col +j].isMine && !grid[i* col +j].revealed) {
                        if (grid[i * col + j].flagged)
                            grid[i * col + j].flagged = false
                        grid[i * col + j].reveal()
                    }else
                        if (checkEdges(i,j) && grid[i* col +j].isMine && !grid[i* col +j].flagged)
                            gameOver(context)
                }
            }
        }
    }
}
 fun setMines() {
    val options = ArrayList<Pair<Int, Int>>()

//    Fill array of every index pair
    for (i in 0 until row) {
        for (j in 0 until col) {
            options.add(i to j)
        }
    }

// pick random indices from option
    for (n in 0 until totalMines) {
        val random = Random()
        val index = random.nextInt(options.size - 1)
        val element = options[index]
        val i = element.first
        val j = element.second
        grid[i * col + j].isMine = true
        options.removeAt(index)
    }
}

 fun setNeighbors() {
    for (i in 0 until row) {
        for (j in 0 until col) {
            grid[i * col + j].neighbors = grid[i * col + j].countNeighbors()
        }
    }
}

 fun winStuff(context:Context) {
    for (i in 0 until row) {
        for (j in 0 until col) {
            if (grid[i * col + j].isMine && !grid[i * col + j].flagged) {
                grid[i * col + j].flagged = true
            } else
                grid[i * col + j].reveal()
        }
    }
    gameEnds = true
    Toast.makeText(context, "Congrats! You win", Toast.LENGTH_SHORT).show()
}

 fun checkWin(): Boolean {
    var flagCount = 0
    var rslt = true
    for (i in 0 until row) {
        for (j in 0 until col) {
            if  (grid[i* col + j].flagged &&   grid[i * col + j].isMine) flagCount++
            if (!grid[i* col + j].revealed && !grid[i * col + j].isMine) {
                rslt = false
            }
        }
    }
    return (flagCount == totalMines) || rslt
}

 fun gameOver(context:Context) {
    for (i in 0 until row) {
        for (j in 0 until col) {
            if (grid[i * col + j].isMine && !grid[i * col + j].flagged) {
                grid[i * col + j].reveal()
            }
        }
    }
    gameEnds = true
    Toast.makeText(context,"Sorry, your game is over ", Toast.LENGTH_SHORT).show()
}

fun checkEdges(i:Int, j:Int):Boolean = (i > -1 && j > -1 && i < row && j < col)