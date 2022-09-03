package com.adragon.minesweeper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        setButtons()
        println("smt")
    }

    @SuppressLint("SetTextI18n")
    private fun setButtons() {
        val beginnerButton: Button = findViewById(R.id.beginnerButton)
        val mediumButton: Button = findViewById(R.id.mediumButton)
        val expertButton: Button = findViewById(R.id.expertButton)
        val customButton: Button = findViewById(R.id.customButton)

        val colFragment =
            supportFragmentManager.findFragmentById(R.id.colsFrag) as CustomTextFragment
        val rowFragment =
            supportFragmentManager.findFragmentById(R.id.rowsFrag) as CustomTextFragment
        val totalMinesFragment =
            supportFragmentManager.findFragmentById(R.id.totalMinesFrag) as CustomTextFragment

        colFragment.nameText.text        = "  Cols:       "
        rowFragment.nameText.text        = "  Rows:       "
        totalMinesFragment.nameText.text = "  Total mines:"

        val intent = Intent(this, MainActivity::class.java)
        beginnerButton.setOnClickListener {
            intent.putExtra("row", 9)
            intent.putExtra("col", 9)
            intent.putExtra("totalMines", 10)
            startActivity(intent)
        }
        mediumButton.setOnClickListener {
            intent.putExtra("row", 16)
            intent.putExtra("col", 16)
            intent.putExtra("totalMines", 40)
            startActivity(intent)
        }
        expertButton.setOnClickListener {
            intent.putExtra("row", 30)
            intent.putExtra("col", 16)
            intent.putExtra("totalMines", 99)
            startActivity(intent)
        }
        customButton.setOnClickListener {
            val col: String = colFragment.valueText.text.toString()
            val row:String = rowFragment.valueText.text.toString()
            val totalMines:String = totalMinesFragment.valueText.text.toString()
            if (col.isNotEmpty() && row.isNotEmpty() && totalMines.isNotEmpty()) {
              intent.putExtra("row", col)
              intent.putExtra("col", row)
              intent.putExtra("totalMines", totalMines)
              startActivity(intent)
            }
        }
    }
}