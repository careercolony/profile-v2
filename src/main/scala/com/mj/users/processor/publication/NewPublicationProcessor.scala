package com.mj.users.processor.publication

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{PublicationRequest, responseMessage}
import com.mj.users.mongo.PublicationDao.insertNewPublication
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewPublicationProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (publicationRequestDto: PublicationRequest) => {
      val origin = sender()
      val result = insertNewPublication(publicationRequestDto).map(response =>
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
