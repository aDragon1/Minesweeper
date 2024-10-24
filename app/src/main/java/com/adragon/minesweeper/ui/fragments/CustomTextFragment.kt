package com.adragon.minesweeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adragon.minesweeper.R

class CustomTextFragment:Fragment() {
    lateinit var nameText:TextView
    lateinit var valueText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameText  = TextView(requireContext())
        valueText = EditText(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.set_custom_fragment,container,false)
        nameText  = rootView.findViewById(R.id.nameText)
        valueText = rootView.findViewById(R.id.valueText)
        return rootView
    }
}