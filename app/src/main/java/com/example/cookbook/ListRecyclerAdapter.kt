package com.example.cookbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row.view.*

class ListRecyclerAdapter(val cookArray: ArrayList<String>, val idArray: ArrayList<Int>) : RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cookArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.recycler_row_textview.text = cookArray[position]
        holder.itemView.setOnClickListener {
            val action =  CookListFragmentDirections.actionCookListFragmentToAddCookFragment("cameFromRecycler", idArray[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

}