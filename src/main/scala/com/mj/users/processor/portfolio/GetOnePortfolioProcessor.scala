package com.mj.users.processor.portfolio

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.PortfolioDao.getOnePortfolioDetails

import scala.concurrent.ExecutionContext.Implicits.global

class GetOnePortfolioProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String , mediaID : String) => {
      val origin = sender()
      val result = getOnePortfolioDetails(memberID , mediaID).map(response =>
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
