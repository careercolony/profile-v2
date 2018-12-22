package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object PortfolioDao {


  val portfolioCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("portfolio"))

  implicit def portfolioeWriter = Macros.handler[Portfolio]


  //insert user Details
  def insertNewPortfolio(userRequest: PortfolioRequest): Future[Portfolio] = {
    for {
      portfolioData <- Future {
        Portfolio(
          userRequest.memberID,
          BSONObjectID.generate().stringify,
          userRequest.media_type,
          userRequest.media_title,
          userRequest.media,
          userRequest.media_description,
            userRequest.media_url
        )
      }
      response <- insert[Portfolio](portfolioCollection, portfolioData)
    }
      yield (response)
  }

  def updatePortfolioDetails(port: Portfolio): Future[String] = {
    val selector: BSONDocument = BSONDocument("$set" -> BSONDocument(
      "memberID" -> port.memberID,
      "media_type" -> port.media_type,
      "media_title" -> port.media_title,
      "media" -> port.mediaID,
      "media_description" -> port.media_description,
      "media_url" -> port.media_url

    ))

    update(portfolioCollection, {
      BSONDocument("mediaID" -> port.mediaID)
    }, selector).map(resp => resp)

  }


  def getPortfolioDetailsByID(memberID: String): Future[List[Portfolio]] = {
    searchAll[Portfolio](portfolioCollection,
      document("memberID" -> memberID))
  }

  def getOnePortfolioDetails(memberID: String, mediaID: String): Future[Option[Portfolio]] = {
    search[Portfolio](portfolioCollection,
      document("memberID" -> memberID, "mediaID" -> mediaID))
  }


}
