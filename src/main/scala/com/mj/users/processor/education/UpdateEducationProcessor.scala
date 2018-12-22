package com.mj.users.processor.education

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Education, Experience, responseMessage}
import com.mj.users.mongo.EducationDao.updateEducationDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateEducationProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (educationRequestDto: Education) => {
      val origin = sender()
      val result = updateEducationDetails(educationRequestDto)
        .map(response => origin ! responseMessage(educationRequestDto.eduID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(educationRequestDto.eduID, e.getMessage, "")
        }
      }

    }

  }
}