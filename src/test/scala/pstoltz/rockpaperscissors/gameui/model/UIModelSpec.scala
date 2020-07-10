package pstoltz.rockpaperscissors.gameui.model

import java.io.ByteArrayOutputStream
import java.io.StringReader

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import org.specs2.mutable.SpecificationLike

import pstoltz.rockpaperscissors.gameengine.model._

class UIModelSpec extends SpecificationLike {
  isolated
  sequential

  val computerModeScoreInit = s"""|Scores : 
                      	          | Computer : 0
                      	          | Computer : 0
                      	          | Ties : 0""".stripMargin

  val scorePlayer1Wins = s"""|Scores : 
                      	     | Computer : 1
                      	     | Computer : 0
                      	     | Ties : 0""".stripMargin

  val scorePlayer2WinsAfterPlayer1 = s"""|Scores : 
                          	             | Computer : 1
                          	             | Computer : 1
                          	             | Ties : 0""".stripMargin

  val scoreTieAfterBothPlayerWon = s"""|Scores : 
                          	           | Computer : 1
                          	           | Computer : 1
                          	           | Ties : 1""".stripMargin

  "the UI model" should {
    "define a Human Player who knows how to play" >> {
      Human.toString() mustEqual "Human"
      val input = new StringReader("0")
      val outCapture = new ByteArrayOutputStream
      val errCapture = new ByteArrayOutputStream
      Console.withIn(input) {
        Console.withOut(outCapture) {
          Console.withErr(errCapture) {
            Await.result(Human.play, Duration.Inf) mustEqual Rock
          }
        }
      }
    }

    "define a Computer Player who knows how to play" >> {
      Computer.toString() mustEqual "Computer"
      Hand.getHandsChoice must contain(Await.result(Computer.play, Duration.Inf))
    }

    "define a Round that let the players play and check the Scores" >> {
      val player1 = Computer
      val player2 = Computer
      Scores.init(player1, player2)
      Await.result(Round(player1, player2).run, Duration.Inf)
      //we must have at least a 1 in the Scores either
      //player1 player2 or Tie
      Scores.getScores must contain("1")
    }

    "resetting the Scores should set O to its indicators" >> {
      val player1 = Computer
      val player2 = Computer
      Scores.init(player1, player2)
      Await.result(Round(player1, player2).run, Duration.Inf)
      Scores.init(player1, player2)
      Scores.getScores must beEqualTo(computerModeScoreInit)
    }

    "saving Scores should work depending on a round result" >> {
      val player1 = Computer
      val player2 = Computer
      Scores.init(player1, player2)
      Scores.save(Some(true))
      Scores.getScores must beEqualTo(scorePlayer1Wins)
      Scores.save(Some(false))
      Scores.getScores must beEqualTo(scorePlayer2WinsAfterPlayer1)
      Scores.save(None)
      Scores.getScores must beEqualTo(scoreTieAfterBothPlayerWon)
    }
  }
}
