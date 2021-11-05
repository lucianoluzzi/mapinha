package nl.com.lucianoluzzi.mapinha.ui.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nl.com.lucianoluzzi.mapinha.R
import nl.com.lucianoluzzi.mapinha.databinding.ViewVenueItemBinding
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

class VenueListAdapter : ListAdapter<VenueModel, VenueListAdapter.VenueViewHolder>(venueDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ViewVenueItemBinding.inflate(layoutInflater)
        return VenueViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.setViews(getItem(position))
    }

    class VenueViewHolder(private val binding: ViewVenueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setViews(venueModel: VenueModel) = with(binding) {
            venueName.text = venueModel.name
            venueCategory.text = venueModel.category
            venueAddress.text = venueModel.addressModel.name
            venueModel.image?.let {
                setVenueCategoryImage(it)
            }
        }

        private fun setVenueCategoryImage(image: String) {
            Glide
                .with(binding.root)
                .load(image)
                .error(R.drawable.ic_store)
                .into(binding.image)
        }
    }

    private companion object {
        val venueDiffUtil = object : DiffUtil.ItemCallback<VenueModel>() {
            override fun areItemsTheSame(oldItem: VenueModel, newItem: VenueModel): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: VenueModel, newItem: VenueModel): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.addressModel == newItem.addressModel &&
                        oldItem.category == newItem.category &&
                        oldItem.image == newItem.category
            }
        }
    }
}