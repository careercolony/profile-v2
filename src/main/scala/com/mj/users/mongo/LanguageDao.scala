package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object LanguageDao {


  val languageCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("language"))
  implicit def languageDetailsWriter = Macros.handler[LanguageDetails]
  implicit def languageWriter = Macros.handler[Language]


  def insertNewLanguage(userRequest: LanguageRequest): Future[Language] = {
    for {
      languageData <- Future {
        Language(
          userRequest.memberID,
          userRequest.languageDetails
        )
      }
      response <- insert[Language](languageCollection, languageData)
    }
      yield (response)
  }

  def updateLanguageDetails(lang: UpdateLanguage): Future[String] = {
    val selector: BSONDocument = BSONDocument("$set" -> BSONDocument(
      "languageDetails.$.fluency" -> lang.languageDetails.fluency
    ))

    update(languageCollection, {
      BSONDocument("memberID" -> lang.memberID ,
        "languageDetails.language" -> lang.languageDetails.language)
    }, selector).map(resp => resp)

  }


  def getLanguageDetailsByID(memberID: String): Future[List[Language]] = {

    searchAll[Language](languageCollection,
      document("memberID" -> memberID))
  }

  def deleteSkillDetails(memberID : String , languageName: String): Future[String] = {
    val selector: BSONDocument = BSONDocument( "$pull" -> BSONDocument( "languageDetails"-> BSONDocument("language" -> languageName)))

    update(languageCollection, {
      BSONDocument("memberID" -> memberID)
    }, selector).map(resp => resp)

  }

}
