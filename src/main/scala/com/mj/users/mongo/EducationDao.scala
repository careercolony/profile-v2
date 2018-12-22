package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object EducationDao {


  val educationCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("education"))

  implicit def educationeWriter = Macros.handler[Education]

  //insert user Details
  def insertNewEducation(userRequest: EducationRequest): Future[Education] = {
    for {
      educationData <- Future {
        Education(BSONObjectID.generate().stringify,
          userRequest.memberID,
          userRequest.school_name,
          userRequest.field_of_study,
          userRequest.degree,
          userRequest.start_year,
          userRequest.end_year,
          userRequest.activities,
          userRequest.created_date,
          userRequest.updated_date
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
      "created_date" -> exp.created_date,
      "updated_date" -> exp.updated_date

    ))

    update(educationCollection, {
      BSONDocument("eduID" -> exp.eduID)
    }, selector).map(resp => resp)

  }




  def getEducationDetailsByID(memberID: String): Future[List[Education]] = {
    searchAll[Education](educationCollection,
      document("memberID" -> memberID))
  }

  def getOneEducationDetails(memberID: String, expID : String): Future[Option[Education]] = {
    search[Education](educationCollection,
      document("memberID" -> memberID , "eduID" -> expID))
  }


}
