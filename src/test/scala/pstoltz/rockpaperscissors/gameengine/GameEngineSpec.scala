package pstoltz.rockpaperscissors.gameengine
import org.specs2._
import org.specs2.mutable.SpecificationLike
import pstoltz.rockpaperscissors.gameengine.model._

class GameEngineSpec extends SpecificationLike{
 isolated
 sequential
   "GameEngine" should {
     
    "return Some(true) when firstHand wins over secondHand" >> {
      GameEngine.round(Rock, Scissors) mustEqual Some(true)
      GameEngine.round(Scissors, Paper) mustEqual Some(true)
      GameEngine.round(Paper, Rock) mustEqual Some(true)
    }
    
    "return Some(false) when firstHand is beaten by secondHand" >> {
      GameEngine.round(Scissors, Rock) mustEqual Some(false)
      GameEngine.round(Paper, Scissors) mustEqual Some(false)
      GameEngine.round(Rock, Paper) mustEqual Some(false)
    }
    
    "return None when firstHand and secondHand are identical" >> {
      GameEngine.round(Scissors, Scissors) mustEqual None
      GameEngine.round(Paper, Paper) mustEqual None
      GameEngine.round(Rock, Rock) mustEqual None
    }
  }
}
