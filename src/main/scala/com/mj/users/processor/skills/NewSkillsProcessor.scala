package com.mj.users.processor.skills

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{SkillRequest, responseMessage}
import com.mj.users.mongo.SkillsDao.insertNewSkill
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewSkillsProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (skillsRequestDto: SkillRequest) => {
      val origin = sender()
      val result = insertNewSkill(skillsRequestDto).map(response =>
        origin ! response
      )

      result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
