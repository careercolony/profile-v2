package com.mj.users.processor.publication

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Publication, responseMessage}
import com.mj.users.mongo.PublicationDao.updatePublicationDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdatePublicationProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (publicationRequestDto: Publication) => {
      val origin = sender()
      val result = updatePublicationDetails(publicationRequestDto)
        .map(response => origin ! responseMessage(publicationRequestDto.pubID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(publicationRequestDto.pubID, e.getMessage, "")
        }
      }

    }

  }
}