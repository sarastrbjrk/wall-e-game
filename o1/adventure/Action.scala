package o1.adventure


/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim

  def execute(actor: Player): Option[String] = {
    if (this.verb == "show") {
      val secondWord = this.modifiers.takeWhile( _ != ' ')
      val lastWordIndex = this.commandText.lastIndexOf(' ') + 1
      val lastWord = this.commandText.takeRight(input.length - lastWordIndex)
      this.verb match {
        case "show"       => Some(actor.show(secondWord,lastWord)) //method using two inputs
      }
    } else {
      this.verb match {
        case "go"         => Some(actor.go(this.modifiers))
        case "get"        => Some(actor.get(this.modifiers))
        case "drop"       => Some(actor.drop(this.modifiers))
        case "scan"       => Some(actor.scan(this.modifiers))
        case "interact"   => Some(actor.interact(this.modifiers))
        case "inventory"  => Some(actor.inventory)
        case "charge"     => Some(actor.charge)
        case "quit"       => Some(actor.quit())
        case "help"       => Some(actor.help())
        case other        => None
      }
    }
  }


  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"


}

