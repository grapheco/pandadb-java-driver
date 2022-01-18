import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import scala.collection.JavaConverters._
/**
 * @program: pandadb-java-driver
 * @description:
 * @author: LiamGao
 * @create: 2022-01-18 15:00
 */
object DriverTest {
  def main(args: Array[String]): Unit = {
    val driver = GraphDatabase.driver("panda://10.0.82.144:9989", AuthTokens.basic("pandadb", "pandadb"))
    val session = driver.session()
    val res = session.run("match (n) return n limit 10")
    var count = 0
    while (res.hasNext){
      count += 1
      val data = res.next()
      println(data.get("n").asEntity().asMap().asScala.toList)
    }
    session.close()
    driver.close()
  }
}
