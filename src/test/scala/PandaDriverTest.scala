import PandaDriverTest.client
import org.grapheco.lynx.types.structural.{LynxNode, LynxPath, LynxRelationship}
import org.grapheco.pandadb.driver.{DataConversion, JPandaDBDriver, Logging, PandaDBDriver, TimeUtil}
import org.junit.jupiter.api.{AfterAll, BeforeAll, Test}

import java.util.logging.Logger

object PandaDriverTest{
  //set your pandaDB serverPort and serverHost
  val testConnectionPort = 7600
  val testConnectionHost = "10.0.82.143"
  var client: PandaDBDriver = null

  @BeforeAll
  def bet(): Unit = {
    client = new PandaDBDriver(testConnectionHost, testConnectionPort)
  }
  @AfterAll
  def after(): Unit ={
    client.shutdown()
  }
}

class PandaDriverTest extends Logging{
  @Test
  def driverTest(): Unit = {
    client.query(
      """
        |create (p:Person1{name:'LiGang'})
        |""".stripMargin)

    val re = client.query(
      """
        |match (p:Person1{name:$name}) return p limit 10
        |""".stripMargin, Map("name"->"LiGang"))
    re.map(x=>x.get("p").get).map(DataConversion.deLynxValue(_)).foreach(println)
  }
}
