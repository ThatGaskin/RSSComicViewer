package eu.barcikowski.comicrss.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import eu.barcikowski.comicrss.Interface.ItemClickListener
import eu.barcikowski.comicrss.Model.RSSObject
import eu.barcikowski.comicrss.R

/**
 * Created by Gaskin's PC on 3/21/2018.
 */


class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener
{

    var txtTitle:TextView = itemView.findViewById<TextView>(R.id.txtTitle) as TextView
    var txtPubdate:TextView = itemView.findViewById<TextView>(R.id.txtPubdate) as TextView
    var txtContent:TextView = itemView.findViewById<TextView>(R.id.txtContent) as TextView
    var imgView:ImageView = itemView.findViewById<ImageView>(R.id.imageView) as ImageView

    private var itemClickListener : ItemClickListener?=null

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v!!, adapterPosition,true);
        return true
    }

    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v!!, adapterPosition,false);
    }

}

class FeedAdapter(private val rssObject: RSSObject, private val mContext:Context): RecyclerView.Adapter<FeedViewHolder>() {

    private val inflater:LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.txtTitle.text = rssObject.items[position].title
        holder.txtContent.text = rssObject.items[position].content.substringAfter("alt=").substringBefore(">")
        holder.txtPubdate.text = rssObject.items[position].pubDate
        Picasso.with(mContext).load(Uri.parse(rssObject.items[position].thumbnail)).into(holder.imgView)

        holder.setItemClickListener(ItemClickListener{ view, position, isLongClick ->
            if(!isLongClick){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.items[position].link))
                mContext.startActivity(browserIntent)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row,parent,false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rssObject.items.size
    }

}