package com.example.ipolitician.nav.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import com.google.android.material.snackbar.Snackbar
import com.richpath.RichPath
import com.richpath.RichPath.OnPathClickListener
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_blank, container, false)

        val notificationsRichPathView = root.findViewById<RichPathView>(R.id.app_nav_header_background)

        val top: RichPath? = notificationsRichPathView.findRichPathByName("top")
        val bottom: RichPath? = notificationsRichPathView.findRichPathByName("bottom")

//        RichPathAnimator.animate(top)
//            .interpolator(DecelerateInterpolator())
//            .rotation(0f, 20f, -20f, 10f, -10f, 5f, -5f, 2f, -2f, 0f)
//            .duration(4000)
//            .andAnimate(bottom)
//            .interpolator(DecelerateInterpolator())
//            .rotation(0f, 10f, -10f, 5f, -5f, 2f, -2f, 0f)
//            .startDelay(50)
//            .duration(4000)
//            .start()


        notificationsRichPathView.setOnPathClickListener(OnPathClickListener { richPath ->
            Snackbar.make(root, richPath.name, Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show()
//            if (richPath.name == "Abkhazia") {
//                Snackbar.make(root, "Abkhazia", Snackbar.LENGTH_LONG).setAction(
//                    "Action",
//                    null
//                ).show()
//            } else {
//                Snackbar.make(root, "BOT CLICK", Snackbar.LENGTH_LONG).setAction(
//                    "Action",
//                    null
//                ).show()
//            }
        })

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}