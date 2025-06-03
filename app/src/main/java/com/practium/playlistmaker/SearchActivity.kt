package com.practium.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
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

class SearchActivity : AppCompatActivity() {

    private var searchLine: String = SEARCH_LINE_DEF

    private val iTunesBaseUrl = getString(R.string.itunes_base_url)
    private val searchEditText = findViewById<EditText>(R.id.searchEditText)

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val trackList = ArrayList<ITunesSearch>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val goBackArrow = findViewById<MaterialToolbar>(R.id.arrowBackButton)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val trackListAdapter = TrackListSearchAdapter(trackList)
        recyclerView.adapter = trackListAdapter


        searchEditText.setText(searchLine)

        goBackArrow.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText(SEARCH_LINE_DEF)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
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

                override fun onResponse(
                    call: Call<ITunesSearchResponse>,
                    response: Response<ITunesSearchResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ITunesSearchResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    companion object {
        const val SEARCH_LINE = "SEARCH_LINE"
        const val SEARCH_LINE_DEF = ""
    }
}
