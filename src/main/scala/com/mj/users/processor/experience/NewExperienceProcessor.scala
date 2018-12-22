package com.mj.users.processor.experience

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{ExperienceRequest, responseMessage}
import com.mj.users.mongo.ExperienceDao.insertNewExperience
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewExperienceProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (experienceRequestDto: ExperienceRequest) => {
      val origin = sender()
      val result = insertNewExperience(experienceRequestDto).map(response =>
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
