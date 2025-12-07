package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitySearch : AppCompatActivity() {

    private var savedText: String = ""

    private val searchService = RetrofitClientiTunes.iTunesService

    val tracks = ArrayList<Track>()
    val searchAdapter = SearchAdapter(tracks)

    private lateinit var placeholderMessage: TextView
    private lateinit var reconnect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarSearch = findViewById<Toolbar>(R.id.toolbar_search)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)
        reconnect = findViewById<Button>(R.id.reconnect)

        toolbarSearch.setNavigationOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")

            tracks.clear()
            placeholderMessage.visibility = View.GONE
            reconnect.visibility = View.GONE

            searchAdapter.notifyDataSetChanged()
            hideKeyboard(inputEditText)
        }
        reconnect.setOnClickListener {
            if (inputEditText.text.isNotEmpty()) {
                performSearch(inputEditText.text.toString())
            } else {
                showMessage(R.drawable.ic_no_network_120, "Введите запрос")
                reconnect.visibility = View.GONE
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    performSearch(inputEditText.text.toString())
                }
                true
            }
            false
        }

        inputEditText.doOnTextChanged { text, start, before, count ->
            savedText = text?.toString() ?: ""
            clearButton.visibility = clearButtonVisibility(text)
        }

        if (savedInstanceState != null) {
            savedText = savedInstanceState.getString(KEY_SEARCH, "")
            inputEditText.setText(savedText)
            clearButton.visibility = clearButtonVisibility(savedText)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = searchAdapter

    }

    private fun performSearch(query: String) {
        searchService.search(query).enqueue(object : Callback<iTunesResponse> {
            override fun onResponse(
                call: Call<iTunesResponse>,
                response: Response<iTunesResponse>
            ) {
                if (response.isSuccessful) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        placeholderMessage.visibility = View.GONE
                        reconnect.visibility = View.GONE
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                    } else {
                        showMessage(
                            R.drawable.ic_nothing_found_120,
                            getString(R.string.nothing_found)
                        )
                        reconnect.visibility = View.GONE
                    }
                } else {
                    showMessage(R.drawable.ic_no_network_120, getString(R.string.no_network))
                }
            }
            override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
                showMessage(R.drawable.ic_no_network_120, getString(R.string.no_network))
                reconnect.visibility = View.VISIBLE
            }

        })
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun showMessage(@DrawableRes iconRes: Int, text: String) {
        placeholderMessage.visibility = View.VISIBLE
        tracks.clear()
        searchAdapter.notifyDataSetChanged()
        placeholderMessage.text = text
        placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(
            null,
            ContextCompat.getDrawable(this, iconRes),
            null,
            null
        )
        val paddingInPx =
            (DRAWABLE_PADDING_DP * placeholderMessage.resources.displayMetrics.density).toInt()
        placeholderMessage.compoundDrawablePadding = paddingInPx
    }

    private fun hideKeyboard(currentView: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(KEY_SEARCH, "")
    }

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
        private const val DRAWABLE_PADDING_DP = 16
    }
}