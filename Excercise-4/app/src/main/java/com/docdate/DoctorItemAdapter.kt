package com.docdate

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.docdate.R
import com.docdate.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class DoctorItemAdapter(
    private val doctors: List<User>
): RecyclerView.Adapter<DoctorItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.name)
        val tvEmail: TextView = view.findViewById(R.id.email)
        val tvWebsite: TextView = view.findViewById(R.id.website)
        val tvPhone: TextView = view.findViewById(R.id.phone)
        val tvSpecialization: TextView = view.findViewById(R.id.specialization)
        val imageView: ImageView = view.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = doctors[position]
        holder.tvName.text = item.firstName + " " + item.lastName
        holder.tvEmail.text = item.email
        holder.tvWebsite.text = item.website
        holder.tvPhone.text = item.phone
        holder.tvSpecialization.text = item.specialisation
        if ( item.uri != "" ) {
            Picasso.with(holder.imageView.context)
                .load(item.uri)
                .into(holder.imageView)
        }

    }

    override fun getItemCount() = doctors.size
}