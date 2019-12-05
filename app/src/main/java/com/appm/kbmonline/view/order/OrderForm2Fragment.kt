package com.appm.kbmonline.view.order


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.appm.kbmonline.R
import kotlinx.android.synthetic.main.fragment_order_form2.*


class OrderForm2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_form2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        f2_btn_map_pesan.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_orderForm2Fragment_to_mapFragment)
        }
    }


}
