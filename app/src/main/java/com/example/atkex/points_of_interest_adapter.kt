package com.example.atkex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class points_of_interest_adapter (private val imageModelArrayList: MutableList<points_of_interest_model>) : RecyclerView.Adapter<points_of_interest_adapter.ViewHolder>() {
    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)

        return ViewHolder(v)
    }

    /*
     * Bind the data to the child views of the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]

        holder.imgView.setImageResource(info.getImages())
        holder.txtTitle.text = info.getNames()
        holder.txtDistance.text = info.getDistances()
    }

    /*
     * Number of models in the array
     */
    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {

        var imgView = itemView.findViewById<View>(R.id.image) as ImageView
        var txtTitle = itemView.findViewById<View>(R.id.title) as TextView
        var txtDistance = itemView.findViewById<View>(R.id.distance) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val mess =
                txtTitle.text.toString()
            val snackbar = Snackbar.make(v, mess, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
}