package org.grapheco.pandadb.driver

import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf
import org.grapheco.lynx.lynxrpc.{LynxByteBufFactory, LynxValueDeserializer, LynxValueSerializer}
import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.composite.LynxMap
import org.grapheco.pandadb.network.{PandaQueryServiceGrpc, Query}

import java.util
import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._

/**
 * @Author: Airzihao
 * @Description:
 * @Date: Created at 11:17 2022/4/2
 * @Modified By:
 */
class PandaDBDriver(host: String, port: Int) extends AutoCloseable{

  val channel: ManagedChannel = NettyChannelBuilder.forAddress(host, port).usePlaintext().build();
  val blockingStub: PandaQueryServiceGrpc.PandaQueryServiceBlockingStub = PandaQueryServiceGrpc.newBlockingStub(channel)

  def query(stat: String, params: Map[String, Any] = Map.empty[String, Any] ): Iterator[Map[String, LynxValue]] = {
    val requestBuiler = Query.QueryRequest.newBuilder()
    requestBuiler.setStatement(stat)
    val lynxSerializer: LynxValueSerializer = new LynxValueSerializer
    params.foreach(param => requestBuiler.putParameters(param._1, ByteString.copyFrom(lynxSerializer.encodeAny(param._2))))
    val request: Query.QueryRequest = requestBuiler.build()
    val response: Iterator[Query.QueryResponse] = blockingStub.query(request).asScala
    val byteBuf: ByteBuf = LynxByteBufFactory.getByteBuf
    val lynxValueDeserializer: LynxValueDeserializer = new LynxValueDeserializer
    if (!response.hasNext) {
      //TODO Ugly impl, have to modify the lynx design.
      val str: String = response.mkString
      Iterator(Map("" -> LynxValue(str)))
    } else {
      response.map(resp =>
        lynxValueDeserializer.decodeLynxValue(byteBuf.writeBytes(resp.getResultInBytes.toByteArray)).asInstanceOf[LynxMap].value)
    }
  }
  def run(stat:String,params: java.util.Map[String, Any] = new util.HashMap[String, Any]()): Iterator[Map[String, LynxValue]] = {
    query(stat, params.asScala.toMap)
  }

  def shutdown(): Unit = {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  override def close(): Unit = {
    shutdown()
  }
}
