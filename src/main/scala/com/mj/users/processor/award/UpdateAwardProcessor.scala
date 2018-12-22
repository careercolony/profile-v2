package com.mj.users.processor.award

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Award, responseMessage}
import com.mj.users.mongo.AwardDao.updateAwardDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdateAwardProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (awardRequestDto: Award) => {
      val origin = sender()
      val result = updateAwardDetails(awardRequestDto)
        .map(response => origin ! responseMessage(awardRequestDto.awID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(awardRequestDto.awID, e.getMessage, "")
        }
      }

    }

  }
}