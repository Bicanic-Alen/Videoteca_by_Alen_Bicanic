package videoteca.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



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


        val tvUserCorrent = view.findViewById<TextView>(R.id.tv_currentuser)
        val currentUser = AuthService.getCurrentUser().hashCode();
        var textWelcome = "Ciao ID ${currentUser}"
        tvUserCorrent.text = textWelcome
    }





}