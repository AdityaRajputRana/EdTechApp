package com.quaser.edtechapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quaser.edtechapp.Adapter.LeaderboardRVAdapter
import com.quaser.edtechapp.Adapter.NotificationsRVAdapter
import com.quaser.edtechapp.models.NotificationModel
import com.quaser.edtechapp.rest.api.APIMethods
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener
import com.quaser.edtechapp.rest.response.RanklistRes
import com.quaser.edtechapp.utils.Method

class NotificationActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var topBar: LinearLayout? = null
    private var manager: LinearLayoutManager? = null
    private var adapter: NotificationsRVAdapter? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        findViews();
        setScrollListener()
        loadNotifications()
    }

    private fun findViews() {
        recyclerView = findViewById(R.id.recyclerView)
        topBar = findViewById(R.id.topBar)
        progressBar = findViewById(R.id.progressBar)
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })
    }

    private fun setScrollListener() {
        recyclerView!!.setOnScrollChangeListener { view, i, i1, i2, i3 ->
            if (manager!!.findFirstCompletelyVisibleItemPosition() == 0) {
                if (topBar!!.visibility == View.VISIBLE) {
                    topBar!!.visibility = View.INVISIBLE
                }
            }
            if (manager!!.findFirstCompletelyVisibleItemPosition() == 1) {
                if (topBar!!.visibility != View.VISIBLE) {
                    topBar!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadNotifications() {
        APIMethods.getNotification(object : APIResponseListener<ArrayList<NotificationModel>?> {

            override fun fail(code: String, message: String, redirectLink: String, retry: Boolean, cancellable: Boolean) {
                progressBar!!.visibility = View.GONE
                Method.showFailedAlert(this@NotificationActivity, "$code - $message")
            }

            override fun success(response: ArrayList<NotificationModel>?) {
                progressBar!!.visibility = View.GONE
                adapter = NotificationsRVAdapter(response, this@NotificationActivity);
                manager = LinearLayoutManager(this@NotificationActivity)
                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = manager
                recyclerView!!.visibility = View.VISIBLE            }
        })
    }

}