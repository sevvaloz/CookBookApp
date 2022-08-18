package com.example.cookbook

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_cook_list.*

class CookListFragment : Fragment() {

    //Tanımlamalar:
    var cookNameArray = ArrayList<String>()
    var cookIdArray = ArrayList<Int>()
    private lateinit var listAdapter : ListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cook_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ListRecyclerAdapter'a göndereceğiz:
        listAdapter = ListRecyclerAdapter(cookNameArray, cookIdArray)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter


        getDataFromSql()
    }


    fun getDataFromSql(){
        try {

            activity?.let {

                val database = it.openOrCreateDatabase("CookBookDatabase", Context.MODE_PRIVATE, null)
                val cursor = database.rawQuery("SELECT * FROM cooks",null)
                val cookNameIndex = cursor.getColumnIndex("cookname")
                val cookIdIndex = cursor.getColumnIndex("id")

                //önceden kalmış bir şeyler varsa silelim:
                cookNameArray.clear()
                cookIdArray.clear()

                //recyclerview'da göstermek için dizilere atadık:
                while (cursor.moveToNext()){
                    cookNameArray.add(cursor.getString(cookNameIndex))
                    cookIdArray.add(cursor.getInt(cookIdIndex))
                }

                //yeni veri eklediğinde database'in güncellenmesini sağlar:
                listAdapter.notifyDataSetChanged()

                cursor.close()
            }

        } catch (e:Exception){

        }
    }













}