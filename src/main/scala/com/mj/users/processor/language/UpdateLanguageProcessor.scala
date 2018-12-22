package com.mj.users.processor.language

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Language, UpdateLanguage, responseMessage}
import com.mj.users.mongo.LanguageDao.updateLanguageDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateLanguageProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (languageRequestDto: UpdateLanguage) => {
      val origin = sender()
      val result = updateLanguageDetails(languageRequestDto)
        .map(response => origin ! responseMessage(languageRequestDto.memberID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(languageRequestDto.memberID, e.getMessage, "")
        }
      }

    }

  }
}