package com.example.peminjamanbuku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

    class MyListData : AppCompatActivity(), RecyclerViewAdapter.dataListener {
        private var recyclerView: RecyclerView? = null
        private var adapter: RecyclerView.Adapter<*>? = null
        private var layoutManager: RecyclerView.LayoutManager? = null

        val database = FirebaseDatabase.getInstance()
        private var dataPinjam = ArrayList<data_pinjam>()
        private var auth: FirebaseAuth? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_my_list_data)
            recyclerView = findViewById(R.id.datalist)
            supportActionBar!!.title = "DataPinjam"
            auth = FirebaseAuth.getInstance()
            MyRecyclerView()
            GetData()
        }

        private fun GetData() {
            Toast.makeText(applicationContext, "Mohon Tunggu Sebentar", Toast.LENGTH_LONG).show()
            val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
            val getReference = database.getReference()
            getReference.child("Admin").child(getUserID).child("DataPinjam")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                        if (datasnapshot.exists()) {
                            dataPinjam.clear()
                            for (snapshot in datasnapshot.children) {
                                val pinjam = snapshot.getValue(data_pinjam::class.java)
                                pinjam?.key = snapshot.key
                                dataPinjam.add(pinjam!!)
                            }
                            adapter = RecyclerViewAdapter(dataPinjam, this@MyListData)
                            recyclerView?.adapter = adapter
                            (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                            Toast.makeText(
                                applicationContext,
                                "Data Berhasil Di Muat",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_LONG)
                            .show()
                        Log.e(
                            "MyListActivity", databaseError.details + " " +
                                    databaseError.message
                        )
                    }
                })
        }

        private fun MyRecyclerView() {
            layoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = layoutManager
            recyclerView?.setHasFixedSize(true)

            val itemDecoration = DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
            itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.line
                )!!
            )
            recyclerView?.addItemDecoration(itemDecoration)
        }

        override fun onDeleteData(data: data_pinjam?, position: Int) {
            var getUserID: String = auth?.getCurrentUser()?.getUid().toString()
            val getReference = database.getReference()
            if (getReference != null) {
                getReference.child("Admin")
                    .child(getUserID)
                    .child("DataPinjam")
                    .child(data?.key.toString())
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@MyListData, "Data berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this@MyListData, "Reference Kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }