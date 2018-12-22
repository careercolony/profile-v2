package com.mj.users.processor.language

import java.util.concurrent.TimeUnit

import com.mj.users.mongo.MongoConnector.remove
import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.LanguageDao.languageCollection
import reactivemongo.bson.document
import com.mj.users.mongo.LanguageDao.deleteSkillDetails
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DeleteLanguageProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String , language : String) => {
      val origin = sender()

      val result =  deleteSkillDetails(memberID,language)
        .flatMap(upResult => Future{responseMessage("", "", deleteSuccess)}).map(response => origin ! response)

        result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
