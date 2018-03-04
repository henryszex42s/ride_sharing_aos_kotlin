package fyp.ride_sharing_aos.activity

import android.os.Bundle
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import android.support.v7.widget.LinearLayoutManager
import fyp.ride_sharing_aos.adapters.MessageListAdapter
import fyp.ride_sharing_aos.model.Message
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.activity_chatroom.*


/**
 * Created by lfreee on 21/2/2018.
 */
class ChatroomActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        FirebaseManager.MessageUpdateListener({DataChange()},true)


        reyclerview_message_list.layoutManager = LinearLayoutManager(this)
        reyclerview_message_list.adapter = MessageListAdapter(this, FirebaseManager.MessageList)


        button_chatbox_send.setOnClickListener {
            val newMessage = Message(edittext_chatbox.text.toString(), Tools.currentTime.time,FirebaseManager.getUserID(),1)
            FirebaseManager.addMessage(newMessage)
        }
    }

    fun DataChange()
    {
        reyclerview_message_list.adapter.notifyDataSetChanged()
    }

}