package videoteca.main.Fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import videoteca.main.Adapters.FilmAdapter
import videoteca.main.Adapters.GenreAdapter
import videoteca.main.Adapters.SliderAdapters
import videoteca.main.Domain.GenreList
import videoteca.main.Domain.SliderItems
import videoteca.main.R
import videoteca.main.Domain.Movie.MovieResponse
import videoteca.main.gestioneAPI.TMDB_Manager
import java.util.Locale
import kotlin.math.abs


class HomeFragment : Fragment() {


    private val tmdbApiManager:TMDB_Manager = TMDB_Manager()
    private lateinit var viewPager2: ViewPager2
    private lateinit var sliderHandler: Handler
    private lateinit var recyclerViewTopRated : RecyclerView
    private lateinit var recyclerViewPopular:RecyclerView
    private lateinit var recyclerViewUpComming: RecyclerView
    private lateinit var recyclerViewGenres: RecyclerView
    private lateinit var adapterTopRating: Adapter<*>
    private lateinit var adapterUpComming:RecyclerView.Adapter<*>
    private lateinit var adapterPopular:RecyclerView.Adapter<*>
    private lateinit var adapterGenres:RecyclerView.Adapter<*>
    private lateinit var loadingBar1:ProgressBar
    private lateinit var loadingBar2:ProgressBar
    private lateinit var loadingBar3: ProgressBar
    private lateinit var loadingBar4:ProgressBar



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

        recyclerViewTopRated = view.findViewById(R.id.recyclerViewTopRated)
        recyclerViewTopRated.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))

        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular)
        Log.d("HomeFragment", "$recyclerViewPopular")
        recyclerViewPopular.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
        Log.d("HomeFragment", "$recyclerViewPopular")

        recyclerViewUpComming = view.findViewById(R.id.recyclerViewUpComming)
        recyclerViewUpComming.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))

        recyclerViewGenres = view.findViewById(R.id.recyclerViewGenresHomePage)
        recyclerViewGenres.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))



        loadingBar1 = view.findViewById(R.id.progressBar1)
        loadingBar1.visibility = View.VISIBLE
        loadingBar2 = view.findViewById(R.id.progressBar2)
        loadingBar2.visibility = View.VISIBLE
        loadingBar3 = view.findViewById(R.id.progressBar3)
        loadingBar3.visibility = View.VISIBLE
        loadingBar4 = view.findViewById(R.id.progressBar4)
        loadingBar4.visibility = View.VISIBLE


        sliderHandler = Handler()
        banners()



        var items: MovieResponse

        val currentLocale: Locale = Locale.getDefault()
        val language:String = currentLocale.language
        val languageTag: String = currentLocale.toLanguageTag()  //Restituisce il tag della lingua

        Log.d("HomeFragment","languageTag = $languageTag")

        adapterPopular = FilmAdapter(MovieResponse().results)
        recyclerViewPopular.adapter = adapterPopular


        adapterGenres = GenreAdapter(GenreList())
        recyclerViewGenres.adapter = adapterGenres

        tmdbApiManager.getMoviePopular(language = languageTag) { movieResponse ->
            activity?.runOnUiThread { //entro del thread pricipale
                if (movieResponse != null && activity != null && isAdded) {
                    loadingBar2.visibility = View.GONE
                    adapterPopular = FilmAdapter(movieResponse.results)
                    recyclerViewPopular.adapter = adapterPopular
                    Log.d("HomeFragment", "Success to fetch movies (popular)")
                } else {
                    Log.e("HomeFragment", "Failed to fetch movies (popular)")
                }
            }
        }

        tmdbApiManager.getGenres(language){it->
            activity?.runOnUiThread{
                if(it!=null){
                    loadingBar4.visibility = View.GONE
                    adapterGenres = GenreAdapter(it)
                    recyclerViewGenres.adapter = adapterGenres
                    Log.d("HomeFragment", "Success to fetch genres")
                }else {
                    Log.e("HomeFragment", "Failed to fetch genres")
                }
            }

        }


        tmdbApiManager.getMovieTopRated(language = languageTag) { movieResponse ->
            activity?.runOnUiThread { //entro del thread pricipale
                if (movieResponse != null && activity != null && isAdded) {
                    loadingBar1.visibility = View.GONE
                    adapterTopRating = FilmAdapter(movieResponse.results)
                    recyclerViewTopRated.adapter = adapterTopRating
                    Log.d("HomeFragment", "Success to fetch movies (top rated)")
                } else {
                    Log.e("HomeFragment", "Failed to fetch movies (top rated)")
                }
            }
        }

        tmdbApiManager.getMovieUpcomming(language = languageTag) { movieResponse ->
            activity?.runOnUiThread { //entro del thread pricipale
                if (movieResponse != null && activity != null && isAdded) {
                    loadingBar3.visibility = View.GONE
                    adapterUpComming = FilmAdapter(movieResponse.results)
                    recyclerViewUpComming.adapter = adapterUpComming
                    Log.e("HomeFragment", "Success to fetch movies (Upcomming)")
                } else {
                    Log.e("HomeFragment", "Failed to fetch movies (Upcomming)")
                }
            }
        }






    }

    private fun banners(){
        val sliderItems = mutableListOf<SliderItems>()



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