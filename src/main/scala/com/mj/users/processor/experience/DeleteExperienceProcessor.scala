package com.mj.users.processor.experience

import java.util.concurrent.TimeUnit

import com.mj.users.mongo.MongoConnector.remove
import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.ExperienceDao.getOneExperienceDetails
import com.mj.users.mongo.ExperienceDao.experienceCollection
import reactivemongo.bson.document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DeleteExperienceProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String , expID : String) => {
      val origin = sender()

      val result = remove(experienceCollection, document("memberID" -> memberID , "expID" -> expID))
        .flatMap(upResult => Future{responseMessage("", "", deleteSuccess)}).map(response => origin ! response)

        result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
