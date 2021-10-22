package com.thitari.foody.ui.widget.shimmer

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.Shimmer.AlphaHighlightBuilder
import com.facebook.shimmer.Shimmer.ColorHighlightBuilder
import com.thitari.foody.R

class ShimmerRecyclerView : RecyclerView {

    companion object {
        const val LAYOUT_GRID = 1
        const val LAYOUT_LIST = 0
    }

    private var shimmerAdapter: ShimmerAdapter? = null

    private var actualAdapter: Adapter<*>? = null

    private var shimmerLayoutManager: LayoutManager? = null

    private var internalLayoutManager: LayoutManager? = null

    private var isShimmerShowing = false

    @Orientation
    private var layoutOrientation = VERTICAL

    private var layoutReverse = false

    private var gridSpanCount = -1

    @LayoutRes
    private var shimmerLayout = 0

    private var shimmerItemCount = 9

    private var layoutType = LAYOUT_LIST

    private var itemViewType: ShimmerAdapter.ItemViewType? = null

    private var shimmer: Shimmer? = null

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {

        initialize(context, attrs)
    }

     constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        initialize(context, attrs)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Overridden methods
    ///////////////////////////////////////////////////////////////////////////
    @CallSuper
    override fun setLayoutManager(manager: LayoutManager?) {
        if (manager == null) {
            internalLayoutManager = null
        } else if (manager !== shimmerLayoutManager) {
            if (manager is GridLayoutManager) {
                gridSpanCount = manager.spanCount
            } else if (manager is LinearLayoutManager) {
                gridSpanCount = -1
                layoutReverse = manager.reverseLayout
                layoutOrientation = manager.orientation
            }
            internalLayoutManager = manager
        }
        initializeLayoutManager()
        invalidateShimmerAdapter()
        super.setLayoutManager(manager)
    }

    @CallSuper
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) {
            actualAdapter = null
        } else if (adapter !== shimmerAdapter) {
            actualAdapter = adapter
        }
        super.setAdapter(adapter)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public APIs
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Public APIs
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Start showing shimmer loading.
     */
    fun showShimmer() {
        if (shimmerLayoutManager == null) {
            initializeLayoutManager()
        }
        layoutManager = shimmerLayoutManager
        invalidateShimmerAdapter()
        setAdapter(getShimmerAdapter())
        isShimmerShowing = true
    }

    /**
     * Stop showing shimmer loading and setup
     */
    fun hideShimmer() {
        layoutManager = layoutManager
        adapter = getActualAdapter()
        isShimmerShowing = false
    }

    /**
     * Similar to setting [.setShimmerLayout]
     * and then [.setLayoutManager].
     *
     * @param manager       linear or grid layout manager.
     * @param shimmerLayout shimmer layout
     */
    fun setLayoutManager(manager: LayoutManager?, @LayoutRes shimmerLayout: Int) {
        setShimmerLayout(shimmerLayout)
        layoutManager = manager
    }

    fun isShimmerShowing(): Boolean {
        return isShimmerShowing
    }

    /**
     * @param layout layout reference for shimmer adapter.
     */
    fun setShimmerLayout(@LayoutRes layout: Int) {
        shimmerLayout = layout
    }

    /**
     * @return layout reference used as shimmer layout.
     */
    @LayoutRes
    fun getShimmerLayout(): Int {
        return shimmerLayout
    }

    /**
     * @param count Number of items to be shown in shimmer adapter.
     */
    fun setShimmerItemCount(count: Int) {
        shimmerItemCount = count
    }

    /**
     * @return number of items shown in shimmer adapter.
     */
    fun getShimmerItemCount(): Int {
        return shimmerItemCount
    }

    /**
     * @param manager Shimmer [androidx.recyclerview.widget.RecyclerView.LayoutManager]
     */
    fun setShimmerLayoutManager(manager: LayoutManager) {
        shimmerLayoutManager = manager
    }

    /**
     * @return [androidx.recyclerview.widget.RecyclerView.LayoutManager] used for shimmer adapter.
     */
    fun getShimmerLayoutManager(): LayoutManager? {
        return shimmerLayoutManager
    }

    /**
     * Setting [Shimmer] programmatically will ignore all xml properties.
     *
     * @param shimmer other required Shimmer properties.
     */
    fun setShimmer(shimmer: Shimmer?) {
        this.shimmer = shimmer
    }

    /**
     * @return current [Shimmer]
     */
    fun getShimmer(): Shimmer? {
        return shimmer
    }

    /**
     * @return Shimmer adapter
     */
    fun getShimmerAdapter(): ShimmerAdapter {
        if (shimmerAdapter == null) shimmerAdapter = ShimmerAdapter(
            shimmerLayout, shimmerItemCount, layoutType,
            itemViewType, shimmer, layoutOrientation
        )
        return shimmerAdapter!!
    }

    /**
     * @return Actual adapter
     */
    fun getActualAdapter(): Adapter<*>? {
        return actualAdapter
    }

    /**
     * Setup loading shimmer view type.
     *
     * @param itemViewType a contract with [ShimmerAdapter].
     */
    fun setItemViewType(itemViewType: ShimmerAdapter.ItemViewType?) {
        this.itemViewType = itemViewType
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal APIs
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Initialize Shimmer adapter based on provided shimmer settings.
     *
     * @param context application context.
     * @param attrs   nullable attribute set.
     */
    private fun initialize(context: Context, attrs: AttributeSet?) {
        if (shimmer == null) shimmer = getDefaultSettings(context, attrs)
    }

    /**
     * Reset layout, count and shimmer settings
     * in adapter and invalidate data.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun invalidateShimmerAdapter() {
        getShimmerAdapter() // just to initialize, if it is null.
        shimmerAdapter?.setLayout(shimmerLayout)
        shimmerAdapter?.setCount(shimmerItemCount)
        shimmerAdapter?.setShimmerItemViewType(layoutType, itemViewType)
        shimmerAdapter?.setShimmer(shimmer)
        shimmerAdapter?.notifyDataSetChanged()
    }

    /**
     * If no shimmer layout is provided, use default layout
     * depending on layout manager.
     *
     * @param isGrid is GridLayoutManager attached to RecyclerView.
     */
    private fun tryAssigningDefaultLayout(isGrid: Boolean) {
        if (shimmerLayout == 0 || shimmerLayout == R.layout.recyclerview_shimmer_item_grid || shimmerLayout == R.layout.recyclerview_shimmer_item_list) shimmerLayout =
            if (isGrid) R.layout.recyclerview_shimmer_item_grid else R.layout.recyclerview_shimmer_item_list
    }

    /**
     * Based on actual layout manager,
     * prepare shimmer layout manager.
     */
    private fun initializeLayoutManager() {
        shimmerLayoutManager = if (gridSpanCount >= 0) {
            object : GridLayoutManager(
                context, gridSpanCount,
                layoutOrientation, layoutReverse
            ) {
                override fun canScrollVertically(): Boolean {
                    return !isShimmerShowing
                }

                override fun canScrollHorizontally(): Boolean {
                    return !isShimmerShowing
                }
            }
        } else {
            object : LinearLayoutManager(
                context,
                layoutOrientation, layoutReverse
            ) {
                override fun canScrollVertically(): Boolean {
                    return !isShimmerShowing
                }

                override fun canScrollHorizontally(): Boolean {
                    return !isShimmerShowing
                }
            }
        }
        val isGridLayoutManager = shimmerLayoutManager is GridLayoutManager
        layoutType = if (isGridLayoutManager) LAYOUT_GRID else LAYOUT_LIST
        tryAssigningDefaultLayout(isGridLayoutManager)
    }

    /**
     * Extract xml attributes from [ShimmerRecyclerView].
     *
     * @param context [android.app.Activity] context...
     * @param attrs   view attributes
     * @return default [Shimmer] built-up considering xml attributes.
     */
    private fun getDefaultSettings(context: Context, attrs: AttributeSet?): Shimmer? {
        if (attrs == null) return AlphaHighlightBuilder() /* alter default values */
            .setBaseAlpha(1f)
            .setHighlightAlpha(0.3f)
            .setTilt(25f)
            .build()
        val a: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ShimmerRecyclerView, 0, 0
        )
        return try {
            val builder = if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_colored)
                && a.getBoolean(R.styleable.ShimmerRecyclerView_shimmer_recycler_colored, false)
            ) ColorHighlightBuilder() else AlphaHighlightBuilder()

            // layout reference
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_layout)) {
                setShimmerLayout(
                    a.getResourceId(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_layout, shimmerLayout
                    )
                )
            }

            // shimmer item count
            setShimmerItemCount(
                a.getInteger(
                    R.styleable.ShimmerRecyclerView_shimmer_recycler_item_count, shimmerItemCount
                )
            )
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_clip_to_children)) {
                builder.setClipToChildren(
                    a.getBoolean(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_clip_to_children, true
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_auto_start)) {
                builder.setAutoStart(
                    a.getBoolean(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_auto_start, true
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_base_color)
                && builder is ColorHighlightBuilder
            ) {
                builder.setBaseColor(
                    a.getColor(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_base_color, 0x4cffffff
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_highlight_color)
                && builder is ColorHighlightBuilder
            ) {
                builder.setHighlightColor(
                    a.getColor(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_highlight_color,
                        Color.WHITE
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_base_alpha)) {
                builder.setBaseAlpha(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_base_alpha, 1f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_highlight_alpha)) {
                builder.setHighlightAlpha(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_highlight_alpha, 0.3f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_duration)) {
                builder.setDuration(
                    a.getInteger(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_duration, 1000
                    ).toLong()
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_count)) {
                builder.setRepeatCount(
                    a.getInt(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_count,
                        ValueAnimator.INFINITE
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_delay)) {
                builder.setRepeatDelay(
                    a.getInt(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_delay, 0
                    ).toLong()
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_mode)) {
                builder.setRepeatMode(
                    a.getInt(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_repeat_mode,
                        ValueAnimator.RESTART
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_direction)) {
                val direction = a.getInt(
                    R.styleable.ShimmerRecyclerView_shimmer_recycler_direction,
                    Shimmer.Direction.LEFT_TO_RIGHT
                )
                when (direction) {
                    Shimmer.Direction.LEFT_TO_RIGHT -> builder.setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    Shimmer.Direction.TOP_TO_BOTTOM -> builder.setDirection(Shimmer.Direction.TOP_TO_BOTTOM)
                    Shimmer.Direction.RIGHT_TO_LEFT -> builder.setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
                    Shimmer.Direction.BOTTOM_TO_TOP -> builder.setDirection(Shimmer.Direction.BOTTOM_TO_TOP)
                    else -> builder.setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                }
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_shape)) {
                val shape =
                    a.getInt(R.styleable.ShimmerRecyclerView_shimmer_recycler_shape, Shimmer.Shape.LINEAR)
                when (shape) {
                    Shimmer.Shape.LINEAR -> builder.setShape(Shimmer.Shape.LINEAR)
                    Shimmer.Shape.RADIAL -> builder.setShape(Shimmer.Shape.RADIAL)
                    else -> builder.setShape(Shimmer.Shape.LINEAR)
                }
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_fixed_width)) {
                builder.setFixedWidth(
                    a.getDimensionPixelSize(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_fixed_width, 0
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_fixed_height)) {
                builder.setFixedHeight(
                    a.getDimensionPixelSize(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_fixed_height, 0
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_width_ratio)) {
                builder.setWidthRatio(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_width_ratio, 1f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_height_ratio)) {
                builder.setHeightRatio(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_height_ratio, 1f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_intensity)) {
                builder.setIntensity(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_intensity, 0f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_dropoff)) {
                builder.setDropoff(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_dropoff, 0.5f
                    )
                )
            }
            if (a.hasValue(R.styleable.ShimmerRecyclerView_shimmer_recycler_tilt)) {
                builder.setTilt(
                    a.getFloat(
                        R.styleable.ShimmerRecyclerView_shimmer_recycler_tilt, 25f
                    )
                )
            }
            builder.build()
        } finally {
            a.recycle()
        }
    }

}