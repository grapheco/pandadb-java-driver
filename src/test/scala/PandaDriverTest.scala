import org.grapheco.lynx.types.structural.{LynxNode, LynxPath, LynxRelationship}
import org.grapheco.pandadb.driver.{DataConversion, JPandaDBDriver, Logging, PandaDBDriver, TimeUtil}
import org.junit.jupiter.api.Test

import java.util.logging.Logger

class PandaDriverTest extends Logging{

  //set your pandaDB serverPort and serverHost
  val testConnectionPort = 7600
  val testConnectionHost = "10.0.82.143"


  def driverTest(cypher:String): Unit = {
    val client: JPandaDBDriver = new JPandaDBDriver(testConnectionHost, testConnectionPort)
    val iter = client.query(cypher)

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
        logger.info((x, tValue))
      })

      client.shutdown()
    }
  }

  def m1(num:Int,cypher:String): Double ={
    var timeT:Long = 0L
    for(item <- 1 to num){
      timeT+=TimeUtil.timingT("",driverTest(cypher))
    }
    timeT/num
  }

  @Test
  def m2(): Unit ={
    val cypher=
      """
        |match  (n:Nucleotide) where n.fasta_seq is not null
        |return n.accn limit 10000
        |""".stripMargin
    logger.info(TimeUtil.millsSecond2Time(m1(10,cypher).toLong))
  }
  @Test
  def m4(): Unit ={
    driverTest(
      """
        |match  (n:Nucleotide) where n.fasta_seq is not null
        |return n.accn limit 10000
        |""".stripMargin)
  }

  @Test
  def m3(): Unit ={
//    driverTest("create index on :Nucleotide(fasta_seq)")
    val c1 =
      """
        |match (n:Nucleotide)-[:taxonomy_nuc]-(t:Taxonomy)-[:produce]-(p:PubMed)
        |where n.accn='NZ_QKVP01000160.1'
        |return n,t,p
        |""".stripMargin
    val c2 =
      """
        |match (t:Taxonomy)-[:produce]-(p:PubMed)
        |where t.accn='1'
        |return t,p
        |""".stripMargin
    val c3 =
      """
        |match (n:Nucleotide)-[:taxonomy_nuc]-(t:Taxonomy)-[:produce]-(p:PubMed)
        |where n.accn='NZ_QKVP01000160.1'
        |return n,t,p
        |""".stripMargin
    driverTest("create index on :Taxonomy(accn)")
    driverTest("create index on :BioProject(accn)")
    driverTest("create index on :BioSample(accn)")
    driverTest("create index on :PubMed(accn)")

  }
}
