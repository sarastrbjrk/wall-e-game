package o1.adventure

/**
  * @param name                the item's name
  * @param walleDescription    the item's description from WALL-E's perspective
  * @param eveDescription      the item's description from EVE's perspective
  * @param humanDescription    the item's description from a human's perspective
  * */


class Item(val name: String, val walleDescription: Option[String], val eveDescription: Option[String], val humanDescription: Option[String]) {

  override def toString = this.name

}