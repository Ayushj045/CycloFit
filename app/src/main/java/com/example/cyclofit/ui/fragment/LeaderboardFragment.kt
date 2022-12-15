package com.example.cyclofit.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyclofit.R
import com.example.cyclofit.databinding.FragmentLeaderboardBinding
import com.example.cyclofit.model.User
import com.example.cyclofit.ui.adapter.FilteredLeaderBoardAdapter
import com.example.cyclofit.ui.adapter.LeaderboardAdapter
import com.example.cyclofit.ui.firestore.FirestoreClass
import kotlinx.android.synthetic.main.fragment_leaderboard.view.*
import java.util.*
import kotlin.collections.ArrayList

class LeaderboardFragment : BaseFragment() {

    private lateinit var list : ArrayList<User>
    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var filteredLeaderBoardList:ArrayList<User>
    private lateinit var leaderBoardUserList:ArrayList<User>
    companion object{
        var list=ArrayList<User>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentLeaderboardBinding.inflate(inflater,container,false)
        binding.toolbarDashboard.inflateMenu(R.menu.leaderboard_top)
        activity?.window!!.statusBarColor = requireActivity().getColor(R.color.dark_green)
        leaderBoardUserList = kotlin.collections.ArrayList<User>()
        //LeaderBoardSearchFunctionality
        binding.searchBox.clearFocus()
        binding.searchBox.setOnQueryTextListener(object:OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterLeaderBoardList(newText)
                return true
            }

        })

//         list=ArrayList<User>()
//        list.add(User("1","Pratyush","aries.@gmail.com","8.4"))
//        list.add(User("2","Ayush","aries.@gmail.com","7.2"))
//        list.add(User("3","Abhiram","aries.@gmail.com","6.8"))
//        list.add(User("4","Aditya","aries.@gmail.com","5.3"))
//        list.add(User("5","Samyak","aries.@gmail.com","5.1"))
//        list.add(User("6","Rahul","aries.@gmail.com","4.9"))
//        list.add(User("7","Ankur","aries.@gmail.com","3.0"))
//        list.add(User("8","Adiii","aries.@gmail.com","2.5"))
//        list.add(User("9","Prince","aries.@gmail.com","1.7"))
//        list.add(User("10","Ritik","aries.@gmail.com","0.8"))


        return binding.root
    }

    private fun filterLeaderBoardList(newText: String?) {
        filteredLeaderBoardList = ArrayList<User>()
        for(item in leaderBoardUserList){
            if(item.name.lowercase().contains(newText!!.lowercase()))
            {
                filteredLeaderBoardList.add(item)
            }
        }
        if(filteredLeaderBoardList.isEmpty()){
            showErrorSnackBar("No User Found",true)
        }else{
            binding.rvLeaderboard.adapter=LeaderboardAdapter(requireContext(),filteredLeaderBoardList)
        }
    }

    fun getLeaderBoard(userList: ArrayList<User>) {
        userList.sortByDescending { it.distance.toDouble() }
        hideProgressDialog()
        binding.rvLeaderboard.adapter=LeaderboardAdapter(requireContext(),userList)
        binding.rvLeaderboard.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
    }
    fun getUserList(userList:ArrayList<User>){

        //filter list issue pratyush singh is at bottom with 7.9km should at pos 2
        leaderBoardUserList = userList
        getTopUser(userList)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FirestoreClass().getLeaderboardFragment(this)
    }

    private fun getTopUser(userList: ArrayList<User>) {
        binding.nameOfUser.text = userList.first().name
        binding.emailOfUser.text = userList.first().email
        binding.distanceCovered.text = userList.first().distance+" km"
    }
}