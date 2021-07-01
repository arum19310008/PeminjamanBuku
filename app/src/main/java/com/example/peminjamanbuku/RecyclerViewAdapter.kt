package com.example.peminjamanbuku

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class RecyclerViewAdapter (private val listdata_pinjam: ArrayList<data_pinjam>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Judul: TextView
        val Nama: TextView
        val Alamat: TextView
        val NoHP: TextView
        val TanggalPinjam: TextView
        val TanggalKembali: TextView
        val Status: TextView
        val ListItem: LinearLayout

        init {
            Judul = itemView.findViewById(R.id.judul)
            Nama = itemView.findViewById(R.id.nama)
            Alamat = itemView.findViewById(R.id.alamat)
            NoHP = itemView.findViewById(R.id.no_hp)
            TanggalPinjam = itemView.findViewById(R.id.tgl_pjm)
            TanggalKembali = itemView.findViewById(R.id.tgl_kmb)
            Status = itemView.findViewById(R.id.status)
            ListItem = itemView.findViewById(R.id.list_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.view_design,
            parent, false
        )
        return ViewHolder(V)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Judul: String? = listdata_pinjam.get(position).judul
        val Nama: String? = listdata_pinjam.get(position).nama
        val Alamat: String? = listdata_pinjam.get(position).alamat
        val NoHP: String? = listdata_pinjam.get(position).noHp
        val TanggalPinjam: String? = listdata_pinjam.get(position).pinjam
        val TanggalKembali: String? = listdata_pinjam.get(position).kembali
        val Status: String? = listdata_pinjam.get(position).status

        holder.Judul.text = "Judul : $Judul"
        holder.Nama.text = "Nama : $Nama"
        holder.Alamat.text = "Alamat : $Alamat"
        holder.NoHP.text = "No HP : $NoHP"
        holder.TanggalPinjam.text = "Tanggal Pinjam : $TanggalPinjam"
        holder.TanggalKembali.text = "Tanggal Kembali : $TanggalKembali"
        holder.Status.text = "Status : $Status"
        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                holder.ListItem.setOnLongClickListener { view ->
                    val action = arrayOf("Update", "Delete")
                    val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alert.setItems(action, DialogInterface.OnClickListener { dialog, i ->
                        when (i) {
                            0 -> {
                                val bundle = Bundle()
                                bundle.putString("dataJudulBuku", listdata_pinjam[position].judul)
                                bundle.putString("dataNama", listdata_pinjam[position].nama)
                                bundle.putString("dataAlamat", listdata_pinjam[position].alamat)
                                bundle.putString("dataNoHP", listdata_pinjam[position].noHp)
                                bundle.putString("dataTglPinjam", listdata_pinjam[position].pinjam)
                                bundle.putString("dataTglKembali", listdata_pinjam[position].kembali)
                                bundle.putString("dataStatus", listdata_pinjam[position].status)
                                val intent = Intent(view.context, UpdateData::class.java)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }
                            1 -> {
                                listener?.onDeleteData(listdata_pinjam.get(position), position)
                            }
                        }
                    })
                    alert.create()
                    alert.show()
                    true
                }
                return true
            }
        })

    }

    override fun getItemCount(): Int {
        return listdata_pinjam.size
    }

    var listener: dataListener? = null

    init {
        this.context = context
        this.listener = context as MyListData
    }

    interface dataListener {
        fun onDeleteData(data: data_pinjam?, position: Int)
    }
}
