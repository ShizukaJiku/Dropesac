package com.dropesac.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dropesac.app.R


class WelcomeFragment(private val username: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val welcomeText = view.findViewById<TextView>(R.id.welcome_text)

        welcomeText.text = "Bienvenido, $username".uppercase()

        val closeButton = view.findViewById<Button>(R.id.close_button)

        closeButton.setOnClickListener {
            dismiss()
        }
    }
}