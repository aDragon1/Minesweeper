package com.adragon.minesweeper.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton

class CellAdapter(private var buttons: ArrayList<ImageButton>): BaseAdapter() {
    override fun getCount(): Int {
        return buttons.size
    }
    override fun getItem(position: Int): Any {
        return buttons[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView ?: buttons[position]
    }
}