package com.mj.users.processor.skills

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Skill, UpdateSkill, responseMessage}
import com.mj.users.mongo.SkillsDao.updateSkillDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateSkillsProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (skillsRequestDto: UpdateSkill) => {
      val origin = sender()
      val result = updateSkillDetails(skillsRequestDto)
        .map(response => origin ! responseMessage(skillsRequestDto.memberID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(skillsRequestDto.memberID, e.getMessage, "")
        }
      }

    }

  }
}