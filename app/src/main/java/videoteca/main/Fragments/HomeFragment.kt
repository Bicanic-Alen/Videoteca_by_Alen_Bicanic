package videoteca.main.Fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import videoteca.main.Adapters.SliderAdapters
import videoteca.main.Domain.SliderItems
import videoteca.main.R
import videoteca.main.gestioneAPI.TMDB_API_Manager
import java.util.Locale
import kotlin.math.abs


class HomeFragment : Fragment() {


    private lateinit var viewPager2: ViewPager2
    private lateinit var sliderHandler: Handler
    private lateinit var adapterTopRating: Adapter<*>
    private lateinit var adapterUpComming:RecyclerView.Adapter<*>
    private lateinit var adapterPopular:RecyclerView.Adapter<*>

    private val tmdbApiManager:TMDB_API_Manager = TMDB_API_Manager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = view.findViewById(R.id.viewPager2)
        sliderHandler = Handler()
        banners()
    }

    private fun banners(){
        val sliderItems = mutableListOf<SliderItems>()
        val currentLocale: Locale = Locale.getDefault()
        val languageTag: String = currentLocale.toLanguageTag()  //Restituisce il tag della lingua

        Log.d("HomeFragment","languageTag = $languageTag")

        sliderItems.add(SliderItems(R.drawable.img1))
        sliderItems.add(SliderItems(R.drawable.img2))
        sliderItems.add(SliderItems(R.drawable.img3))
        sliderItems.add(SliderItems(R.drawable.img4))

        viewPager2.setAdapter(SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false)
        viewPager2.setClipChildren(false)
        viewPager2.setOffscreenPageLimit(4)
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        val compositePageTransformer:CompositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = (1 - abs(position.toDouble())).toFloat()
            page.scaleY = 0.85f + r * 0.15f
        }

        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.setCurrentItem(1)
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
            }
        })
    }

    private val sliderRunnable = java.lang.Runnable { viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1) }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable,2000)
    }






}