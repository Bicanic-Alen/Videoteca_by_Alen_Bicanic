package videoteca.main.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import videoteca.main.LoginActivity
import videoteca.main.R
import videoteca.main.SettingsActivity
import videoteca.main.SharedInfo
import videoteca.main.api.AuthService
import java.util.Calendar


class UserAreaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //istanzio sharedinfo per recuperare le informazioni salvate nel sistema
        val sharedInfo = SharedInfo(requireContext())
        var textWelcome:String
        val tvUserCurrent = view.findViewById<TextView>(R.id.tv_currentuser)
        textWelcome = "${getGreeting()} ${sharedInfo.getUserName()}"
        tvUserCurrent.text = textWelcome


        val tvLogout = view.findViewById<TextView>(R.id.tv_logout)
        tvLogout.setOnClickListener {
            AuthService.logout()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }




        val tvSettings = view.findViewById<TextView>(R.id.tv_settings_ua)
        tvSettings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            context?.startActivity(intent)
        }

        val tvRented = view.findViewById<TextView>(R.id.tv_rented)
        //tvRented.setOnClickListener {  }



        val tvFav = view.findViewById<TextView>(R.id.tv_genres_fav)
        //tvFav.setOnClickListener {  }





    }

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        return when {
            hourOfDay < 4 || hourOfDay >= 22 -> getString(R.string.good_night)
            hourOfDay < 13 -> getString(R.string.good_morning)
            hourOfDay < 18 -> getString(R.string.good_afternoon)
            else -> getString(R.string.good_evening)
        }
    }

    private fun changeFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null) // Opzionale: aggiunge la transazione al back stack
        transaction.commit()
    }





}