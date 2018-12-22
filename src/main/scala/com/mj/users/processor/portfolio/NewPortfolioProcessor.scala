package com.mj.users.processor.portfolio

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{PortfolioRequest, responseMessage}
import com.mj.users.mongo.PortfolioDao.insertNewPortfolio
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

class NewPortfolioProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (portfolioRequestDto: PortfolioRequest) => {
      val origin = sender()
      val result = insertNewPortfolio(portfolioRequestDto).map(response =>
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
