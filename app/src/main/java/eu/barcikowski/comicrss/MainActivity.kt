package eu.barcikowski.comicrss

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import eu.barcikowski.comicrss.Adapter.FeedAdapter
import eu.barcikowski.comicrss.Common.HTTPDataHandler
import eu.barcikowski.comicrss.Model.RSSObject

import kotlinx.android.synthetic.main.activity_main.*;
import eu.barcikowski.comicrss.R.id.recyclerView
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.support.v4.widget.SwipeRefreshLayout


class MainActivity : AppCompatActivity() {


    private val RSS_link = "https://xkcd.com/rss.xml"
    // private val RSS_link = "http://www.comicsrss.com/rss/think.rss"
    // private val RSS_link = "https://www.reddit.com/r/comics/.rss?format=xml"


    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //   toolbar.title = "xkcd"
        //   setSupportActionBar(toolbar)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.activity_main_swipe_refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener { loadRSS() }

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        val helper = LinearSnapHelper()

        helper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = linearLayoutManager

        loadRSS()

    }


    private inner class loadRSSTask : AsyncTask<String, String, String>() {
        internal var mDialog = ProgressDialog(this@MainActivity)

        override fun doInBackground(vararg params: String?): String {
            val result: String
            val http = HTTPDataHandler()
            result = http.GetHTTPDataHandler(params[0])
            return result
        }

        override fun onPreExecute() {

            mDialog.setMessage("Please wait...")
            mDialog.show()
        }

        override fun onPostExecute(result: String?) {
            mDialog.dismiss()
            var rssObject: RSSObject
            rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java!!)
            val adapter = FeedAdapter(rssObject, baseContext)
            recyclerView.adapter = adapter
            findViewById<SwipeRefreshLayout>(R.id.activity_main_swipe_refresh_layout).isRefreshing = false
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadRSS() {
        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSTask().execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh)
            loadRSS()
        return true
    }
}
