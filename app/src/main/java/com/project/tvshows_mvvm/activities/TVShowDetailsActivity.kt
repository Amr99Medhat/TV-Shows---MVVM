package com.project.tvshows_mvvm.activities

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.tvshows_mvvm.R
import com.project.tvshows_mvvm.adapters.EpisodesAdapter
import com.project.tvshows_mvvm.adapters.ImageSliderAdapter
import com.project.tvshows_mvvm.databinding.ActivityTvshowDetailsBinding
import com.project.tvshows_mvvm.databinding.LayoutEpisodesBottomSheetBinding
import com.project.tvshows_mvvm.models.TVShow
import com.project.tvshows_mvvm.utlities.TempDataHolder
import com.project.tvshows_mvvm.viewModels.TVShowDetailsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class TVShowDetailsActivity : AppCompatActivity() {
    private lateinit var activityTvShowDetailsBinding: ActivityTvshowDetailsBinding
    private lateinit var tvShowDetailsViewModel: TVShowDetailsViewModel
    private var episodesBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var layoutEpisodesBottomSheetBinding: LayoutEpisodesBottomSheetBinding
    private var tvShow: TVShow? = null
    private var isTVShowAvailableInWatchlist = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTvShowDetailsBinding = ActivityTvshowDetailsBinding.inflate(layoutInflater)
        setContentView(activityTvShowDetailsBinding.root)
        doInitialization()
    }

    private fun doInitialization() {
        tvShowDetailsViewModel =
            ViewModelProvider(this).get(TVShowDetailsViewModel(application)::class.java)
        activityTvShowDetailsBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        tvShow = intent.getSerializableExtra("tvShow") as TVShow?
        checkTVShowInWatchlist()
        getTVShowDetails()
    }

    private fun checkTVShowInWatchlist() {
        val compositeDisposableForGetTVShowFromWatchlist = CompositeDisposable()
        compositeDisposableForGetTVShowFromWatchlist.add(tvShowDetailsViewModel.getTVShowFromWatchlist(
            tvShow?.id.toString()
        )
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isTVShowAvailableInWatchlist = true
                activityTvShowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added)
                compositeDisposableForGetTVShowFromWatchlist.dispose()
            })
    }

    private fun getTVShowDetails() {
        activityTvShowDetailsBinding.isLoading = true
        val tvShowId = tvShow!!.id
        tvShowDetailsViewModel.getTVShowDetails(tvShowId.toString())
            .observe(this, { tvShowDetailsResponse ->
                activityTvShowDetailsBinding.isLoading = false
                if (tvShowDetailsResponse.tvShowDetails != null) {
                    if (tvShowDetailsResponse.tvShowDetails.pictures != null) {
                        loadingSliderImages(tvShowDetailsResponse.tvShowDetails.pictures)
                    }
                    activityTvShowDetailsBinding.tvShowImageURL =
                        tvShowDetailsResponse.tvShowDetails.imagePath
                    activityTvShowDetailsBinding.imageTVShow.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.description = HtmlCompat.fromHtml(
                        tvShowDetailsResponse.tvShowDetails.description!!,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString()
                    activityTvShowDetailsBinding.textDescription.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.textReadMore.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.textReadMore.setOnClickListener {
                        if (activityTvShowDetailsBinding.textReadMore.text.toString() == "Read More") {
                            activityTvShowDetailsBinding.textDescription.maxLines = Int.MAX_VALUE
                            activityTvShowDetailsBinding.textDescription.ellipsize = null
                            activityTvShowDetailsBinding.textReadMore.text =
                                getString(R.string.read_less)
                        } else {
                            activityTvShowDetailsBinding.textDescription.maxLines = 4
                            activityTvShowDetailsBinding.textDescription.ellipsize =
                                TextUtils.TruncateAt.END
                            activityTvShowDetailsBinding.textReadMore.text =
                                getString(R.string.read_more)

                        }
                    }
                    activityTvShowDetailsBinding.rating = String.format(
                        Locale.getDefault(),
                        "%.2f",
                        tvShowDetailsResponse.tvShowDetails.rating!!.toDouble()
                    )
                    if (tvShowDetailsResponse.tvShowDetails.genres != null) {
                        activityTvShowDetailsBinding.genre =
                            tvShowDetailsResponse.tvShowDetails.genres[0]
                    } else {
                        activityTvShowDetailsBinding.genre = "N/A"
                    }
                    activityTvShowDetailsBinding.runtime =
                        tvShowDetailsResponse.tvShowDetails.runtime + " Min"
                    activityTvShowDetailsBinding.viewDivider1.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.layoutMisc.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.viewDivider2.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.buttonWebsite.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(tvShowDetailsResponse.tvShowDetails.url)
                        startActivity(intent)
                    }
                    activityTvShowDetailsBinding.buttonWebsite.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.buttonEpisodes.visibility = View.VISIBLE
                    activityTvShowDetailsBinding.buttonEpisodes.setOnClickListener {
                        if (episodesBottomSheetDialog == null) {
                            episodesBottomSheetDialog = BottomSheetDialog(this)
                            layoutEpisodesBottomSheetBinding =
                                LayoutEpisodesBottomSheetBinding.inflate(
                                    LayoutInflater.from(layoutInflater.context),
                                    null,
                                    false
                                )
                            episodesBottomSheetDialog!!.setContentView(
                                layoutEpisodesBottomSheetBinding.root
                            )
                            layoutEpisodesBottomSheetBinding.episodesRecyclerView.adapter =
                                EpisodesAdapter(tvShowDetailsResponse.tvShowDetails.episodes!!)
                            layoutEpisodesBottomSheetBinding.textTitle.text =
                                String.format("Episode | %s", tvShow!!.name)
                            layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener {
                                episodesBottomSheetDialog!!.dismiss()
                            }

                            //bottom sheet style
                            val frameLayout: FrameLayout? =
                                episodesBottomSheetDialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                            if (frameLayout != null) {
                                val bottomSheetBehavior: BottomSheetBehavior<View> =
                                    BottomSheetBehavior.from(frameLayout)
                                bottomSheetBehavior.peekHeight =
                                    Resources.getSystem().displayMetrics.heightPixels
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                        episodesBottomSheetDialog!!.show()
                    }

                    activityTvShowDetailsBinding.imageWatchlist.setOnClickListener {
                        val compositeDisposableForAddToWatchlist = CompositeDisposable()

                        if (isTVShowAvailableInWatchlist) {
                            compositeDisposableForAddToWatchlist.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(
                                tvShow!!
                            )
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    isTVShowAvailableInWatchlist = false
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true
                                    activityTvShowDetailsBinding.imageWatchlist.setImageResource(
                                        R.drawable.ic_watch_list
                                    )
                                    Toast.makeText(
                                        applicationContext,
                                        "Removed from watchlist",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    compositeDisposableForAddToWatchlist.dispose()
                                })

                        } else {
                            compositeDisposableForAddToWatchlist.add(tvShowDetailsViewModel.addToWatchlist(
                                tvShow!!
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true
                                    activityTvShowDetailsBinding.imageWatchlist.setImageResource(
                                        R.drawable.ic_added
                                    )
                                    Toast.makeText(
                                        applicationContext,
                                        "Added to watchlist",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    compositeDisposableForAddToWatchlist.dispose()
                                })
                        }
                    }
                    activityTvShowDetailsBinding.imageWatchlist.visibility = View.VISIBLE
                    loadBasicTVShowDetails()
                }
            })
    }

    private fun loadingSliderImages(sliderImages: Array<String>) {
        activityTvShowDetailsBinding.sliderViewPager.offscreenPageLimit = 1
        activityTvShowDetailsBinding.sliderViewPager.adapter = ImageSliderAdapter(sliderImages)
        activityTvShowDetailsBinding.sliderViewPager.visibility = View.VISIBLE
        activityTvShowDetailsBinding.viewFadingEdge.visibility = View.VISIBLE
        setupSliderIndicators(sliderImages.size)
        activityTvShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentSliderIndicator(position)
            }
        })
    }

    private fun setupSliderIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)

        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.backgroud_slider_indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            activityTvShowDetailsBinding.layoutSliderIndicators.addView(indicators[i])
        }
        activityTvShowDetailsBinding.layoutSliderIndicators.visibility = View.VISIBLE
        setCurrentSliderIndicator(0)
    }

    private fun setCurrentSliderIndicator(position: Int) {
        val childCount = activityTvShowDetailsBinding.layoutSliderIndicators.childCount

        for (i in 0 until childCount) {
            val imageView: ImageView =
                activityTvShowDetailsBinding.layoutSliderIndicators.getChildAt(i) as ImageView

            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backgroud_slider_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.backgroud_slider_indicator_inactive
                    )
                )
            }
        }
    }

    private fun loadBasicTVShowDetails() {
        activityTvShowDetailsBinding.tvShowName = tvShow!!.name
        activityTvShowDetailsBinding.networkCountry =
            tvShow!!.network + " (" + tvShow!!.country + ")"
        activityTvShowDetailsBinding.status = tvShow!!.status
        activityTvShowDetailsBinding.startedDate = tvShow!!.startDate
        activityTvShowDetailsBinding.textName.visibility = View.VISIBLE
        activityTvShowDetailsBinding.textNetworkCountry.visibility = View.VISIBLE
        activityTvShowDetailsBinding.textStatus.visibility = View.VISIBLE
        activityTvShowDetailsBinding.textStarted.visibility = View.VISIBLE

    }

}






