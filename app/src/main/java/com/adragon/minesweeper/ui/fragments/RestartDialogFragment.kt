package com.adragon.minesweeper.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.DialogFragment
import com.adragon.minesweeper.ui.activities.MainActivity


class RestartDialogFragment(var menu: Menu):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            with(builder) {
                setTitle("")
                setMessage("Restart?")
                setPositiveButton("Yes") { dialog, _ ->
                    val mainActivity: MainActivity = activity as MainActivity
                    mainActivity.restart()
                    mainActivity.onPrepareOptionsMenu(menu)
                    dialog.cancel()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                builder.create()
            }
        }!!
    }
}