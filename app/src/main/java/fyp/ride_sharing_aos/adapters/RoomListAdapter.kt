package fyp.ride_sharing_aos.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.model.Room
import kotlinx.android.synthetic.main.roomlist_layout.view.*

/**
 * Created by lfreee on 25/1/2018.
 */
class RoomListAdapter(val c : Context,val roomlist: MutableList<Room>) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(c).inflate(R.layout.roomlist_layout, parent, false)
        return ViewHolder(c,view)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(roomlist[position])
    }


    override fun getItemCount(): Int {
        return roomlist.size
    }




class ViewHolder(val c : Context, val view: View): RecyclerView.ViewHolder(view) {

    fun bindItems(room: Room) {
        view.textViewUsername.text = room.start
        view.textViewAddress.text = room.destination

        view.setOnClickListener {
            Toast.makeText(c, room.start+" To "+room.destination, Toast.LENGTH_SHORT).show()

        }
    }
}

}