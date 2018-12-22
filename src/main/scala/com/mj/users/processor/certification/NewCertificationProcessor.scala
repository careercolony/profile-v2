package com.mj.users.processor.certification

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{CertificationRequest, responseMessage}
import com.mj.users.mongo.CertificationDao.insertNewCertification


import scala.concurrent.ExecutionContext.Implicits.global

class NewCertificationProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (certificationRequestDto: CertificationRequest) => {
      val origin = sender()
      val result = insertNewCertification(certificationRequestDto).map(response =>
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
