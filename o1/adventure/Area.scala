package o1.adventure

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. In general, an "area" can be pretty much anything: a room, a building,
  * an acre of forest, or something completely different. What different areas have in
  * common is that players can be located in them and that they can have exits leading to
  * other, neighboring areas. An area also has a name and a description.
  * @param name         the name of the area
  * @param description  a basic description of the area (typically not including information about items) */
class Area(var name: String, var description: String) {

  private val neighbors = Map[String, Area]()
  private var items = Map[String, Item]()
  private var visitors = Map[String, NonPlayer]()

  def containsItem(itemName: String) = {
    this.items.contains(itemName)
  }
  def addItem(item: Item) = {
    this.items += item.name -> item
  }
  def removeItem(itemName: String): Option[Item] = {
    this.items.remove(itemName)
  }
  //methods for nonplayers in area
  def containsNonPlayer(nonPlayer: String) = {
    this.visitors.contains(nonPlayer)
  }
  def addNonPlayer(nonPlayer: NonPlayer) = {
    this.visitors += nonPlayer.name -> nonPlayer
  }
  def removeNonPlayer(nonPlayer: String): Option[NonPlayer] = {
    this.visitors.remove(nonPlayer)
  }
  def nonPlayerDescription(nonPlayer: String) = {
    this.visitors(nonPlayer).description
  }

  def neighbor(direction: String) = this.neighbors.get(direction)

  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction--area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) = {
    this.neighbors ++= exits
  }

  /** Returns a multi-line description of the area as a player sees it. This includes a basic
    * description of the area as well as information about exits and items. The return
    * value has the form "DESCRIPTION\n\nExits available: DIRECTIONS SEPARATED BY SPACES".
    * The directions are listed in an arbitrary order. */
  def fullDescription = {
    val itemList = "\n\nItems in this location:\n- " + this.items.values.mkString("\n- ")
    val nonPlayerList = "\nNonplayers in this location:\n- " + this.visitors.values.mkString("\n- ")
    val exitList = "\nPlaces available:\n- " + this.neighbors.keys.mkString("\n- ")
    if (this.name == "home") {
      this.description + "\n" + "-" * this.name.length + "\n" + nonPlayerList + "\n" + exitList
    } else {
      this.description + "\n" + "-" * this.name.length + itemList + nonPlayerList + exitList
    }
  }


  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)



}
