package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import org.joda.time.DateTime
import scala.concurrent.Future
import com.mj.users.config.Application._

object SkillsDao {



  val skillsCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("skill"))

  implicit def skillsWriter = Macros.handler[Skill]

//insert user Details

  def insertNewSkill(userRequest: SkillRequest): Future[Skill] = {
    for {
      userData <- Future {
        Skill(userRequest.memberID,
          userRequest.skill_title

        )
      }
      response <- insert[Skill](skillsCollection, userData)
    }
      yield (response)
  }

  def updateSkillDetails(skill: UpdateSkill): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$addToSet" -> BSONDocument(
      "skill_title" -> skill.skill_title

    ))

    update(skillsCollection, {
      BSONDocument("memberID" -> skill.memberID)
    }, selector).map(resp => resp)

  }


  def deleteSkillDetails(memberID : String , skill_title: String): Future[String] = {
    val selector: BSONDocument = BSONDocument( "$pull" -> BSONDocument( "skill_title"-> skill_title))

    update(skillsCollection, {
      BSONDocument("memberID" -> memberID)
    }, selector).map(resp => resp)

  }

  def getSkillDetailsByID(memberID: String): Future[List[Skill]] = {
    searchAll[Skill](skillsCollection,
      document("memberID" -> memberID))
  }



}
