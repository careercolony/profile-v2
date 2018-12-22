package com.mj.users.processor.webprofile

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.WebProfileDao.getOneWebProfileDetails

import scala.concurrent.ExecutionContext.Implicits.global

class GetOneWebProfileProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String , webpID : String) => {
      val origin = sender()
      val result = getOneWebProfileDetails(memberID , webpID).map(response =>
        response match {
          case Some(resp) => origin ! resp
          case None =>  origin ! responseMessage("", noRecordFound, "")
        }

      )

      result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
