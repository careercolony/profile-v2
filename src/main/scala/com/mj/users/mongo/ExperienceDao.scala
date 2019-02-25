package com.mj.users.mongo


import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import org.joda.time.DateTime
import scala.concurrent.Future
import com.mj.users.config.Application._
import org.joda.time.DateTime
object ExperienceDao {


  val experienceCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("experience"))


  implicit def experienceWriter = Macros.handler[Experience]



  //insert user Details
  def insertNewExperience(userRequest: ExperienceRequest): Future[Experience] = {
    for {
      experienceData <- Future {
        Experience(BSONObjectID.generate().stringify,active,
          userRequest.memberID,
          userRequest.position,
          userRequest.career_level ,
          userRequest.description,
          userRequest.employer,
          userRequest.start_month,
          userRequest.start_year,
          userRequest.end_month,
          userRequest.end_year,
          Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
          None,
          userRequest.current,
          userRequest.industry
        )
      }
      response <- insert[Experience](experienceCollection, experienceData)
    }
      yield (response)
  }

  def updateExperienceDetails(exp: Experience): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$set" -> BSONDocument(
      "expID" -> exp.expID,
      "memberID" -> exp.memberID,
      "position" -> exp.position,
      "employer" -> exp.employer,
      "career_level" -> exp.career_level,
      "description" -> exp.description,
      "start_month" -> exp.start_month,
      "start_year" -> exp.start_year,
      "end_month" -> exp.end_month,
      "end_year" -> exp.end_year,
      "updated_date" -> Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
      "current" -> exp.current,
      "industry" -> exp.industry
    ))

    update(experienceCollection, {
      BSONDocument("expID" -> exp.expID, "status" -> active)
    }, selector).map(resp => resp)

  }

  def getExperienceDetailsByID(memberID: String): Future[List[Experience]] = {
    searchAll[Experience](experienceCollection,
      document("memberID" -> memberID, "status" -> active))
  }

  def getOneExperienceDetails(memberID: String, expID : String): Future[Option[Experience]] = {
    search[Experience](experienceCollection,
      document("memberID" -> memberID , "expID" -> expID, "status" -> active))
  }


}
