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
    val driver = GraphDatabase.driver("panda://127.0.0.1:9989", AuthTokens.basic("", ""))
    val session = driver.session()
    val res = session.run("match (n) return n limit 10")
    while (res.hasNext){
      val data = res.next()
      println(data.get("n").asEntity().asMap().asScala.toList)
    }
    session.close()
    driver.close()
  }
}
