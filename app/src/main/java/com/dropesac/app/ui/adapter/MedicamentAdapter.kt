package com.dropesac.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dropesac.app.data.model.Product
import com.dropesac.app.data.model.CartItem
import com.dropesac.app.data.repository.CartRepository
import com.dropesac.app.databinding.MedicamentItemBinding

class MedicamentAdapter(
    private var products: List<Product>,
    private val context: android.content.Context
) : RecyclerView.Adapter<MedicamentAdapter.ViewHolder>() {

    private val cartRepository = CartRepository(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MedicamentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = products[position]

        holder.productName.text = item.name
        holder.productShortDescription.text = item.description
        holder.productPrice.text = "Precio: S/${item.price}"

        Glide.with(holder.productImage.context)
            .load(item.image_url)
            .into(holder.productImage)

        holder.expandButton.setOnClickListener {
            if (holder.expandedView.visibility == LinearLayout.GONE) {
                holder.expandedView.visibility = LinearLayout.VISIBLE
                holder.expandButton.text = "-"
            } else {
                holder.expandedView.visibility = LinearLayout.GONE
                holder.expandButton.text = "+"
            }
        }

        holder.quantityIncrease.setOnClickListener {
            val currentQuantity = holder.quantityText.text.toString().toInt()
            holder.quantityText.text = (currentQuantity + 1).toString()
        }

        holder.quantityDecrease.setOnClickListener {
            val currentQuantity = holder.quantityText.text.toString().toInt()
            if (currentQuantity > 1) {
                holder.quantityText.text = (currentQuantity - 1).toString()
            }
        }

        holder.addToCartButton.setOnClickListener {
            val quantity = holder.quantityText.text.toString().toInt()
            val cartItem = CartItem(
                productId = item.id,
                productName = item.name,
                productDescription = item.description,
                productPrice = item.price,
                productImageUrl = item.image_url,
                quantity = quantity
            )
            addToCart(cartItem)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    private fun addToCart(cartItem: CartItem) {
        cartRepository.addToCart(cartItem) { success, message ->
            if (success) {
                Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al agregar el producto al carrito: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(binding: MedicamentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val productImage: ImageView = binding.productImage
        val productName: TextView = binding.productName
        val productShortDescription: TextView = binding.productShortDescription
        val expandButton: Button = binding.expandButton
        val expandedView: LinearLayout = binding.expandedView
        val productPrice: TextView = binding.productPrice
        val presentationOptions: LinearLayout = binding.presentationOptions
        val quantityIncrease: Button = binding.quantityIncrease
        val quantityDecrease: Button = binding.quantityDecrease
        val quantityText: TextView = binding.quantityText
        val addToCartButton: Button = binding.addToCartButton
    }
}
