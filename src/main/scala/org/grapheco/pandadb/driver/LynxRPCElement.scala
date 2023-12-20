package org.grapheco.pandadb.driver

import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.property.LynxInteger
import org.grapheco.lynx.types.structural.{LynxId, LynxNode, LynxNodeLabel, LynxPropertyKey, LynxRelationship, LynxRelationshipType}

/**
 * @Author renhao
 * @Description:
 * @Data 2023/2/18 01:10
 * @Modified By:
 */
case class LynxNodeId(value: Long) extends LynxId {
  override def toLynxInteger: LynxInteger = LynxInteger(value)
}

case class PandaNode(longId: Long, labels: Seq[LynxNodeLabel], props: Seq[(String, LynxValue)]) extends LynxNode {
  lazy val properties: Map[String, LynxValue] = props.toMap
  override val id: LynxId = LynxNodeId(longId)
  def property(name: String): Option[LynxValue] = properties.get(name)

  override def toString: String = s"{<id>:${id.value}, labels:[${labels.mkString(",")}], properties:{${properties.map(kv=>kv._1+": "+kv._2.value.toString).mkString(",")}}"

  override def keys: Seq[LynxPropertyKey] = props.map(pair => LynxPropertyKey(pair._1))

  override def property(propertyKey: LynxPropertyKey): Option[LynxValue] = properties.get(propertyKey.value)
}

case class PandaRelationship(_id: Long, startId: Long, endId: Long, relationType: Option[LynxRelationshipType],
                             props: Seq[(String, LynxValue)]) extends LynxRelationship {
  lazy val properties = props.toMap
  override val id: LynxId = LynxRelationshipId(_id)
  override val startNodeId: LynxId = LynxNodeId(startId)
  override val endNodeId: LynxId = LynxNodeId(endId)

  override def property(propertyKey: LynxPropertyKey): Option[LynxValue] = properties.get(propertyKey.value)

  override def toString: String = s"{<id>:${id}, from:${startId}, to:${endId},relType:[${relationType.get.value}], properties:{${props.map(kv=>kv._1+": "+kv._2).mkString(",")}}"
  override def keys: Seq[LynxPropertyKey] = props.map(pair => LynxPropertyKey(pair._1))
}

case class LynxRelationshipId(value: Long) extends LynxId {
  override def toLynxInteger: LynxInteger = LynxInteger(value)
}
