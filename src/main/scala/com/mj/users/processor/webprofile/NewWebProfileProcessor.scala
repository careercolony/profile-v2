package com.mj.users.processor.webprofile

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{WebProfileRequest, responseMessage}
import com.mj.users.mongo.WebProfileDao.insertNewWebProfile
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewWebProfileProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (webProfileRequestDto: WebProfileRequest) => {
      val origin = sender()
      val result = insertNewWebProfile(webProfileRequestDto).map(response =>
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
