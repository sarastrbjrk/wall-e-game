package o1.adventure

import scala.collection.mutable.Map
import scala.io.StdIn.readLine


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation  = startingArea          // gatherer: changes in relation to the previous location
  private var possessions      = Map[String,Item]()    // gatherer key-value pairs

  private val plant            = new Item("plant", Some("never seen this before..."), Some("\n~ Match!\n~ Marchantia polymorpha, Marchantia of kingdom: Plantae.\n\n~ Contacting... Axiom\n\nWALL-E: o.0\n-----------\nA few moments later the same spacehip from earlier comes into view and lands besides them. The cargo door opens and this time a human approaches them."), None)
  private val human            = new NonPlayer("human", "- So this is what they left behind... Can't believe they let it get like this.")

  private var quitCommandGiven = false
  var plantToHuman             = false                 // wins game
  var dead                     = false                 // loses game
  var battery                  = 14                    // if battery isn't charge -> lose game

  def location = this.currentLocation

  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) "Going to.. " + direction + ".." else "Can't go... " + direction + ".."
  }

  def get(itemName: String) = {
    if (this.location.containsItem(itemName)) {
      if (location.name == "museum" && itemName == "trash") {
        this.location.removeItem(itemName)
        this.currentLocation.addItem(plant)
        // activates sandstorm
        this.sandstorm()
      } else if (itemName == "trash") {
        //trash isn't added to inventory
        this.location.removeItem(itemName)
        "Brrr.. compacting trash... brrrr... finished.\n\nYou picked up all the trash in this location!"
      } else {
      possessions += itemName -> this.location.removeItem(itemName).get
      s".. What is that? Maybe.. I could add it.. to my collection..  \n* " + itemName + " put in inventory *"
      }
    } else {
      s"There is no " + itemName + " here to pick up."
    }
  }

  def drop(itemName: String): String = {
    if (possessions.contains(itemName)) {
      this.currentLocation.addItem(possessions(itemName))
      possessions -= itemName
      s"You drop the " + itemName
    } else {
      s"You don't have that!"
    }
  }

  def has(itemName: String): Boolean = {
    possessions.contains(itemName)
  }

  def scan(itemName: String): String = {
    if (possessions.contains(itemName)) {
      possessions(itemName).walleDescription.get
    } else {
      "I not .. have that.. on me.."
    }
  }

  def inventory: String = {
    if (this.possessions.nonEmpty) {
    s"... I have: \n" + possessions.keys.mkString("\n")
    } else {
    s".. Not carrying.. anything right now.. "
    }
  }

  def charge: String = {
    if (this.currentLocation.name == "home") {
      this.battery = 14
      "battery full! now can... continue journey.."
  } else {
      "can't.. charge here... must go home.. to charge"
    }
  }

  def show(itemName: String, nonPlayer: String): String = {
    val condition = this.has(itemName) && this.currentLocation.containsNonPlayer(nonPlayer)
    val showPlantToEve   = itemName == "plant" && nonPlayer == "eve"
    val showPlantToHuman = itemName == "plant" && nonPlayer == "human"
    if (condition && showPlantToEve) {
      this.currentLocation.addNonPlayer(human)
      s"EVE: \n${possessions(itemName).eveDescription.get}"
    } else if (condition && showPlantToHuman) {
        //activates final dilemma and potential boss-fight
        this.finalDilemma()
    } else if (condition && nonPlayer == "eve") {
        s"EVE: \n~ Analyzing... ${possessions(itemName).eveDescription.get}"
    } else if (condition && nonPlayer == "human") {
        s"Human: \n${possessions(itemName).humanDescription.get}"
    } else if ( condition && nonPlayer == "error") {
        "Wrong command... don't understand.. what to do.."
    } else {
        "Can't... show this to anyone.. right now..."
    }
  }

  def interact(nonPlayer: String) = {
    if (currentLocation.containsNonPlayer(nonPlayer) && nonPlayer == "eve") {
      s"EVE: \n${this.currentLocation.nonPlayerDescription(nonPlayer)}\n~ Analyzing robot... ~\n~ Waste Allocation Load-Lifter: Earth-Class a.k.a. WALL-E ~\n" +
      "~ Mission: Pick up and compact metal trash ~\n\nWALL-E: \no.O how does it know..? don't understand... sus-tainable life..?"
    } else if (currentLocation.containsNonPlayer(nonPlayer) && nonPlayer == "human") {
      s"human: \n${this.currentLocation.nonPlayerDescription(nonPlayer)} \n" +
      "\n- EVE! You found something?\n\nEVE:\n~ Yes, the robot over there has a Marchantia Polymorpha on them ~\n\nHuman: \nReally?? Is that possible? Robot.. do you have it on you?"
    } else {
      "No one.. to interact with here... or wrong command.."
    }
  }
  //Activated when player picks up plant in museum. Reveals the plant needed to win the game.
  def sandstorm() = {
    println("Brrr.. compacting trash... brrrr... finished.\n")
    println("Oh no! A sandstorm is coming this way! Hide quickly so you don't get caught in it!\n\n")
    val command = readLine("Command: ")
    if (command == "hide") {
      "\nPhew! now must only.. wait until over... \n***** \nclear now.. safe to go outside.. \n\n... what is .. this? never.. seen before.. sandstorm must have revealed it.."
    } else {
      this.dead = true
      "(X.X)"
    }
  }
  // final dilemma and potential boss-fight. Different endings depending on the choices the player makes (and have made earlier))
  def finalDilemma(): String = {
    println("Human: \n- Woahh... I never thought this could actually be possible. \n- I can't believe it's true what they said. We can return to Earth!")
    println("- If you would just give it to me so I can take it back to our laboratory and we can examine it further.\n\n")
    val command = readLine("Do you give the plant to the human? ")
    if (command == "yes") {
      this.plantToHuman = true
      "/^.^/!"
    } else {
      println("- Why wouldn't you give it to me?? Hmpf, I guess I'll just take it from your inactivated body!\n")
      val notGiveCommand = readLine("fight or run?")
      if (notGiveCommand == "fight") {
          if (this.has("phone")) {
            this.dead = true
            "You knock the human out with the Nokia phone. \n" +
            "This makes EVE deactivate you and take the plant from your powerless body."
          } else {
            this.dead = true
            "You didn't have anything on you to fight the human with so the human deactivates you in seconds."
          }
      } else if (notGiveCommand == "run") {
        "You run. Since you know the area well and the human isn't used to the in the gravitational force of Earth, you get away. But at what cost?"
      } else {
        this.dead = true
        "The human inactivates you before you can do anything."
      }
    }
  }

  def help(): String = {
    "brr. I forgot.. what to do.. must refresh memory..\n" +
      "Me: Waste Allocation Load-Lifter: Earth-Class a.k.a. WALL-E. \n" +
      "Mission: Pick up and compact metal trash. \n" +
      "Commands:\n" +
      "- go AREA: go to another location that is currently avaliable \n" +
      "- get ITEM: pick up and put item in inventory \n" +
      "- drop ITEM: drop item from inventory on the ground in current location \n" +
      "- scan ITEM: get description of item in invenotry \n" +
      "- inventory: list all items in inventory \n" +
      "- charge: charges WALL-E's batteries when at home\n" +
      "- interact NONPLAYER: interacts with nonplayer in the same location \n" +
      "- show ITEM to NONPLAYER: get nonplayer's description of item  \n" +
      "- quit: end game \n" +
      "+ additinal commands that work in certain conditions"
  }


  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}


