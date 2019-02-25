package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import com.mj.users.config.Application._
import org.joda.time.DateTime

import scala.concurrent.Future

object EducationDao {


  val educationCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("education"))

  implicit def educationeWriter = Macros.handler[Education]

  //insert user Details
  def insertNewEducation(userRequest: EducationRequest): Future[Education] = {
    for {
      educationData <- Future {
        Education(BSONObjectID.generate().stringify,active,
          userRequest.memberID,
          userRequest.school_name,
          userRequest.field_of_study,
          userRequest.degree,
          userRequest.start_year,
          userRequest.end_year,
          userRequest.activities,
          Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
          None
        )
      }
      response <- insert[Education](educationCollection, educationData)
    }
      yield (response)
  }

  def updateEducationDetails(exp: Education): Future[String] = {
    val selector: BSONDocument = BSONDocument("$set" -> BSONDocument(
      "eduID" -> exp.eduID,
      "memberID" -> exp.memberID,
      "school_name" -> exp.school_name,
      "field_of_study" -> exp.field_of_study,
      "degree" -> exp.degree,
      "start_year" -> exp.start_year,
      "end_year" -> exp.end_year,
      "activities" -> exp.activities,
      "updated_date" -> Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ"))

    ))

    update(educationCollection, {
      BSONDocument("eduID" -> exp.eduID, "status" -> active)
    }, selector).map(resp => resp)

  }




  def getEducationDetailsByID(memberID: String): Future[List[Education]] = {
    searchAll[Education](educationCollection,
      document("memberID" -> memberID , "status" -> active))
  }

  def getOneEducationDetails(memberID: String, expID : String): Future[Option[Education]] = {
    search[Education](educationCollection,
      document("memberID" -> memberID , "eduID" -> expID , "status" -> active))
  }


}
