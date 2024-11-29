package com.dropesac.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dropesac.app.R
import com.dropesac.app.data.model.CartItem
import com.dropesac.app.data.repository.CartRepository
import com.dropesac.app.ui.adapter.CartAdapter

class CartFragment : Fragment() {

    private lateinit var cartRepository: CartRepository
    private lateinit var cartAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var clearCartButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        totalTextView = view.findViewById(R.id.totalTextView)
        clearCartButton = view.findViewById(R.id.clearCartButton)

        cartRepository = CartRepository(requireContext())

        loadCartItems()

        clearCartButton.setOnClickListener {
            clearCart()
        }

        return view
    }

    private fun loadCartItems() {
        cartRepository.getCartItems(
            onResponse = { cartItems ->
                cartAdapter = CartAdapter(cartItems)
                recyclerView.adapter = cartAdapter
                updateTotal(cartItems)
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateTotal(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.getSubtotal() }
        totalTextView.text = "Total: $${"%.2f".format(total)}"
    }

    private fun clearCart() {
        // Aquí agregas la lógica para vaciar el carrito, por ejemplo llamando a otro endpoint si es necesario.
        Toast.makeText(requireContext(), "Carrito vaciado", Toast.LENGTH_SHORT).show()
    }
}
