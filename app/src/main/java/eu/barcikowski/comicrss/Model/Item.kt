package eu.barcikowski.comicrss.Model

/**
 * Created by Gaskin's PC on 3/21/2018.
 */


data class Item(val title:String,
                val pubDate:String,
                val link:String,
                val guid:String,
                val author:String,
                val thumbnail:String,
                val description:String,
                val content:String,
                val enclosure:kotlin.Any,
                val categories:List<String>)
