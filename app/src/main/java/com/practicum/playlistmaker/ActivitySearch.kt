package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView

class ActivitySearch : AppCompatActivity() {

    private var savedText: String = ""

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

        toolbarSearch.setNavigationOnClickListener  {
            finish()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(inputEditText)
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
        val searchAdapter = SearchAdapter(genarateMockTrakList(trackNameList, artistNameList, trackTime, artworkUrl100))
        recyclerView.adapter = searchAdapter

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard(currentView: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH,savedText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(KEY_SEARCH, "")
    }

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
    }

    val trackNameList = arrayListOf("Smells Like Teen Spirit", "Billie Jean", "Stayin' Alive", "Whole Lotta Love", "Sweet Child O'Mine")
    val artistNameList = arrayListOf("Nirvana", "Michael Jackson", "Bee Gees", "Led Zeppelin", "Guns N' Roses")
    val trackTime = arrayListOf("5:01", "4:35", "4:10", "5:33", "5:03")
    val artworkUrl100 = arrayListOf("https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ,"https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ,"https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ,"https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ,"https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")

    fun genarateMockTrakList(name: ArrayList<String>, artist: ArrayList<String>, time: ArrayList<String>, url: ArrayList<String>): ArrayList<Track>{
        val minSize = minOf(name.size, artist.size, time.size, url.size)
        // Если списки будут разного размера, размер списка Traсk будет с учётом минимального списка
        val trackList = ArrayList<Track>()

        for (i in 0 until minSize) {
            trackList.add(
                Track(
                    trackName = name[i],
                    artistName = artist[i],
                    trackTime = time[i],
                    artworkUrl100 = url[i]
                )
            )
        }
        return trackList
    }
}