package com.example.carrotmarketclone.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrotmarketclone.DBKey.Companion.DB_ARTICLES
import com.example.carrotmarketclone.R
import com.example.carrotmarketclone.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDB: DatabaseReference

    private val articleList = mutableListOf<ArticleModel>()

    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

        override fun onCancelled(error: DatabaseError) {}

    }

    private var binding: FragmentHomeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear()
        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        articleAdapter = ArticleAdapter()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        articleDB.addChildEventListener(listener)
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        articleDB.removeEventListener(listener)
    }

}