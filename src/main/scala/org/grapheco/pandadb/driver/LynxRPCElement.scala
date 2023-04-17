package org.grapheco.pandadb.driver

import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.property.LynxInteger
import org.grapheco.lynx.types.structural.{LynxElement, LynxId, LynxNodeLabel, LynxPropertyKey}

/**
 * @Author renhao
 * @Description:
 * @Data 2023/2/18 01:10
 * @Modified By:
 */
case class LynxNodeId(value: Long) extends LynxId {
  override def toLynxInteger: LynxInteger = LynxInteger(value)
}

case class LynxRPCNode(longId: Long, labels: Seq[LynxNodeLabel], props: Seq[(String, LynxValue)]) extends LynxElement{

  val properties: Map[String, LynxValue] = props.toMap
  val id: LynxId = LynxNodeId(longId)
  def property(name: String): Option[LynxValue] = properties.get(name)

  override def toString: String = s"{<id>:${id.value}, labels:[${labels.mkString(",")}], properties:{${properties.map(kv=>kv._1+": "+kv._2.value.toString).mkString(",")}}"

  def keys: Seq[LynxPropertyKey] = props.map(pair => LynxPropertyKey(pair._1))

  def property(propertyKey: LynxPropertyKey): Option[LynxValue] = properties.get(propertyKey.value)
}

case class LynxRPCRelationship(id: Long, from: Long, to: Long, relType:String,props: Seq[(String, LynxValue)]) extends LynxElement {
  val properties: Map[String, LynxValue] = props.toMap
  def keys: Seq[LynxPropertyKey] = props.map(pair => LynxPropertyKey(pair._1))

  def property(propertyKey: LynxPropertyKey): Option[LynxValue] = properties.get(propertyKey.value)
  override def toString: String = s"{<id>:${id}, from:${from}, to:${to},relType:[${relType}], properties:{${props.map(kv=>kv._1+": "+kv._2).mkString(",")}}"
}
