package org.grapheco.pandadb.driver

import org.apache.log4j.Logger

/**
 * @Author renhao
 * @Description:
 * @Data 2023/2/1 17:18
 * @Modified By:
 */
trait Logging {
  protected lazy val logger = Logger.getLogger(this.getClass)
}