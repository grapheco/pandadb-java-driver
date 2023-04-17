package org.grapheco.pandadb.driver;

import com.google.protobuf.Any;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.grapheco.lynx.types.LynxValue;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import org.grapheco.lynx.lynxrpc.LynxByteBufFactory;
import org.grapheco.lynx.lynxrpc.LynxValueDeserializer;
import org.grapheco.lynx.types.LynxValue;
import org.grapheco.lynx.types.composite.LynxMap;
import org.grapheco.pandadb.network.PandaQueryServiceGrpc;
import org.grapheco.pandadb.network.Query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * @Author renhao
 * @Description:
 * @Data 2023/4/4 18:37
 * @Modified By:
 */
public class JPandaDBDriver {

    public String host;
    public int port;

    public JPandaDBDriver(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = PandaQueryServiceGrpc.newBlockingStub(channel);
    }

    private final ManagedChannel channel;
    private final PandaQueryServiceGrpc.PandaQueryServiceBlockingStub blockingStub;

    public Iterator<HashMap<String, Object>> query(String stat){
        Query.QueryRequest request = Query.QueryRequest.newBuilder().setStatement(stat).build();
        Iterator<Query.QueryResponse> response  = blockingStub.query(request);
        ByteBuf byteBuf = LynxByteBufFactory.getByteBuf();
        LynxValueDeserializer lynxValueDeserializer =new LynxValueDeserializer();
        Iterator<HashMap<String,Object>> resultIter = new Iterator<HashMap<String, Object>>() {
            @Override
            public boolean hasNext() {
                return response.hasNext();
            }
            @Override
            public HashMap<String, Object> next() {
                LynxMap value = (LynxMap) lynxValueDeserializer
                        .decodeLynxValue(byteBuf.writeBytes(response.next().getResultInBytes().toByteArray()));
                return DataConversion.mapToHashMap(value.value());
            }
        };
        return resultIter;
    }
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}
