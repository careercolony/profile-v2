package com.mj.users.processor.skills

import java.util.concurrent.TimeUnit

import com.mj.users.mongo.MongoConnector.remove
import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.responseMessage
import com.mj.users.mongo.SkillsDao.skillsCollection
import com.mj.users.mongo.SkillsDao.deleteSkillDetails
import reactivemongo.bson.document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DeleteSkillsProcessor extends Actor with MessageConfig{

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (memberID: String , skill_title : String) => {
      val origin = sender()

      val result = deleteSkillDetails(memberID,skill_title)
        .flatMap(upResult => Future{responseMessage("", "", deleteSuccess)}).map(response => origin ! response)

        result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
