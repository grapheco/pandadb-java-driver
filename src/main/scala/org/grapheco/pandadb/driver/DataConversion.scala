package org.grapheco.pandadb.driver

import org.grapheco.lynx.cypherplus.LynxBlob
import org.grapheco.lynx.types.LynxValue
import org.grapheco.lynx.types.composite.{LynxList, LynxMap}
import org.grapheco.lynx.types.property.{LynxBoolean, LynxFloat, LynxInteger, LynxString, LynxNull}
import org.grapheco.lynx.types.structural.{LynxElement, LynxNode, LynxPath, LynxRelationship}
import org.grapheco.lynx.types.time.{LynxDate, LynxDateTime}

import scala.collection.JavaConverters._
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
    lynxValue.typeOrder(lynxValue)
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
      case r: LynxDate => r.value
      case r: LynxDateTime => r.value
      case LynxNull => null
      case _ => new Exception(s"Unexpected type of ${lynxValue}")
    }
  }

  def hashMapToScala(map:java.util.HashMap[String, Object]): Map[String, Any] ={
    map.asScala.toMap.map(kv=>{
      var value = kv._2
      kv._2 match {
        case e:java.util.ArrayList[java.util.Map[String, Object]] => {
          value = e.asScala.map(_.asScala.toMap).toArray
        }
        case _ => value = kv._2
      }
      (kv._1, value)
    })
  }

  def iterMapToIterHashMap(iter: scala.collection.Iterator[scala.collection.immutable.Map[String, LynxValue]]): java.util.Iterator[java.util.HashMap[String, PandaDBValue]]={
    iter.map(m=>mapToHashMap(m.toMap)).asJava
  }

  def mapToHashMap(map:Map[String,LynxValue]): java.util.HashMap[String,PandaDBValue] ={
    val resultMap = new java.util.HashMap[String,PandaDBValue]()
    map.foreach(x=>{
      resultMap.put(x._1,PandaDBValue(x._2))
    })
    resultMap
  }
  def javaListToArray(list: java.util.ArrayList[_]): Array[_] ={
    val result = list.asScala.toArray.map(x=>{
      x match {
        case map: java.util.HashMap[String,Any] =>
          hashMapToMap(x.asInstanceOf[java.util.HashMap[String, Any]])
        case _ => x
      }
    })
    result
  }
  def hashMapToMap(map: java.util.HashMap[String,Any]): Map[String, Any] ={
    map.asScala.toMap
  }
}
