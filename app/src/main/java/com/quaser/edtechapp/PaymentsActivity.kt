package com.quaser.edtechapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quaser.edtechapp.Adapter.PaymentsRVAdapter
import com.quaser.edtechapp.rest.api.APIMethods
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener
import com.quaser.edtechapp.rest.response.AssignmentListRP
import com.quaser.edtechapp.rest.response.PaymentsListRP
import com.quaser.edtechapp.utils.Method

class PaymentsActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var topBar: LinearLayout? = null
    private var manager: LinearLayoutManager? = null
    private var adapter: PaymentsRVAdapter? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        findViews();
        setScrollListener()
        loadAssignments()
    }

    private fun findViews() {
        recyclerView = findViewById(R.id.recyclerView)
        topBar = findViewById(R.id.topBar)
        progressBar = findViewById(R.id.progressBar)
        var titleTxt = findViewById<TextView>(R.id.titleTxt);
        titleTxt.text = "Your Payments"
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

    //Todo setUp pagination here.
    //Todo onClick go to respective lesson/purchase


    private fun loadAssignments() {
        APIMethods.getPayments(object : APIResponseListener<PaymentsListRP> {

            override fun fail(code: String, message: String, redirectLink: String, retry: Boolean, cancellable: Boolean) {
                progressBar!!.visibility = View.GONE
                Method.showFailedAlert(this@PaymentsActivity, "$code - $message")
            }

            override fun success(response: PaymentsListRP?) {
                progressBar!!.visibility = View.GONE
                adapter = PaymentsRVAdapter(response, this@PaymentsActivity);
                manager = LinearLayoutManager(this@PaymentsActivity)
                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager = manager
                recyclerView!!.visibility = View.VISIBLE            }
        })
    }
}