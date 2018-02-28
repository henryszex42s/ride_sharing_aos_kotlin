package fyp.ride_sharing_aos.fragement


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fyp.ride_sharing_aos.R
import fyp.ride_sharing_aos.adapters.RoomListAdapter
import fyp.ride_sharing_aos.tools.FirebaseManager
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if (view != null) {
            super.onViewCreated(view, savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_roomlist.layoutManager = LinearLayoutManager(activity)
        rv_roomlist.adapter = RoomListAdapter(this.context!!,FirebaseManager.getRoomList())
    }

    override fun onStart() {
        super.onStart()
            }

     fun DataChange()
    {
        rv_roomlist.adapter.notifyDataSetChanged()
    }


}
