package org.grapheco.pandadb.driver

import org.grapheco.lynx.cypherplus.LynxBlob
import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.composite.{LynxList, LynxMap}
import org.grapheco.lynx.types.property.{LynxBoolean, LynxFloat, LynxInteger, LynxString}
import org.grapheco.lynx.types.structural.{LynxElement, LynxNode, LynxPath, LynxRelationship}

import scala.collection.mutable

/**
 * @Author renhao
 * @Description:
 * @Data 2023/2/18 01:21
 * @Modified By:
 */
object DataConversion {

  def mapNode(node: LynxNode): PandaNode ={
    val props = node.keys.map(x => (x.value, node.property(x).get))
    PandaNode(node.id.value.toString.toLong, node.labels, props)
  }

  def mapRelation(lynxRelationship: LynxRelationship): PandaRelationship = {
    val props = lynxRelationship.keys.map(x => (x.value, lynxRelationship.property(x).get))
    PandaRelationship(lynxRelationship.id.toLynxInteger.value
      , lynxRelationship.startNodeId.toLynxInteger.value
      , lynxRelationship.endNodeId.toLynxInteger.value
      , lynxRelationship.relationType, props)
  }

  def mapLynxPath(lynxPath: LynxPath): LynxPath ={
    val elements: Seq[LynxElement] = lynxPath.elements.map(element => {
      element match {
        case r: LynxNode => mapNode(r)
        case r: LynxRelationship => mapRelation(r)
      }
    })
    LynxPath(elements)
  }

  def deLynxValue(lynxValue: LynxValue): Any = {
    lynxValue match {
      case r: LynxPath => mapLynxPath(r)
      case r: LynxInteger => r.value
      case r: LynxFloat => r.value
      case r: LynxString => r.value
      case r: LynxBoolean => r.value
      case r: LynxList => List(r.value.map(x => deLynxValue(x)))
      case r: LynxMap => r.v.map(x => (x._1, deLynxValue(x._2)))
      case r: LynxNode => mapNode(r.value)
      case r: LynxRelationship => mapRelation(r.value)
      case r: LynxBlob => r.toString
      case _ => new Exception(s"Unexpected type of ${lynxValue}")
    }
  }

  def mapToHashMap(map:Map[String,LynxValue]): java.util.HashMap[String,Any] ={
    val resultMap = new java.util.HashMap[String,Any]()
    map.foreach(x=>{
      resultMap.put(x._1,deLynxValue(x._2))
    })
    resultMap
  }
}
