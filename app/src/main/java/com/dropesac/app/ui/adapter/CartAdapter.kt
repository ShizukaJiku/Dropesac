package com.dropesac.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dropesac.app.R
import com.dropesac.app.data.model.CartItem
import com.bumptech.glide.Glide

class CartAdapter(private val cartItems: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.productName.text = cartItem.productName
        holder.productDescription.text = cartItem.productDescription
        holder.productPrice.text = "$${cartItem.productPrice}"
        holder.productQuantity.text = "Cantidad: ${cartItem.quantity}"
        holder.subtotal.text = "Subtotal: $${cartItem.getSubtotal()}"

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(cartItem.productImageUrl)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        val subtotal: TextView = itemView.findViewById(R.id.subtotal)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
    }
}
