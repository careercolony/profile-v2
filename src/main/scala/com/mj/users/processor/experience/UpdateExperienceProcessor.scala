package com.mj.users.processor.experience

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Experience, responseMessage}
import com.mj.users.mongo.ExperienceDao.updateExperienceDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateExperienceProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (experienceRequestDto: Experience) => {
      val origin = sender()
      val result = updateExperienceDetails(experienceRequestDto)
        .map(response => origin ! responseMessage(experienceRequestDto.expID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(experienceRequestDto.expID, e.getMessage, "")
        }
      }

    }

  }
}