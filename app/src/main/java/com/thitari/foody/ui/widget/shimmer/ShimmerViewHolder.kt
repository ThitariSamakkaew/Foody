package com.thitari.foody.ui.widget.shimmer

import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerViewHolder(
    private val shimmerLayout: ShimmerFrameLayout
) : RecyclerView.ViewHolder(shimmerLayout) {

    fun bindView(shimmer: Shimmer) {
        shimmerLayout.setShimmer(shimmer).startShimmer()
    }
}