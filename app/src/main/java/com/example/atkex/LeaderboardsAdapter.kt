package com.example.atkex

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class LeaderboardsAdapter (private val imageModelArrayList: MutableList<LeaderboardsModel>) : RecyclerView.Adapter<LeaderboardsAdapter.ViewHolder>() {
    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout_leaderboards, parent, false)

        return ViewHolder(v)
    }

    /*
     * Bind the data to the child views of the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]

        holder.txtName.text = "Player: " + info.getNames()
        holder.txtPoints.text = "Points: " + info.getPoints()
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

        var txtName = itemView.findViewById<View>(R.id.txtPlayerName) as TextView
        var txtPoints = itemView.findViewById<View>(R.id.txtPoints) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val mess =
                txtName.text.toString()
            val snackbar = Snackbar.make(v, mess, Snackbar.LENGTH_LONG)
            snackbar.show()

//            /** Intent of which poi to go to */
//            val intent = Intent(v.getContext(), PointOfInterestActivity::class.java)
//            intent.putExtra("name", txtTitle.text)
//            intent.putExtra("distance", txtDistance.text)
//            //intent.putExtra("image", )
//
//            imgView.buildDrawingCache()
//            val bitmap: Bitmap = imgView.getDrawingCache()
//            intent.putExtra("BitmapImage", bitmap)
//
//            v.getContext().startActivity(intent)
        }
    }
}