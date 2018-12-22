package com.mj.users.processor.education

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{EducationRequest, responseMessage}
import com.mj.users.mongo.EducationDao.insertNewEducation

import scala.concurrent.ExecutionContext.Implicits.global

class NewEducationProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (educationRequestDto: EducationRequest) => {
      val origin = sender()
      val result = insertNewEducation(educationRequestDto).map(response =>
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
