package pstoltz.rockpaperscissors.gameengine.model
import org.specs2._
import org.specs2.mutable.SpecificationLike

class EngineModelSpec extends SpecificationLike {
  isolated
  sequential
  
  "the Rock, Paper, Scissors model" should {

    "define that Rock has the name rock and is beaten by Paper" >> {
      Rock.name mustEqual "rock"
      Rock.isBeatenBy(Paper) mustEqual Some(true)
    }

    "define that Paper has the name paper and is beaten by Scissors" >> {
      Paper.name mustEqual "paper"
      Paper.isBeatenBy(Scissors) mustEqual Some(true)
    }

    "define that Scissors has the name scissors and is beaten by Rock" >> {
      Scissors.name mustEqual "scissors"
      Scissors.isBeatenBy(Rock) mustEqual Some(true)
    }
    
    "define that a Hand is not beaten by nor beats the same Hand" >> {
    	Rock.isBeatenBy(Rock) mustEqual None
    	Paper.isBeatenBy(Paper) mustEqual None
      Scissors.isBeatenBy(Scissors) mustEqual None
    }
  }
}
