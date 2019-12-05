package com.appm.kbmonline.view.order


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.appm.kbmonline.R
import kotlinx.android.synthetic.main.fragment_order.*
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 */
class OrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    var next_page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fOrderViewPager.adapter = OrderPagerAdapter(childFragmentManager)

        fOrderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 3) {
                    return
                } else {
                    next_page = position + 1
                }
            }

            override fun onPageSelected(position: Int) {
                if (position != 2) {
                    f_order_fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            view.context,
                            R.drawable.ic_arrow_forward_black_24dp
                        )
                    )
                } else {
                    f_order_fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            view.context,
                            R.drawable.ic_done_black_24dp
                        )
                    )
                }
            }
        })

        f_order_fab.setOnClickListener {
            if (next_page - 1 == 2) {//current position
                toast("submited")
            } else {
                fOrderViewPager.setCurrentItem(next_page, true)
            }

        }
    }
}
