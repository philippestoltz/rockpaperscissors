package pstoltz.rockpaperscissors.gameui.model

import pstoltz.rockpaperscissors.gameui.UIUtil
import pstoltz.rockpaperscissors.gameengine._
import pstoltz.rockpaperscissors.gameengine.model._
import scala.util.Try
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.annotation.tailrec

/**
 * defines a Player
 */
sealed trait Player {
  /**
   * Defines the input function
   */
  protected def inputFunc: Int

  /**
   * Returns a Future[Hand] given the inputFunc of the Player
   */
  def play: Future[Hand] = Future {
    val chosenHand = Hand.getHandsChoice(inputFunc)
    println(s"$this choice is $chosenHand")
    chosenHand
  }

  /**
   * Returns the name of this Player
   */
  override def toString = "player"
}

/**
 * Defines a Human Player
 */
case object Human extends Player {

  override def inputFunc: Int = promptUntilValidMove

  override def toString = "Human"

  @tailrec
  private def promptUntilValidMove: Int = {
    getUserChoiceForMove match {
      case Some(choice) if (0 until Hand.getHandsChoice.length) contains choice => choice
      case _ => promptUntilValidMove
    }
  }

  private def getUserChoiceForMove = {
    println(movePrompt)
    UIUtil.getUserChoice.flatMap(str => Try(str.toInt).toOption)
  }

  private val movePrompt = {
    val choiceStartPrompt = "Please make your choice. Press :\n"
    val choicePrompt = Hand.getHandsChoice.zipWithIndex.map {
      case (element, index) => s"${index} for ${element}"
    }.mkString("\n")

    choiceStartPrompt + choicePrompt
  }
}

/**
 * Defines a Computer Player
 */
case object Computer extends Player {
  override def inputFunc: Int = scala.util.Random.nextInt(Hand.getHandsChoice.length)
  override def toString = "Computer"
}

/**
 * Defines a Round
 *
 * @constructor create a new Round with 2 players
 * @param firstPlayer
 * @param secondPlayer
 */
case class Round(firstPlayer: Player, secondPlayer: Player) {

  /**run this Round**/
  def run = {
    val handsF = for {
      first <- firstPlayer.play
      second <- secondPlayer.play
    } yield (first, second)

    handsF.map { hands =>
      val firstPlayerWins = GameEngine.round(hands._1, hands._2)
      Scores.save(firstPlayerWins)
      printResult(firstPlayerWins, hands._1, hands._2)
    }
  }

  /**
   * Print the result of the round
   * like who beats who
   * @param firstPlayerWins
   * 	 optional boolean representing the result of a Round
   * @param firstHand
   * @param secondHand
   */
  private def printResult(firstPlayerWins: Option[Boolean], firstHand: Hand, secondHand: Hand) = firstPlayerWins match {
    case Some(true)  => println(s"$firstPlayer ($firstHand) beats $secondPlayer ($secondHand) !")
    case Some(false) => println(s"$secondPlayer ($secondHand) beats $firstPlayer ($firstHand) !")
    case None        => println(s"Tie ! Both players chose $firstHand")
  }
}

/**
 * defines a Scores "board" for the Game
 */
object Scores {

  private var player1: Player = Human
  private var player2: Player = Computer
  private var player1Score: Int = 0
  private var player2Score: Int = 0
  private var ties: Int = 0

  /**
   * Saves the scores
   * @param firstPlayerWins
   * 				optional boolean representing the result of a Round
   */
  def save(firstPlayerWins: Option[Boolean]) = {
    firstPlayerWins match {
      case Some(true)  => player1Score += 1
      case Some(false) => player2Score += 1
      case None        => ties += 1
    }
  }

  /**
   * Initialize the scores with new players
   * Resets the scores
   * @param player1
   * @param player2
   *
   */
  def init(player1: Player, player2: Player) = {
    this.player1 = player1
    this.player2 = player2
    player1Score = 0
    player2Score = 0
    ties = 0
  }

  /**
   * Print the scores
   */
  def print = {
    println(getScores)
  }
  
  def getScores = {
    s"""|Scores : 
  	    | $player1 : $player1Score
  	    | $player2 : $player2Score
  	    | Ties : $ties""".stripMargin
  }

  /**
   * Saves the scores and print them
   * @param firstPlayerWins
   * 				optional boolean representing the result of a Round
   */
  def saveAndPrint(firstPlayerWins: Option[Boolean]) = {
    save(firstPlayerWins)
    print
  }
}
