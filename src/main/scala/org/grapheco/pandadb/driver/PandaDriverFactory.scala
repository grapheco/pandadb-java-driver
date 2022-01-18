package org.grapheco.pandadb.driver

import org.neo4j.driver.v1.Value

class PandaDriverFactory(uriAuthority: String, authtoken: java.util.Map[String, Value], config: PandaDriverConfig) {

  def newInstance(): PandaDriver ={
    new PandaDriver(uriAuthority, config)
  }

}
