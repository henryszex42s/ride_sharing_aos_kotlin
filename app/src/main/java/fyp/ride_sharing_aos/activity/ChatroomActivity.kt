package fyp.ride_sharing_aos.activity

import android.os.Bundle
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import fyp.ride_sharing_aos.adapters.MessageListAdapter
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_chatroom.*

import kotlinx.android.synthetic.main.app_bar_home.*
import java.sql.Timestamp
import android.widget.ScrollView
import android.text.method.TextKeyListener.clear
import android.view.MenuInflater
import com.squareup.okhttp.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


/**
 * Created by lfreee on 21/2/2018.
 */
class ChatroomActivity: BaseActivity(){

    var client = OkHttpClient()
    var distance = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

//        val abc = Tools.feeEstimation(startLatLng,desLatLng)

        //Update the room info, when there are any change in the RoomObj
        FirebaseManager.RoomObjUpdateListener(
                {
                    supportActionBar!!.setTitle(FirebaseManager.RoomObj!!.roomid)
                    chat_item_from.setText(FirebaseManager.RoomObj!!.start)
                    chat_item_to.setText(FirebaseManager.RoomObj!!.destination)
                    chat_current_date.setText(Tools.convertDate(FirebaseManager.RoomObj!!.prefertime!!))
                    chat_item_prefer_time.setText(Tools.convertTime(FirebaseManager.RoomObj!!.prefertime!!))
                    chat_item_numpeople.setText(FirebaseManager.RoomObj!!.numOfPeople.toString())

                    var startLatLng = Tools.getlatLngUsingName(FirebaseManager.RoomObj!!.start!!,this)
                    var desLatLng = Tools.getlatLngUsingName(FirebaseManager.RoomObj!!.destination!!,this)

                    val url = Tools.getDistanceMatrixUrl(startLatLng,desLatLng)
                    getDistance(url,{

                        runOnUiThread {
                            UpdateTaxiFee()
                        }

                    })
                })

        //Notify the View to update, when the message list is updated
        FirebaseManager.MessageUpdateListener({DataChange()},true)


        reyclerview_message_list.layoutManager = LinearLayoutManager(this)
        reyclerview_message_list.adapter = MessageListAdapter(this, FirebaseManager.MessageList)

        button_chatbox_send.setOnClickListener {
            val data = HashMap<String, Any>()
            data.put("message", edittext_chatbox.text.toString())
            data.put("send_time", System.currentTimeMillis())
            data.put("sender", FirebaseManager.getUserID().toString())
            data.put("type", FirebaseManager.MESSAGE_TYPE_USER)
            FirebaseManager.sendMessage(data)
            edittext_chatbox.setText("")
        }


    }

    fun getDistance(Url_Link : String, callback: (Any)->Unit)
    {

        val request = Request.Builder()
                .url(Url_Link)
                .build()

        client.newCall(request).enqueue(object: Callback {
             override fun onResponse(response: Response?) {
                 val responseData = response?.body()?.string()
                 val json = JSONObject(responseData)

                 val getDistance = json
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("distance")
                     distance = getDistance.get("value") as Int
                     callback(Unit)
             }
            override fun onFailure(request: Request?, e: IOException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun leaveChatRoom()
    {
        dismissProgressDialog()
        FirebaseManager.detachMessageListener()
        FirebaseManager.detachRoomListener()
        FirebaseManager.detachUserListener()
        finish()
    }

    fun UpdateTaxiFee()
    {
        var taxi_fee = ((distance/1000)-2.0)
        if(taxi_fee <0)
        {
            chat_item_fare_est.setText("HKD$ 24")
        }
        else
        {
            taxi_fee = 24+taxi_fee*(5.0*1.7)+1*1.7
            chat_item_fare_est.setText("HKD$ "+ taxi_fee)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.chatroom_menu, menu)
        return true
   }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.chatroom_leave ->
            {
                showProgressDialog(getString(R.string.progress_loading))
                FirebaseManager.leaveChatRoom { leaveChatRoom() }
                return true
            }
            R.id.chatroom_lock ->
            {
//                showProgressDialog(getString(R.string.progress_loading))
//                FirebaseManager.lockRoom()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
       }
   }


    fun DataChange()
    {
        if(reyclerview_message_list.getAdapter().getItemCount() != 0)
        {
            reyclerview_message_list.smoothScrollToPosition(reyclerview_message_list.getAdapter().getItemCount()-1)
        }
        reyclerview_message_list.adapter.notifyDataSetChanged()

    }

}