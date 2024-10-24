package com.adragon.minesweeper.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adragon.minesweeper.R
import com.adragon.minesweeper.data.model.GameSettings
import com.adragon.minesweeper.ui.fragments.CustomTextFragment


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        setButtons()
    }

    @SuppressLint("SetTextI18n")
    private fun setButtons() {
        // get button view's
        val beginnerButton: Button = findViewById(R.id.beginnerButton)
        val mediumButton: Button = findViewById(R.id.mediumButton)
        val expertButton: Button = findViewById(R.id.expertButton)
        val customButton: Button = findViewById(R.id.customButton)

        // get fragment for customButton text field's
        val colFragment =
            supportFragmentManager.findFragmentById(R.id.colsFrag) as CustomTextFragment
        val rowFragment =
            supportFragmentManager.findFragmentById(R.id.rowsFrag) as CustomTextFragment
        val totalMinesFragment =
            supportFragmentManager.findFragmentById(R.id.totalMinesFrag) as CustomTextFragment

        // set fragment text
        colFragment.nameText.text = "  Cols:       "
        rowFragment.nameText.text = "  Rows:       "
        totalMinesFragment.nameText.text = "  Total mines:"

        // set base level difficult
        val beginner = GameSettings(9, 9, 10)
        val medium = GameSettings(16, 16, 40)
        val expert = GameSettings(30, 16, 99)

        // on click + intent
        val intent = Intent(this, MainActivity::class.java)
        beginnerButton.setOnClickListener {
            intent.putExtra("row", beginner.row)
            intent.putExtra("col", beginner.col)
            intent.putExtra("totalMines", beginner.totalMines)
            startActivity(intent)
        }
        mediumButton.setOnClickListener {
            intent.putExtra("row", medium.row)
            intent.putExtra("col", medium.col)
            intent.putExtra("totalMines", medium.totalMines)
            startActivity(intent)
        }
        expertButton.setOnClickListener {
            intent.putExtra("row", expert.row)
            intent.putExtra("col", expert.col)
            intent.putExtra("totalMines", expert.totalMines)
            startActivity(intent)
        }
        customButton.setOnClickListener {
            val col: String? = colFragment.valueText.text?.toString()
            val row: String? = rowFragment.valueText.text?.toString()
            val totalMines: String? = totalMinesFragment.valueText.text?.toString()
            if (col != null && row != null && totalMines != null) {

                intent.putExtra("row", col)
                intent.putExtra("col", row)
                intent.putExtra("totalMines", totalMines)
                startActivity(intent)
            } else
                Toast.makeText(this, "Введите все значение", Toast.LENGTH_SHORT).show()
        }
    }
}
