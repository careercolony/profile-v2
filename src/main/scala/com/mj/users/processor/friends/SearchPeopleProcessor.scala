package com.mj.users.processor.friends

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{User, responseMessage}
import com.mj.users.mongo.Neo4jConnector.getNeo4j

import scala.collection.mutable.MutableList
import scala.concurrent.ExecutionContext.Implicits.global

class SearchPeopleProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case memberID : String => {
      val origin = sender()
      val script = s"MATCH (a:users {memberID:'${memberID}'})-[r:FRIEND *1..3]-(b:users) return  length(r) as degree, b.firstname, b.lastname,b.email, b.memberID, b.avatar"
      val result = getNeo4j(script).map(response =>{
      val records = MutableList[User]()
        while (response.hasNext()) {
          val record = response.next()
          val user: User = new User(record.get("b.memberID").asString(), record.get("b.firstname").asString(), record.get("b.lastname").asString(), record.get("b.email").asString(),record.get("b.avatar").asString(), record.get("degree").asInt())
          records += user
        }
        origin ! records.toList}
        )
      /*val user: User3 = new User3(response.get("b.memberID").asInt, record.get("b.firstname").asString(), record.get("b.lastname").asString(), record.get("b.email").asString(),record.get("b.avatar").asString(), record.get("degree").asInt()))*/

      result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
