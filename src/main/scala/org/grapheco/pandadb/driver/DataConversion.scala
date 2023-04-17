package org.grapheco.pandadb.driver

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

  def toLynxRPCNode(lynxNode: LynxNode): LynxRPCNode = {
    val props = lynxNode.keys.map(x => (x.value, lynxNode.property(x).get))
    LynxRPCNode(lynxNode.id.toLynxInteger.value, lynxNode.labels, props)
  }

  def toLynxRPCRelationship(lynxRelationship: LynxRelationship): LynxRPCRelationship = {
    val props = lynxRelationship.keys.map(x => (x.value, lynxRelationship.property(x).get))
    LynxRPCRelationship(lynxRelationship.id.toLynxInteger.value
      , lynxRelationship.startNodeId.toLynxInteger.value
      , lynxRelationship.endNodeId.toLynxInteger.value
      , lynxRelationship.relationType.get.value, props)
  }

  def tLynxPath(lynxPath: LynxPath): Seq[LynxElement] = {
    val elements: Seq[LynxElement] = lynxPath.elements.map(element => {
      element match {
        case r: LynxNode => toLynxRPCNode(r)
        case r: LynxRelationship => toLynxRPCRelationship(r)
      }
    })
    elements
  }

  def tLynxPath1(lynxPath: LynxPath): LynxPath = {
    val elements: Seq[LynxElement] = lynxPath.elements.map(element => {
      element match {
        case r: LynxNode => r
        case r: LynxRelationship => r
      }
    })
    LynxPath(elements)
  }

  def tLynxValue(lynxValue: LynxValue): Any = {

    lynxValue match {
      case r: LynxPath => tLynxPath(r)
      case r: LynxInteger => r.value
      case r: LynxFloat => r.value
      case r: LynxString => r.value
      case r: LynxBoolean => r.value
      case r: LynxList => List(r.value.map(x => tLynxValue(x)))
      case r: LynxMap => r.v.map(x => (x._1, tLynxValue(x._2)))
      case r: LynxNode => toLynxRPCNode(r.value)
      case r: LynxRelationship => toLynxRPCRelationship(r.value)
      case _ => new Exception(s"Unexpected type of ${lynxValue}")
    }

  }

  def tLynxValue1(lynxValue: LynxValue): Any = {

    lynxValue match {
      case r: LynxPath => tLynxPath1(r)
      case r: LynxInteger => r.value
      case r: LynxFloat => r.value
      case r: LynxString => r.value
      case r: LynxBoolean => r.value
      case r: LynxList => List(r.value.map(x => tLynxValue(x)))
      case r: LynxMap => r.v.map(x => (x._1, tLynxValue(x._2)))
      case r: LynxNode => r.value
      case r: LynxRelationship => r.value
      case _ => new Exception(s"Unexpected type of ${lynxValue}")
    }

  }
  def mapToHashMap(map:Map[String,LynxValue]): java.util.HashMap[String,Any] ={
    val resultMap = new java.util.HashMap[String,Any]()
    map.foreach(x=>{
      resultMap.put(x._1,tLynxValue1(x._2))
    })
    resultMap
  }
}
