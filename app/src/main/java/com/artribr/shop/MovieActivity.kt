package com.artribr.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_movice.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MovieActivity : AppCompatActivity(), AnkoLogger {
    var movies:List<Movie>? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.myjson.com/bins/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movie = "http://api.myjson.com/bins/1gqjys"
        doAsync {



            val movieService = retrofit.create(MovieService::class.java)
            movies = movieService.listMovice().execute().body()

//            val json = URL(movie).readText()
            //System.err: com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING at line 8 column 1 path $
            //因Movie是物件，而取回的json string是array中放Movie物件，因此使用TypeToken定義規範，程式將依規範進行轉換
//            movies = Gson().fromJson<List<Movie>>(json,
//                object: TypeToken<List<Movie>>(){}.type)


            movies?.forEach{
                info("${it.Title} ${it.imdbRating}")
            }


            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = MovieAdapter()
            }
        }




    }

    inner class MovieAdapter(): RecyclerView.Adapter<MovieHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movice, parent, false)
            return MovieHolder(view)
        }

        override fun getItemCount(): Int {
            return movies?.size?:0
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies?.get(position)

            holder.bindMovie(movie!!)
        }


    }

    inner class MovieHolder(view : View): RecyclerView.ViewHolder(view){
        val titleText: TextView = view.movice_titile
        val imdbText: TextView = view.movie_imdb
        val directorText: TextView = view.movie_director
        val posterImage: ImageView = view.movie_poster
        fun bindMovie(movie: Movie){
            titleText.text = movie.Title
            imdbText.text = movie.imdbRating
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                .load(movie.Poster)
                .override(300)
                .into(posterImage)

        }
    }
}



data class Movie(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
)

interface MovieService{
    @GET("1gqjys")
    fun listMovice(): Call<List<Movie>>
}
