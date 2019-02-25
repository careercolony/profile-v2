package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future
import com.mj.users.config.Application._
import org.joda.time.DateTime
object WebProfileDao {



  val webProfileCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("webprofile"))

  implicit def webProfileWriter = Macros.handler[WebProfile]

//insert user Details

  def insertNewWebProfile(userRequest: WebProfileRequest): Future[WebProfile] = {
    for {
      userData <- Future {
        WebProfile(userRequest.memberID,active,
          BSONObjectID.generate().stringify,
          userRequest.profile_url,
          Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
          None

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
      "updated_date" -> Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ"))    ))

    update(webProfileCollection, {
      BSONDocument("webpID" -> web.webpID, "status" -> active)
    }, selector).map(resp => resp)

  }

  def getWebProfileDetailsByID(memberID: String): Future[List[WebProfile]] = {
    searchAll[WebProfile](webProfileCollection,
      document("memberID" -> memberID, "status" -> active))
  }

  def getOneWebProfileDetails(memberID: String, webpID : String): Future[Option[WebProfile]] = {
    search[WebProfile](webProfileCollection,
      document("memberID" -> memberID , "webpID" -> webpID, "status" -> active))
  }



}
