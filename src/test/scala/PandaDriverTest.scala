import org.grapheco.lynx.types.LynxValue
import org.graphheco.pandadb.client.PandaDBDriver
import org.junit.jupiter.api.Test

class PandaDriverTest {

  //set your pandaDB serverPort and serverHost
  val testConnectionPort = 7601
  val testConnectionHost = "10.0.82.141"

  @Test
  def driverTest(): Unit = {
    val client: PandaDBDriver = new PandaDBDriver(testConnectionHost, testConnectionPort)
    val iter: Iterator[Map[String, LynxValue]] = client.query("Match(n2) Return n2 limit 10;")
    iter.foreach(next => println(next.mkString))
    client.shutdown()
  }
}
