package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object PublicationDao {

  val publicationCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("publication"))

  implicit def publicationeWriter = Macros.handler[Publication]


//insert user Details

  def insertNewPublication(userRequest: PublicationRequest): Future[Publication] = {
    for {
      experienceData <- Future {
        Publication(userRequest.memberID,
          BSONObjectID.generate().stringify,
          userRequest.publication_title,
          userRequest.author,
          userRequest.publication_url ,
          userRequest.publication_month ,
          userRequest.publication_year,
          userRequest.description,
          userRequest.created_date,
          userRequest.updated_date
        )
      }
      response <- insert[Publication](publicationCollection, experienceData)
    }
      yield (response)
  }


  def updatePublicationDetails(pub: Publication): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$set" -> BSONDocument(
      "memberID" -> pub.memberID,
      "publication_title" -> pub.publication_title,
      "author" -> pub.author,
      "publication_url" -> pub.publication_url,
      "publication_month" -> pub.publication_month,
      "publication_year" -> pub.publication_year,
      "description" -> pub.description ,
      "created_date" -> pub.created_date,
      "updated_date" -> pub.updated_date
    ))

    update(publicationCollection, {
      BSONDocument("pubID" -> pub.pubID)
    }, selector).map(resp => resp)

  }

  def getPublicationDetailsByID(memberID: String): Future[List[Publication]] = {
    searchAll[Publication](publicationCollection,
      document("memberID" -> memberID))
  }

  def getOnePublicationDetails(memberID: String, pubID : String): Future[Option[Publication]] = {
    search[Publication](publicationCollection,
      document("memberID" -> memberID , "pubID" -> pubID))
  }


}
