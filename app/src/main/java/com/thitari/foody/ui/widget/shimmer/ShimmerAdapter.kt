package com.thitari.foody.ui.widget.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.thitari.foody.R

class ShimmerAdapter(
    @LayoutRes
    private var layout: Int,
    private var itemCount: Int,
    private var layoutManagerType: Int,
    private var itemViewType: ItemViewType?,
    private var shimmer: Shimmer?,
    @RecyclerView.Orientation
    private var layoutOrientation: Int
) : RecyclerView.Adapter<ShimmerViewHolder>() {
    /**
     * A contract to change shimmer view type.
     */
    interface ItemViewType {
        @LayoutRes
        fun getItemViewType(layoutManagerType: Int, position: Int): Int
    }

    init {
        this.itemCount = validateCount(itemCount)
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemViewType != null) itemViewType!!.getItemViewType(
            layoutManagerType,
            position
        ) else layout /* default */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        /* inflate view holder layout and then attach provided view in it. */
        val view: View = inflater.inflate(
            R.layout.recyclerview_shimmer_viewholder_layout,
            parent, false
        )
        if (layoutOrientation == RecyclerView.HORIZONTAL) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        return ShimmerViewHolder(
            (inflater
                .inflate(
                    viewType, view as ShimmerFrameLayout,
                    true /* attach to view holder layout */
                ) as ShimmerFrameLayout)
        )
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        holder.bindView(shimmer!!)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal APIs
    ///////////////////////////////////////////////////////////////////////////
    fun setShimmer(shimmer: Shimmer?) {
        this.shimmer = shimmer
    }

    fun setLayout(@LayoutRes layout: Int) {
        this.layout = layout
    }

    fun setCount(count: Int) {
        itemCount = validateCount(count)
    }

    fun setShimmerItemViewType(layoutManagerType: Int, itemViewType: ItemViewType?) {
        this.layoutManagerType = layoutManagerType
        this.itemViewType = itemViewType
    }

    /**
     * Validates if provided item count is greater than reasonable number
     * of items and returns max number of items allowed.
     *
     *
     * Try to save memory produced by shimmer layouts.
     *
     * @param count input number.
     * @return valid count number.
     */
    private fun validateCount(count: Int): Int {
        return if (count < 20) count else 20
    }
}