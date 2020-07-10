package pstoltz.rockpaperscissors.gameui

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try
import scala.annotation.tailrec
import pstoltz.rockpaperscissors.gameui.model._

/**
 * This object defines the user interface of
 * Rock Paper Scissors
 */
object GameUI {

  private val gameModePrompt = """
    |Welcome to Rock, Paper, Scissors game !
    |Please choose the mode, press :
    |1 for Human vs Computer mode 
    |2 for Computer vs Computer mode
    |Any key to quit
    |""".stripMargin

  private val actionAfterRoundPrompt = """
    |Press :
    |n for new game
    |q to quit 
    |Any key to continue""".stripMargin

  private val goodByePrompt = "GoodBye!"

  /**
   * Starts the game
   */
  def start = chooseModeAndGetPlayers match {
    case None                     => stop
    case Some((player1, player2)) => startNewGame(player1, player2)
  }

  /**
   * Stops the game
   */
  def stop = {
    println(goodByePrompt)
    System.exit(0)
  }

  private def chooseModeAndGetPlayers: Option[(Player, Player)] = {
    getUserChoiceForMode match {
      case Some("1") => Some((Human, Computer))
      case Some("2") => Some((Computer, Computer))
      case _         => None
    }
  }

  @tailrec
  private def executeRound(firstPlayer: Player, secondPlayer: Player): Unit = {
    playRoundAndPrintScores(firstPlayer, secondPlayer)
    getUserChoiceAfterRound match {
      case Some(character) if character == "q" => stop
      case Some(character) if character == "n" => start
      case _                                   => executeRound(firstPlayer, secondPlayer)
    }
  }

  private def startNewGame(player1: Player, player2: Player) = {
    Scores.init(player1, player2)
    executeRound(player1, player2)
  }

  private def getUserChoiceAfterRound = {
    println(actionAfterRoundPrompt)
    UIUtil.getUserChoice
  }

  private def getUserChoiceForMode = {
    println(gameModePrompt)
    UIUtil.getUserChoice
  }

  private def playRoundAndPrintScores(firstPlayer: Player, secondPlayer: Player) = {
    Await.result(Round(firstPlayer, secondPlayer).run, Duration.Inf)
    Scores.print
  }
}
/**
 * Utility object for UIs
 */
object UIUtil {
  /**
   * Returns an optional character
   * from the Console prompt
   */
  def getUserChoice = {
    Try(scala.io.StdIn.readLine()).toOption
  }
}