package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future

object CertificationDao {



  val certificationCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("certification"))

  implicit def certificationeWriter = Macros.handler[Certification]

//insert user Details

  def insertNewCertification(userRequest: CertificationRequest): Future[Certification] = {
    for {
      experienceData <- Future {
        Certification(userRequest.memberID,
          BSONObjectID.generate().stringify,
          userRequest.cert_name,
          userRequest.cert_authority ,
          userRequest.licence ,
          userRequest.cert_url,
          userRequest.start_month,
          userRequest.start_year,
          userRequest.end_month,
          userRequest.end_year,
          userRequest.created_date,
          userRequest.updated_date,
          userRequest.expires
        )
      }
      response <- insert[Certification](certificationCollection, experienceData)
    }
      yield (response)
  }

  def updateCertificationDetails(cert: Certification): Future[String] = {
    val selector: BSONDocument = BSONDocument ( "$set" -> BSONDocument(
      "memberID" -> cert.memberID,
      "cert_name" -> cert.cert_name,
      "cert_authority" -> cert.cert_authority,
      "licence" -> cert.licence,
      "cert_url" -> cert.cert_url,
      "start_month" -> cert.start_month,
      "start_year" -> cert.start_year,
      "end_month" -> cert.end_month,
      "end_year" -> cert.end_year,
      "created_date" -> cert.created_date,
      "updated_date" -> cert.updated_date,
      "expires" -> cert.expires
    ))

    update(certificationCollection, {
      BSONDocument("certID" -> cert.certID)
    }, selector).map(resp => resp)

  }

  def getCertificationDetailsByID(memberID: String): Future[List[Certification]] = {
    searchAll[Certification](certificationCollection,
      document("memberID" -> memberID))
  }

  def getOneCertificationDetails(memberID: String, certID : String): Future[Option[Certification]] = {
    search[Certification](certificationCollection,
      document("memberID" -> memberID , "certID" -> certID))
  }


}
