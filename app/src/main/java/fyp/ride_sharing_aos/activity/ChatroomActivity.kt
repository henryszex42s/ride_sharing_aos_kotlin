package fyp.ride_sharing_aos.activity

import android.os.Bundle
import fyp.ride_sharing_aos.BaseActivity
import fyp.ride_sharing_aos.R
import android.support.v7.widget.LinearLayoutManager
import fyp.ride_sharing_aos.adapters.MessageListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import fyp.ride_sharing_aos.model.Message
import fyp.ride_sharing_aos.tools.FirebaseManager
import kotlinx.android.synthetic.main.activity_chatroom.*


/**
 * Created by lfreee on 21/2/2018.
 */
class ChatroomActivity: BaseActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        var  mMessageList: List<Message> = arrayListOf()

        reyclerview_message_list.layoutManager = LinearLayoutManager(this)
        reyclerview_message_list.adapter = MessageListAdapter(this, mMessageList)


    }




}