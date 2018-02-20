package solutions

import scala.annotation.tailrec

object Day9 {

  val bangMatch = "^!.(.*)$".r
  val garbageStartMatch = "^<(.*)$".r
  val garbageEndMatch = "^>(.*)$".r
  val openBracketMatch = "^\\{(.*)$".r
  val closeBracketMatch = "^}(.*)$".r
  val anyMatch = "^.(.*)".r

  def removeGarbage(input: String): (String, Int) = {

    @tailrec
    def loop(s: String, result: String, totalGarbage: Int): (String, Int) = {
      s match {
        case "" => (result, totalGarbage)
        case openBracketMatch(x) => loop(x, result + "{", totalGarbage)
        case closeBracketMatch(x) => loop(x, result + "}", totalGarbage)
        case bangMatch(x) => loop(x, result, totalGarbage)
        case garbageStartMatch(x) =>
          val (rest, garbage) = garbageLoop(x, 0)
          loop(rest, result, totalGarbage + garbage)
        case anyMatch(x) => loop(x, result, totalGarbage)
      }
    }

    @tailrec
    def garbageLoop(s: String, totalGarbage: Int): (String, Int) = {
      s match {
        case bangMatch(x) => garbageLoop(x, totalGarbage)
        case garbageEndMatch(x) => (x, totalGarbage)
        case anyMatch(x) => garbageLoop(x, totalGarbage + 1)
      }
    }

    loop(input, "", 0)
  }

  def getValue(input: String): (Int, Int) = {

    @tailrec
    def loop(rest: String, value: Int, total: Int): Int = {
      rest match {
        case "" => total
        case openBracketMatch(x) => loop(x, value + 1, total)
        case closeBracketMatch(x) => loop(x, value - 1, total + value)
      }
    }

    val (cleaned, totalGarbage) = removeGarbage(input)
    (loop(cleaned, 0, 0), totalGarbage)
  }
}
