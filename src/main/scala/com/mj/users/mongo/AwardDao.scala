package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object AwardDao {



  val awardCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("award"))

  implicit def awardeWriter = Macros.handler[Award]

//insert user Details


  def insertNewAward(userRequest: AwardRequest): Future[Award] = {
    for {
      experienceData <- Future {
        Award(userRequest.memberID,
          BSONObjectID.generate().stringify,
          userRequest.award_title,
          userRequest.issuer ,
          userRequest.month ,
          userRequest.year,
          userRequest.description,
          userRequest.created_date,
          userRequest.updated_date
        )
      }
      response <- insert[Award](awardCollection, experienceData)
    }
      yield (response)
  }

  def updateAwardDetails(awd: Award): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$set" -> BSONDocument(
      "memberID" -> awd.memberID,
      "award_title" -> awd.award_title,
      "issuer" -> awd.issuer,
      "month" -> awd.month,
      "year" -> awd.year,
      "description" -> awd.description,
      "created_date" -> awd.created_date,
      "updated_date" -> awd.updated_date
    ))

    update(awardCollection, {
      BSONDocument("awID" -> awd.awID)
    }, selector).map(resp => resp)

  }

  def getAwardDetailsByID(memberID: String): Future[List[Award]] = {
    searchAll[Award](awardCollection,
      document("memberID" -> memberID))
  }

  def getOneAwardDetails(memberID: String, awID : String): Future[Option[Award]] = {
    search[Award](awardCollection,
      document("memberID" -> memberID , "awID" -> awID))
  }


}
