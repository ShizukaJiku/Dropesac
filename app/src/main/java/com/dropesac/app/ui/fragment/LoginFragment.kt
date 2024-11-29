package com.dropesac.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dropesac.app.R
import com.dropesac.app.data.repository.LoginRepository

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton = view.findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.username).text.toString()
            val password = view.findViewById<EditText>(R.id.password).text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                val loginRepository = LoginRepository(requireContext(), "https://adratech.app/dropesac/login.php")

                loginRepository.login(username, password,
                    { isSuccess ->
                        if (isSuccess) {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, MedicamentListFragment(username))
                                .addToBackStack(null)
                                .commit()

                        } else {
                            Toast.makeText(requireContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(requireContext(), "Error en la conexión: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        return view
    }
}