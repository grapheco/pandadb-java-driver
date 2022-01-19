# PandaDB-Java-Driver

pandadb-java-driver can easily switch to neo4j driver or pandadb driver, all the user needs to do is change the connection schema to `bolt` or `panda`.

## License
pandadb-java-driver is under the Apache 2.0 license. More detail see [LICENSE](https://github.com/grapheco/pandadb-java-driver/blob/master/LICENSE)

## Building PandaDB-Java-Driver
```
mvn package
```

## Usage
```
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

public class DriverDemo {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("panda://127.0.0.1:9989", AuthTokens.basic("", ""));
        // Driver driver = GraphDatabase.driver("panda://127.0.0.1:9989,127.0.0.2:9989,127.0.0.3:9989", AuthTokens.basic("", ""));

        Session session = driver.session();
        StatementResult result1 = session.run("match (n) return n limit 10");
        while (result1.hasNext()){
            Record next = result1.next();
            Node n = next.get("n").asNode();
            System.out.print("node labels: ");
            n.labels().iterator().forEachRemaining(System.out::println);
            System.out.println("node properties: " + n.asMap());
            System.out.println();
        }

        StatementResult result2 = session.run("match (n)-[r]->(m) return r limit 10");
        while (result2.hasNext()){
            Record next = result2.next();
            Relationship r = next.get("r").asRelationship();
            System.out.println("rel type: " + r.type());
            System.out.println("rel start node id: " + r.startNodeId());
            System.out.println("rel end node id: " + r.endNodeId());
            System.out.println("rel properties: " + r.asMap());
            System.out.println();
        }

        session.close();
        driver.close();
    }
}

```

## Limitation
when connected to pandadb, only support `session.run()` method to run cypher.