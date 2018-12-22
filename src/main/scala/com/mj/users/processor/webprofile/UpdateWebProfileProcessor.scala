package com.mj.users.processor.webprofile

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{WebProfile, responseMessage}
import com.mj.users.mongo.WebProfileDao.updateWebProfileDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateWebProfileProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (webProfileRequestDto: WebProfile) => {
      val origin = sender()
      val result = updateWebProfileDetails(webProfileRequestDto)
        .map(response => origin ! responseMessage(webProfileRequestDto.webpID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(webProfileRequestDto.webpID, e.getMessage, "")
        }
      }

    }

  }
}