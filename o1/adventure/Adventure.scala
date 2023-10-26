package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  val title = "WALL-E"

  private val field       = new Area("field", "Open field of nothing more than sand and dust from the sandstorms. Event the surrounding buildings buildings have got worn out from the harsh storms.")
  private val museum      = new Area("museum", "This was once a museum of ancient history, which supposedly means that the creature's that were displayed here were ancient even for them. \nNow there's only a couple of giant bones left where the exhibitions once were displayed.")
  private val airport     = new Area("airport", "This open area was originally an airport, but when the air got too dusty from the sandstorms it was abandoned and haven't been used since. ")
  private val home        = new Area("home", "This old truck used to serve as a repair workshop for WALL-E robots. \nNowadays it only consists of shelves of spare parts and the solar powered ackumulator that charges WALL-E's batteries. \nTip: Command 'help' for more instructions.")

  val player = new Player(home)

  home.setNeighbors(Vector(     "airport" -> airport                      ))
  airport.setNeighbors(Vector(    "field" -> field,      "home" -> home   ))
  field.setNeighbors(Vector(    "airport" -> airport,  "museum" -> museum ))
  museum.setNeighbors(Vector(     "field" -> field                        ))

  private val trash        = new Item("trash", None, None, None)
  private val nokia        = new Item("phone", Some("mobile phone...? this one must be... very strong... could be useful..."),
                                               Some("Phone; \n~ Model: Nokia 3310 (2000) Dual SIM\n~ Technology: GSM\n~ Battery: Li-Ion 1200 mAh, removable\n~ Features: SMS, calculator, clock, alarm, snake"),
                                               Some("- Oo.. a NOKIA 3310! \n- This was some of the first second-generation mobile phones ever made! And it still works!"))
  private val bone         = new Item("bone",  Some(".. should add this to my collection... a bit heavy.."),
                                               Some("Nasal horn of Styracosaurus from the Cretaceous era"),
                                               Some("- Woah, this must be from a dinosaur. They lived on this planet so long ago, far before we humans destroyed it. That's so cool!"))

  this.field.addItem(trash)
  this.museum.addItem(bone)
  this.museum.addItem(trash)
  this.airport.addItem(nokia)
  this.airport.addItem(trash)

  private val eve = new NonPlayer("eve", "~ Extraterrestrial Vegetation Evaluator a.k.a EVE\n~ Mission: Scan earth for signs of sustainable life and report to Axiom")

  this.airport.addNonPlayer(eve)

  def isComplete = this.player.plantToHuman

  def isOver = this.isComplete || this.player.hasQuit || this.player.dead || this.player.battery == 0

  def welcomeMessage = "Year 2821: Extreme weather conditions and human's overconsumption has long ago made Earth unlivable and overfilled with trash. \n" +
    "So the humans left for space in hope for the Earth to recover from their mistakes. \n" +
    "The only thing they left behind were robots, so called 'Waste Allocation Load-Lifter: Earth-Class' a.k.a. 'WALL-E's to pick up the trash they left behind. \n\n" +
    "With time, the WALL-E's got worn out and one after another they fell apart. \n" +
    "Now there's only one left. You. \n\n" +
    "After so many years rolling around on Earth picking up trash, you started to find some quite interesting items.\n" +
    "So you started keeping a collection back home where you charge your solar powered-batteries.\n\n" +
    "But today is not like every other day.. \n\nYou hear a rumbling and not long after you see a spaceship fly past you. It lands at the old airport and the cargo door opens. \n" +
    "Out of it comes a white, oval-shaped robot. It hovers a bit in the air and seems to be interested in its surroundings, especially the ground. \n\n" +
    "Curiosity strikes you. Who could that be?"

  def goodbyeMessage = {
    if (this.isComplete)
      "You gave the plant to the human! This is the first step of them finally returning to earth! You saved humanity!"
    else if (this.player.battery == 0)
      "Oh no! You didn't get home in time and ran out of battery!"
    else if (this.player.hasQuit)
      "Please come back! How else will humanity be saved?!"
    else
      "GAME OVER"
  }

  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String): String = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    this.player.battery -= 1
    if (this.player.battery == 4) {
      println("! low battery.. should hurry back home.. to charge... \nTurns left before battery runs out: 4\n")
    } else if (this.player.battery == 3) {
      println("! low battery.. should hurry back home.. to charge... \nTurns left before battery runs out: 3\n")
    } else if (this.player.battery == 2) {
      println("! low battery.. should hurry back home.. to charge... \nTurns left before battery runs out: 2\n")
    } else if (this.player.battery == 1) {
      println("! low battery.. should hurry back home.. to charge... \nTurns left before battery runs out: 1\n")
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }

}

