import org.grapheco.lynx.types.structural.{LynxNode, LynxPath, LynxRelationship}
import org.grapheco.pandadb.driver.{DataConversion, JPandaDBDriver, PandaDBDriver}
import org.junit.jupiter.api.Test

class PandaDriverTest {

  //set your pandaDB serverPort and serverHost
  val testConnectionPort = 7601
  val testConnectionHost = "10.0.82.143"

  @Test
  def driverTest(): Unit = {
    val client: JPandaDBDriver = new JPandaDBDriver(testConnectionHost, testConnectionPort)
    val iter = client.query(
      """
        |match (p:person) return p limit 20
        |""".stripMargin)

    while (iter.hasNext) {
      val mapR = iter.next()
      mapR.keySet().forEach(x => {
        val value = mapR.get(x)

        var tValue = value match {
          case v: LynxNode => DataConversion.toLynxRPCNode(v).toString
          case v: LynxRelationship => DataConversion.toLynxRPCRelationship(v).toString
          case v: LynxPath => DataConversion.tLynxPath(v).map(x => x.toString)
          case _ => value
        }

        if (value.isInstanceOf[LynxPath]) {
          tValue = DataConversion.tLynxPath(value.asInstanceOf[LynxPath]).map(x => x.toString)
        }
        println((x, tValue))
      })

      client.shutdown()
    }
  }
}
