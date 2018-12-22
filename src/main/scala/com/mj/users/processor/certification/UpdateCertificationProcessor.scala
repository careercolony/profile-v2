package com.mj.users.processor.certification

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Certification, responseMessage}
import com.mj.users.mongo.CertificationDao.updateCertificationDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateCertificationProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (certificationRequestDto: Certification) => {
      val origin = sender()
      val result = updateCertificationDetails(certificationRequestDto)
        .map(response => origin ! responseMessage(certificationRequestDto.certID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(certificationRequestDto.certID, e.getMessage, "")
        }
      }

    }

  }
}