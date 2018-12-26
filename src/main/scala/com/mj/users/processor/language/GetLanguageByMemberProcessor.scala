package com.mj.users.processor.language

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.LanguageDao.getLanguageDetailsByID

import scala.concurrent.ExecutionContext.Implicits.global

class GetLanguageByMemberProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String) => {
      val origin = sender()

      val result = getLanguageDetailsByID(memberID).map(response => {
        response match {
          case None => origin ! responseMessage("", noRecordFound, "")
          case Some(resp) => origin ! resp
        }
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
