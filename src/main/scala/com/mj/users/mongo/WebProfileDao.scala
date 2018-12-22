package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object WebProfileDao {



  val webProfileCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("webprofile"))

  implicit def webProfileWriter = Macros.handler[WebProfile]

//insert user Details

  def insertNewWebProfile(userRequest: WebProfileRequest): Future[WebProfile] = {
    for {
      userData <- Future {
        WebProfile(userRequest.memberID,
          BSONObjectID.generate().stringify,
          userRequest.profile_url,
          userRequest.created_date ,
          userRequest.updated_date
        )
      }
      response <- insert[WebProfile](webProfileCollection, userData)
    }
      yield (response)
  }

  def updateWebProfileDetails(web: WebProfile): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$set" -> BSONDocument(
      "memberID" -> web.memberID,
      "profile_url" -> web.profile_url,
      "created_date" -> web.created_date,
      "updated_date" -> web.updated_date
    ))

    update(webProfileCollection, {
      BSONDocument("webpID" -> web.webpID)
    }, selector).map(resp => resp)

  }

  def getWebProfileDetailsByID(memberID: String): Future[List[WebProfile]] = {
    searchAll[WebProfile](webProfileCollection,
      document("memberID" -> memberID))
  }

  def getOneWebProfileDetails(memberID: String, webpID : String): Future[Option[WebProfile]] = {
    search[WebProfile](webProfileCollection,
      document("memberID" -> memberID , "webpID" -> webpID))
  }



}
