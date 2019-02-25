package com.mj.users.mongo

import com.mj.users.model._
import com.mj.users.mongo.MongoConnector._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._

import scala.concurrent.Future
import com.mj.users.config.Application._
import org.joda.time.DateTime

object CertificationDao {



  val certificationCollection: Future[BSONCollection] = db1.map(_.collection[BSONCollection]("certification"))

  implicit def certificationeWriter = Macros.handler[Certification]

//insert user Details

  def insertNewCertification(userRequest: CertificationRequest): Future[Certification] = {
    for {
      experienceData <- Future {
        Certification(userRequest.memberID,active,
          BSONObjectID.generate().stringify,
          userRequest.cert_name,
          userRequest.cert_authority ,
          userRequest.licence ,
          userRequest.cert_url,
          userRequest.start_month,
          userRequest.start_year,
          userRequest.end_month,
          userRequest.end_year,
          Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
          None,
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
      "updated_date" -> Some(DateTime.now.toString("yyyy-MM-dd'T'HH:mm:ssZ")),
      "expires" -> cert.expires
    ))

    update(certificationCollection, {
      BSONDocument("certID" -> cert.certID, "status" -> active)
    }, selector).map(resp => resp)

  }

  def getCertificationDetailsByID(memberID: String): Future[List[Certification]] = {
    searchAll[Certification](certificationCollection,
      document("memberID" -> memberID , "status" -> active))
  }

  def getOneCertificationDetails(memberID: String, certID : String): Future[Option[Certification]] = {
    search[Certification](certificationCollection,
      document("memberID" -> memberID , "certID" -> certID , "status" -> active))
  }


}
