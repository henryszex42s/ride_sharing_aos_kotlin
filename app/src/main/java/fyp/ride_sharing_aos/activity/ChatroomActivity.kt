package fyp.ride_sharing_aos.activity

import android.os.Bundle
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import fyp.ride_sharing_aos.adapters.MessageListAdapter
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_chatroom.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.sql.Timestamp


/**
 * Created by lfreee on 21/2/2018.
 */
class ChatroomActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        FirebaseManager.RoomObjUpdateListener({supportActionBar!!.setTitle(FirebaseManager.RoomObj!!.roomid) })
        FirebaseManager.MessageUpdateListener({DataChange()},true)

        reyclerview_message_list.layoutManager = LinearLayoutManager(this)
        reyclerview_message_list.adapter = MessageListAdapter(this, FirebaseManager.MessageList)


        button_chatbox_send.setOnClickListener {
            val data = HashMap<String, Any>()
            data.put("message", edittext_chatbox.text.toString())
            data.put("send_time", System.currentTimeMillis())
            data.put("sender", FirebaseManager.getUserID().toString())
            data.put("type", FirebaseManager.MESSAGE_TYPE_USER)
            FirebaseManager.addMessage(data)
            edittext_chatbox.setText("")
        }


        // room info
        FirebaseManager.RoomObjUpdateListener({chat_item_from.setText(FirebaseManager.RoomObj!!.start) })
        FirebaseManager.RoomObjUpdateListener({chat_item_to.setText(FirebaseManager.RoomObj!!.destination) })
        FirebaseManager.RoomObjUpdateListener({chat_current_date.setText(Tools.convertDate(FirebaseManager.RoomObj!!.prefertime!!)) })
        FirebaseManager.RoomObjUpdateListener({chat_item_prefer_time.setText(Tools.convertTime(FirebaseManager.RoomObj!!.prefertime!!)) })
        FirebaseManager.RoomObjUpdateListener({chat_item_numpeople.setText(FirebaseManager.RoomObj!!.numOfPeople.toString() + "/4") })



    }

    fun DataChange()
    {
        reyclerview_message_list.adapter.notifyDataSetChanged()
    }

}