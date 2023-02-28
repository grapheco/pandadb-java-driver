package org.grapheco.pandadb.driver

import io.grpc.netty.NettyChannelBuilder
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf
import org.grapheco.lynx.lynxrpc.{LynxByteBufFactory, LynxValueDeserializer}
import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.composite.LynxMap
import org.grapheco.pandadb.network.{PandaQueryServiceGrpc, Query}

import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._

/**
 * @Author: Airzihao
 * @Description:
 * @Date: Created at 11:17 2022/4/2
 * @Modified By:
 */
class PandaDBDriver(host: String, port: Int) {

  val channel = NettyChannelBuilder.forAddress(host, port).usePlaintext().build();
  val blockingStub = PandaQueryServiceGrpc.newBlockingStub(channel)

  def query(stat: String): Iterator[Map[String, LynxValue]] = {
    val request: Query.QueryRequest = Query.QueryRequest.newBuilder().setStatement(stat).build()
    val response: Iterator[Query.QueryResponse] = blockingStub.query(request).asScala
    val byteBuf: ByteBuf = LynxByteBufFactory.getByteBuf
    val lynxValueDeserializer: LynxValueDeserializer = new LynxValueDeserializer
    if(!response.hasNext) {
      //Ugly impl, have to modify the lynx design.
      val str: String = response.mkString
      Iterator(Map("" -> LynxValue(str)))
    } else {
      response.map(resp =>
        lynxValueDeserializer.decodeLynxValue(byteBuf.writeBytes(resp.getResultInBytes.toByteArray))
          .asInstanceOf[LynxMap].value)
    }
  }

  def shutdown(): Unit = {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

}
