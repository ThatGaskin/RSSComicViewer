package eu.barcikowski.comicrss.Model

/**
 * Created by Gaskin's PC on 3/21/2018.
 */
data class RSSObject(val status:String, val feed:Feed, val items:List<Item>)