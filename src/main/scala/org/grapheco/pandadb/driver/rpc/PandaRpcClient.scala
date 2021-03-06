package org.grapheco.pandadb.driver.rpc

import org.grapheco.pandadb.driver.CypherErrorException
import org.grapheco.pandadb.net.rpc.message.{CreateIndexRequest, CreateIndexResponse, CypherRequest, DropIndexRequest, DropIndexResponse, GetIndexedMetaRequest, GetIndexedMetaResponse, GetStatisticsRequest, GetStatisticsResponse,TransactionCommitRequest, TransactionCommitResponse, TransactionCypherRequest, TransactionRollbackRequest, TransactionRollbackResponse}
import net.neoremind.kraps.RpcConf
import net.neoremind.kraps.rpc.netty.HippoRpcEnvFactory
import net.neoremind.kraps.rpc.{RpcAddress, RpcEnvClientConfig}
import org.grapheco.pandadb.net.rpc.utils.DriverValue

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PandaRpcClient(hostName:String, port: Int, clientName: String, serverName: String) {
  val sparkConf = new RpcConf()
  sparkConf.set("spark.network.timeout", "3700s")
  val config: RpcEnvClientConfig = new RpcEnvClientConfig(sparkConf, clientName)
  val rpcEnv = HippoRpcEnvFactory.create(config)
  var endpointRef = rpcEnv.setupEndpointRef(new RpcAddress(hostName, port), serverName)

  val DURATION_TIME = "3600s"

  def getStatistics(): GetStatisticsResponse ={
    Await.result(endpointRef.askWithBuffer[GetStatisticsResponse](GetStatisticsRequest()), Duration(DURATION_TIME))
  }
  def getIndexedMetaData(): GetIndexedMetaResponse = {
    Await.result(endpointRef.askWithBuffer[GetIndexedMetaResponse](GetIndexedMetaRequest()), Duration(DURATION_TIME))
  }
  def createIndex(label: String, propNames: Seq[String]): CreateIndexResponse = {
    Await.result(endpointRef.askWithBuffer[CreateIndexResponse](CreateIndexRequest(label, propNames)), Duration(DURATION_TIME))
  }
  def dropIndex(label: String, propName: String): DropIndexResponse = {
    Await.result(endpointRef.askWithBuffer[DropIndexResponse](DropIndexRequest(label, propName)), Duration(DURATION_TIME))
  }

  def sendCypherRequest(cypher: String, params:Map[String, Any]): Stream[DriverValue] ={
    val res = endpointRef.getChunkedStream[Any](CypherRequest(cypher, params), Duration(DURATION_TIME))
    res.head match {
      case n: DriverValue => {
        res.asInstanceOf[Stream[DriverValue]]
      }
      case e: String => {
        throw new CypherErrorException(e)
      }
    }
  }

  def sendTransactionCypherRequest(uuid: String, cypher: String, params:Map[String, Any]): Stream[DriverValue] ={
    val res = endpointRef.getChunkedStream[Any](TransactionCypherRequest(uuid, cypher, params), Duration(DURATION_TIME))
    res.head match {
      case n: DriverValue => {
        res.asInstanceOf[Stream[DriverValue]]
      }
      case e: String => {
        throw new CypherErrorException(e)
      }
    }
  }
  def sendTransactionCommitRequest(uuid: String): String ={
    Await.result(endpointRef.askWithBuffer[TransactionCommitResponse](TransactionCommitRequest(uuid)), Duration(DURATION_TIME)).msg
  }
  def sendTransactionRollbackRequest(uuid: String): String ={
    Await.result(endpointRef.askWithBuffer[TransactionRollbackResponse](TransactionRollbackRequest(uuid)), Duration(DURATION_TIME)).msg
  }

  def closeEndpointRef(): Unit ={
    rpcEnv.stop(endpointRef)
  }
  def createEndpointRef(): Unit ={
    endpointRef = rpcEnv.setupEndpointRef(new RpcAddress(hostName, port), serverName)
  }

  def shutdown(): Unit ={
    rpcEnv.stop(endpointRef)
    rpcEnv.shutdown()
  }
}