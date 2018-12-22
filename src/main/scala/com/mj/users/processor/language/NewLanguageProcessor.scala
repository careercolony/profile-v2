package com.mj.users.processor.language

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{LanguageRequest, responseMessage}
import com.mj.users.mongo.LanguageDao.insertNewLanguage
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewLanguageProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (languageRequestDto: LanguageRequest) => {
      val origin = sender()
      val result = insertNewLanguage(languageRequestDto).map(response =>
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
