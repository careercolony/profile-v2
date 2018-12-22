package com.mj.users.processor.award

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{AwardRequest, responseMessage}
import com.mj.users.mongo.AwardDao.insertNewAward
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewAwardProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (awardRequestDto: AwardRequest) => {
      val origin = sender()
      val result = insertNewAward(awardRequestDto).map(response =>
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
