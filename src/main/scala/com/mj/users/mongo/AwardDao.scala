package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future
import com.mj.users.config.Application._
import org.joda.time.DateTime

object AwardDao {



  val awardCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("award"))

  implicit def awardeWriter = Macros.handler[Award]

//insert user Details


  def insertNewAward(userRequest: AwardRequest): Future[Award] = {
    for {
      experienceData <- Future {
        Award(userRequest.memberID,active ,
          BSONObjectID.generate().stringify,
          userRequest.award_title,
          userRequest.issuer ,
          userRequest.month ,
          userRequest.year,
          userRequest.description,
          Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
          None
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
      "updated_date" -> Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ"))
    ))

    update(awardCollection, {
      BSONDocument("awID" -> awd.awID, "status" -> active)
    }, selector).map(resp => resp)

  }

  def getAwardDetailsByID(memberID: String): Future[List[Award]] = {
    searchAll[Award](awardCollection,
      document("memberID" -> memberID , "status" -> active))
  }

  def getOneAwardDetails(memberID: String, awID : String): Future[Option[Award]] = {
    search[Award](awardCollection,
      document("memberID" -> memberID , "awID" -> awID , "status" -> active))
  }


}
