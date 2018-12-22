package com.mj.users.model

import java.util.Date

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

//Experience
case class ExperienceRequest( memberID: String, position:Option[String], career_level:Option[String], description:Option[String], employer: Option[String], start_month:Option[String],
                              start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String], current:Option[Boolean], industry: String)
case class Experience(expID: String, memberID: String, position:Option[String], career_level:Option[String], description:Option[String], employer: Option[String], start_month:Option[String],
                         start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String], current:Option[Boolean], industry: String
                        )

//Education
case class EducationRequest(memberID: String, school_name: Option[String], field_of_study:Option[String], degree: Option[String], start_year:Option[String], end_year:Option[String], activities: Option[String], created_date: Option[String], updated_date:Option[String])
case class Education(eduID: String, memberID: String, school_name: Option[String], field_of_study:Option[String], degree: Option[String],
                        start_year:Option[String], end_year:Option[String],activities: Option[String], created_date: Option[String], updated_date:Option[String])


//Portfolio
case class PortfolioRequest(memberID:String,  media_type:String, media_title:String, media: String, media_description: String , media_url : String)
case class Portfolio(memberID:String, mediaID: String, media_type:String, media_title:String, media: String, media_description: String , media_url: String)

//Certification
case class CertificationRequest(memberID: String, cert_name: String, cert_authority: String, licence: Option[String], cert_url: Option[String], start_month:Option[String], start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String],  expires: Option[Int] )
case class Certification(memberID: String, certID: String, cert_name: String, cert_authority: String, licence: Option[String], cert_url: Option[String], start_month:Option[String], start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String], expires: Option[Int] )

//Publication
case class PublicationRequest(memberID: String, publication_title: String, author:Option[String], publication_url:Option[String], publication_month:Option[String], publication_year:Option[String], description: Option[String], created_date: Option[String], updated_date: Option[String])
case class Publication(memberID: String, pubID: String, publication_title: String, author:Option[String], publication_url:Option[String], publication_month:Option[String], publication_year:Option[String], description: Option[String], created_date: Option[String], updated_date: Option[String])

//Award
case class AwardRequest(memberID: String, award_title: String, issuer: Option[String], month:Option[String], year:Option[String], description: Option[String],created_date: Option[String], updated_date: Option[String])
case class Award(memberID: String, awID: String, award_title: String, issuer: Option[String], month:Option[String], year:Option[String], description: Option[String],created_date: Option[String], updated_date: Option[String])

//Language
case class LanguageDetails( language: String, fluency: Option[String])
case class Language(memberID: String,languageDetails : List[LanguageDetails])
case class LanguageRequest(memberID: String, languageDetails : List[LanguageDetails])
case class UpdateLanguage(memberID: String, languageDetails: LanguageDetails)

//web profile
case class WebProfileRequest(memberID: String, profile_url: Option[String], created_date: Option[String], updated_date: Option[String])
case class WebProfile(memberID: String, webpID: String, profile_url: Option[String], created_date: Option[String], updated_date: Option[String])

//skills
case class SkillRequest(memberID: String, skill_title: List[String])
case class Skill(memberID: String, skill_title: List[String])
case class UpdateSkill(memberID: String, skill_title: String)

//Response format for all apis
case class responseMessage(uid: String, errmsg: String , successmsg : String)


object JsonRepo extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val experienceRequestDtoFormats: RootJsonFormat[ExperienceRequest] = jsonFormat13(ExperienceRequest)
  implicit val experienceResponseDtoFormats: RootJsonFormat[Experience] = jsonFormat14(Experience)
  implicit val educationRequestDtoFormats: RootJsonFormat[EducationRequest] = jsonFormat9(EducationRequest)
  implicit val educationResponseDtoFormats: RootJsonFormat[Education] = jsonFormat10(Education)
  implicit val portfolioRequestDtoFormats: RootJsonFormat[PortfolioRequest] = jsonFormat6(PortfolioRequest)
  implicit val portfolioResponseDtoFormats: RootJsonFormat[Portfolio] = jsonFormat7(Portfolio)
  implicit val certificationRequestDtoFormats: RootJsonFormat[CertificationRequest] = jsonFormat12(CertificationRequest)
  implicit val certificationResponseDtoFormats: RootJsonFormat[Certification] = jsonFormat13(Certification)
  implicit val publicationRequestDtoFormats: RootJsonFormat[PublicationRequest] = jsonFormat9(PublicationRequest)
  implicit val publicationResponseDtoFormats: RootJsonFormat[Publication] = jsonFormat10(Publication)
  implicit val awardRequestDtoFormats: RootJsonFormat[AwardRequest] = jsonFormat8(AwardRequest)
  implicit val awardResponseDtoFormats: RootJsonFormat[Award] = jsonFormat9(Award)
  implicit val languageDetailsResponseDtoFormats: RootJsonFormat[LanguageDetails] = jsonFormat2(LanguageDetails)
  implicit val languageRequestDtoFormats: RootJsonFormat[LanguageRequest] = jsonFormat2(LanguageRequest)
  implicit val languageResponseDtoFormats: RootJsonFormat[Language] = jsonFormat2(Language)
  implicit val updateLanguageResponseDtoFormats: RootJsonFormat[UpdateLanguage] = jsonFormat2(UpdateLanguage)
  implicit val webProfileRequestDtoFormats: RootJsonFormat[WebProfileRequest] = jsonFormat4(WebProfileRequest)
  implicit val webProfileResponseDtoFormats: RootJsonFormat[WebProfile] = jsonFormat5(WebProfile)
  implicit val skillsRequestDtoFormats: RootJsonFormat[SkillRequest] = jsonFormat2(SkillRequest)
  implicit val skillsResponseDtoFormats: RootJsonFormat[Skill] = jsonFormat2(Skill)
  implicit val updateSkillsResponseDtoFormats: RootJsonFormat[UpdateSkill] = jsonFormat2(UpdateSkill)
  implicit val errorMessageDtoFormats: RootJsonFormat[responseMessage] = jsonFormat3(responseMessage)
}
