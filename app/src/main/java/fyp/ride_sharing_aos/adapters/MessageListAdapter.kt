package fyp.ride_sharing_aos.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.model.Message
import fyp.ride_sharing_aos.tools.FirebaseManager
import fyp.ride_sharing_aos.tools.Tools
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_sent.view.*



/**
 * Created by lfreee on 21/2/2018.
 */
class MessageListAdapter(private val mContext: Context, private val mMessageList: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    companion object {
        const val RECEIVE = 0
        const val SENT = 1
        const val SYSTEM = 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder = if(viewType == RECEIVE)
          ReceiveViewHolder( LayoutInflater.from(mContext).inflate(R.layout.item_message_received, parent, false))
      else
          SentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false))

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {

        var type = 1

        if(mMessageList[position].type == "0")
        {
            type = SYSTEM
        }

        if(mMessageList[position].type == "1")
        {
            if(mMessageList[position].sender.equals(FirebaseManager.getUserID()))
            {
                type = SENT
            }
            else
            {
                type = RECEIVE
            }

        }
        return type
    }
    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UpdateViewHolder).bindViews(mMessageList[position])
    }


    override fun getItemCount(): Int {
        return mMessageList.size
    }



//    fun setUpdates(updates: List<Message>) {
//        mMessageList = updates
//        notifyDataSetChanged()
//    }


    interface UpdateViewHolder {
        fun bindViews(msg_info: Message)
    }


    class ReceiveViewHolder(val view: View) : RecyclerView.ViewHolder(view), UpdateViewHolder {

        // get the views reference from itemView...

        override fun bindViews(msg_info: Message) {
            view.received_text_message_body.text = msg_info.message
            view.received_time.text = Tools.convertTime(msg_info.send_time!!)
            FirebaseManager.uidGetUserID(msg_info.sender!!,{ u_name -> view.sender.text = u_name})
        }
    }

    class SentViewHolder(val view: View)
: RecyclerView.ViewHolder(view), UpdateViewHolder {

        override fun bindViews(msg_info: Message) {
            view.sent_text_message_body.text = msg_info.message
            view.sent_time.text = Tools.convertTime(msg_info.send_time!!)

        }
    }
}

