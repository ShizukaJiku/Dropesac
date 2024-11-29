package com.dropesac.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dropesac.app.R
import com.dropesac.app.data.repository.ProductRepository
import com.dropesac.app.ui.adapter.MedicamentAdapter
import com.google.android.material.textfield.TextInputEditText

class MedicamentListFragment(private val username: String) : Fragment() {

    private var columnCount = 1
    private lateinit var medicamentAdapter: MedicamentAdapter
    private lateinit var searchInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_medicament_list, container, false)

        val adapterView = view.findViewById<RecyclerView>(R.id.medicament_list)

        if (adapterView is RecyclerView) {
            with(adapterView) {
                layoutManager = if (columnCount <= 1) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, columnCount)
                }

                medicamentAdapter = MedicamentAdapter(listOf(), requireContext())

                fetchProducts(" ") //Busqueda inicial

                adapter = medicamentAdapter
            }
        }

        val cartButton = view.findViewById<LinearLayout>(R.id.cartButton)

        cartButton.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, CartFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }


        // Referencia al campo de búsqueda
        searchInput = view.findViewById(R.id.search_input)

        // Detectar la acción "Enter"
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = searchInput.text.toString()

                if (query.isNotEmpty()) {
                    fetchProducts(query)
                }
                true
            } else {
                false
            }
        }

        WelcomeFragment(username).show(childFragmentManager, "WelcomeFragment")

        return view
    }

    private fun fetchProducts(query: String) {
        val productRepository = ProductRepository(requireContext())

        val queryToSearch = query.trim().ifEmpty { " " }

        productRepository.getByQuery(queryToSearch,
            { products ->
                if (products.isNullOrEmpty()) {
                    showNoResultsFound()
                    return@getByQuery
                }

                medicamentAdapter.updateList(products)
            },
            { error ->
                showError(error)
            }
        )
    }

    private fun showNoResultsFound() {
        Toast.makeText(requireContext(), "No se encontraron productos", Toast.LENGTH_SHORT).show()
    }

    private fun showError(error: Throwable) {
        Toast.makeText(requireContext(), "Hubo un error al realizar la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int, username: String) =
            MedicamentListFragment(username).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
