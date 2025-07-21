package com.practium.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val ITUNES_BASE_URL = "https://itunes.apple.com"

class SearchActivity : AppCompatActivity() {

    private var searchLine: String = SEARCH_LINE_DEF

    private lateinit var searchEditText: EditText
    private lateinit var searchPlaceHolderImage: ImageView
    private lateinit var searchPlaceHolderText: TextView
    private lateinit var refreshSearchButton: Button
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var clearSearchHistoryButton: Button
    private lateinit var searchHistory: SearchHistory

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val trackList: MutableList<Track> = mutableListOf()
    private var searchHistoryList: ArrayList<Track> = ArrayList()
    private lateinit var trackListAdapter: TrackListSearchAdapter
    private lateinit var searchHistoryListAdapter: TrackListSearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchPlaceHolderImage = findViewById<ImageView>(R.id.searchPlaceHolderImage)
        searchPlaceHolderText = findViewById<TextView>(R.id.searchPlaceHolderText)
        refreshSearchButton = findViewById<Button>(R.id.refreshSearchButton)
        searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        clearSearchHistoryButton= findViewById<Button>(R.id.clearSearchHistoryButton)
        val goBackArrow = findViewById<MaterialToolbar>(R.id.arrowBackButton)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        val searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

/*        val testSearchHistoryArrayList: ArrayList<Track> = ArrayList()
        testSearchHistoryArrayList.add(Track("Track1", "Artist", 11111,"",1))
        testSearchHistoryArrayList.add(Track("Track2", "Artist_0", 11122,"",2))
        testSearchHistoryArrayList.add(Track("Track3", "Artist_1", 11133,"",3))
*/

        hidePlaceholder()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRecyclerView.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        trackListAdapter = TrackListSearchAdapter(trackList,
            onTrackClick = {track ->
            clickOnTrack(track, sharedPrefs)
        })
        recyclerView.adapter = trackListAdapter

        searchHistory = SearchHistory()
        //searchHistoryList = searchHistory.getSearchHistoryFromPrefs(sharedPrefs)
        searchHistoryList = ArrayList<Track>()
        searchHistoryListAdapter = TrackListSearchAdapter(
            searchHistoryList,
            onTrackClick = {})

        searchHistoryRecyclerView.adapter = searchHistoryListAdapter

        searchEditText.setText(searchLine)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearSearchResult()
                hidePlaceholder()
                search()
                true
            }
            false
        }

        goBackArrow.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText(SEARCH_LINE_DEF)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            clearSearchResult()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchLine = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

        refreshSearchButton.setOnClickListener {
            clearSearchResult()
            hidePlaceholder()
            search()
        }

        clearSearchHistoryButton.setOnClickListener {
            clearSearchHistory(sharedPrefs)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_LINE, searchLine)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchLine = savedInstanceState.getString(SEARCH_LINE, SEARCH_LINE_DEF)
    }

    private fun search(){
        iTunesService.search(searchEditText.text.toString())
            .enqueue(object : Callback<ITunesSearchResponse> {


                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ITunesSearchResponse>,
                    response: Response<ITunesSearchResponse>
                ) {
                    val responseResult = response.body()?.results
                    when (response.code()) {
                        200 -> {
                            if (responseResult?.isNotEmpty() == true) {
                                trackList.clear()
                                trackList.addAll(response.body()?.results!!)
                                trackListAdapter.notifyDataSetChanged()
                            } else {
                                showPlaceholder(Placeholder.NOTHING_FIND)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ITunesSearchResponse>, t: Throwable) {
                    trackList.clear()
                    showPlaceholder(Placeholder.NO_CONNECTION)
                }
            })
    }

    companion object {
        const val SEARCH_LINE = "SEARCH_LINE"
        const val SEARCH_LINE_DEF = ""
    }

    private fun showPlaceholder(placeholder: Placeholder){
        when (placeholder) {
            Placeholder.NOTHING_FIND -> {
                searchPlaceHolderImage.isVisible = true
                searchPlaceHolderText.isVisible = true
                searchPlaceHolderImage.setImageResource(R.drawable.nothing_find)
                searchPlaceHolderText.setText(R.string.nothing_find)
            }
            Placeholder.NO_CONNECTION -> {
                searchPlaceHolderImage.isVisible = true
                searchPlaceHolderText.isVisible = true
                refreshSearchButton.isVisible = true
                searchPlaceHolderImage.setImageResource(R.drawable.no_connection)
                searchPlaceHolderText.setText(R.string.connection_error)
            }
        }
    }

    private fun hidePlaceholder() {
        searchPlaceHolderImage.isVisible = false
        searchPlaceHolderText.isVisible = false
        refreshSearchButton.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchResult(){
        trackList.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchHistory(sharedPrefs: SharedPreferences){
        searchHistoryList.clear()
        searchHistory.clearSearchHistory(sharedPrefs)
        searchHistoryListAdapter.notifyDataSetChanged()
        Toast.makeText(this, "История поиска очищена", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickOnTrack(track: Track, sharedPrefs: SharedPreferences){
        searchHistoryList.clear()
        searchHistory.addTrackToSearchHistory(sharedPrefs, track)
        searchHistoryList.addAll(searchHistory.getSearchHistory())
        searchHistoryListAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Трек ${track.trackName} добавлен в историю", Toast.LENGTH_SHORT).show()
    }

    enum class Placeholder {
        NOTHING_FIND,
        NO_CONNECTION
    }
}
