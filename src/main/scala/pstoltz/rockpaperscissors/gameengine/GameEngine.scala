package pstoltz.rockpaperscissors.gameengine

import pstoltz.rockpaperscissors.gameengine.model.Hand

object GameEngine {
  /**
   * Returns Some(true) if firstHand beats secondHand
   *         Some(false)if secondHand beats firstHand
   *         None if both hands are equal
   */
  def round(firstHand : Hand, secondHand : Hand) : Option[Boolean] = {
    secondHand.isBeatenBy(firstHand)
  }
}