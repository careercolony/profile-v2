package com.mj.users.processor.portfolio

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Portfolio, responseMessage}
import com.mj.users.mongo.PortfolioDao.updatePortfolioDetails

import scala.concurrent.ExecutionContext.Implicits.global

class UpdatePortfolioProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (portfolioRequestDto: Portfolio) => {
      val origin = sender()
      val result = updatePortfolioDetails(portfolioRequestDto)
        .map(response => origin ! responseMessage(portfolioRequestDto.mediaID, "", updateSuccess)
        )
      result.recover {
        case e: Throwable => {
          origin ! responseMessage(portfolioRequestDto.mediaID, e.getMessage, "")
        }
      }

    }

  }
}