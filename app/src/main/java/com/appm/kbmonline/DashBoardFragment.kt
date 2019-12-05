package com.appm.kbmonline


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_dash_board.*

class DashBoardFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        f_menu_card_order.setOnClickListener(this)
        f_menu_card_acc.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.f_menu_card_order -> {
                Navigation.findNavController(view)
                    .navigate(R.id.action_dashBoardFragment_to_orderFragment)
            }
            R.id.f_menu_card_acc -> {
                Navigation.findNavController(view)
                    .navigate(R.id.action_dashBoardFragment_to_accOrderFragment)
            }
        }
    }
}
