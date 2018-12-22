package com.mj.users.processor.publication

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.PublicationDao.getPublicationDetailsByID

import scala.concurrent.ExecutionContext.Implicits.global

class GetPublicationByMemberProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String) => {
      val origin = sender()
      val result = getPublicationDetailsByID(memberID).map(response =>
        response match {
          case List() => origin ! responseMessage("", noRecordFound, "")
          case _ =>  origin ! response
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
