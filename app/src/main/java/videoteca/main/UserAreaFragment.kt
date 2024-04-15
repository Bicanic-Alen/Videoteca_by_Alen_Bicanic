package videoteca.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.log


class UserAreaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val tvLogout = view.findViewById<TextView>(R.id.tv_logout)
        tvLogout.setOnClickListener {
            AuthService.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val sharedInfo = SharedInfo(requireContext())


        val tvSettings = view.findViewById<TextView>(R.id.tv_settings_ua)
        tvSettings.setOnClickListener {
            val fragmentSettings = SettingsFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentSettings).commit()

        }

        val tvRented = view.findViewById<TextView>(R.id.tv_rented)
        tvSettings.setOnClickListener {  }

        val tvReserved = view.findViewById<TextView>(R.id.tv_reserved)
        tvSettings.setOnClickListener {  }

        val tvFav = view.findViewById<TextView>(R.id.tv_genres_fav)
        tvSettings.setOnClickListener {  }

        var textWelcome:String
        val tvUserCurrent = view.findViewById<TextView>(R.id.tv_currentuser)
        textWelcome = "${getGreeting()} ${sharedInfo.getUserName()} ${sharedInfo.getUserSurname()}"
        tvUserCurrent.text = textWelcome
        Log.d("UserAreaFragment", "name (var) = ${sharedInfo.getUserName()}")
        Log.d("UserAreaFragment", "surname (var) = ${sharedInfo.getUserSurname()}")



    }

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        return when {
            hourOfDay < 12 -> getString(R.string.good_morning)
            hourOfDay < 18 -> getString(R.string.good_afternoon)
            else -> getString(R.string.good_evening)
        }
    }





}