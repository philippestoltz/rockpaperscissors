package pstoltz.rockpaperscissors.gameengine.model

import scala.collection.Seq

/**This trait defines a Hand */
sealed trait Hand {
  /**Returns the name of the Hand*/
  def name: String
  
  /**Returns a list of Hands beats this Hand*/
  def superiorHands: Seq[Hand]
  
  /**
   * Returns Some(true) when this Hand is beaten by that Hand
   * 				 Some(false) when this Hand beats that Hand
   * 				 None when this == that
   */
  def isBeatenBy(that: Hand): Option[Boolean] = (this, that) match {
    case (a, b) if a == b => None
    case _                => Some(superiorHands.contains(that))
  }
  
  override def toString = name
}

/**companion object*/
object Hand {
  /**returns the hands choice as a List*/
  val getHandsChoice : Seq[Hand] = Seq(Rock, Paper, Scissors)
}

//define the different hands
case object Rock extends Hand {
  override val name = "rock"
  override val superiorHands = Seq(Paper)
}

case object Paper extends Hand {
  override val name = "paper"
  override val superiorHands = Seq(Scissors)
}

case object Scissors extends Hand {
  override val name = "scissors"
  override val superiorHands = Seq(Rock)
}
