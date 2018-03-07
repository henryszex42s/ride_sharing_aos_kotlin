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




/**
 * Created by lfreee on 21/2/2018.
 */
class ChatroomActivity: BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)


        //Update the room info, when there are any change in the RoomObj
        FirebaseManager.RoomObjUpdateListener(
                {
                    supportActionBar!!.setTitle(FirebaseManager.RoomObj!!.roomid)
                    chat_item_from.setText(FirebaseManager.RoomObj!!.start)
                    chat_item_to.setText(FirebaseManager.RoomObj!!.destination)
                    chat_current_date.setText(Tools.convertDate(FirebaseManager.RoomObj!!.prefertime!!))
                    chat_item_prefer_time.setText(Tools.convertTime(FirebaseManager.RoomObj!!.prefertime!!))
                    chat_item_numpeople.setText(FirebaseManager.RoomObj!!.numOfPeople.toString() + "/4")
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

        button_leave.setOnClickListener {

            showProgressDialog(getString(R.string.progress_loading))
            FirebaseManager.leaveChatRoom { leaveChatRoom() }

        }


    }

    fun leaveChatRoom()
    {
        dismissProgressDialog()
        FirebaseManager.detachMessageListener()
        FirebaseManager.detachRoomListener()
        FirebaseManager.detachUserListener()
        finish()

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
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
       }
   }

    fun DataChange()
    {
        reyclerview_message_list.adapter.notifyDataSetChanged()
    }

}