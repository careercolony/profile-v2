package com.mj.users.processor.experience

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{ExperienceRequest, responseMessage}
import com.mj.users.mongo.ExperienceDao.insertNewExperience
import org.slf4j.LoggerFactory
import com.mj.users.mongo.Neo4jConnector.updateNeo4j

import scala.concurrent.ExecutionContext.Implicits.global

class NewExperienceProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (experienceRequestDto: ExperienceRequest) => {
      val origin = sender()
      println(experienceRequestDto)
      val result = insertNewExperience(experienceRequestDto).map(response =>
        origin ! response
      )
      
      /**
      //Create a profile node and [r:HAS_PROFILE] relationship 
      val positionVal: String = experienceRequestDto.position match {
        case None => ""
        case Some(str) => str
      }

      val descriptionVal: String = experienceRequestDto.description match {
        case None => ""
        case Some(str) => str
      }
     
      val script = s"CREATE (a:Profile {position:'${positionVal}', description:'${descriptionVal}'} ), (b:users {memberID:'${experienceRequestDto.memberID}'} ) CREATE (b)-[r:HAS_PROFILE {conn_type:'profile'}]->(a)"
      val result = insertNewExperience(experienceRequestDto).map(response =>
          //origin ! response
          updateNeo4j(script).map(resp => resp match {
          case count if count > 0 => origin ! response
          case 0 => origin ! responseMessage("", s"insert record", "")
        })
      )
      */

      result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
