package com.mj.users.model

import java.util.Date

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.collection.mutable.MutableList

case class MultipleInvitation(memberID: String, connections: Option[List[ConnectionsDto]])

// Media 
case class Media(title:Option[String], description:Option[String], media_file:Option[String])

//Experience
case class ExperienceRequest( memberID: String, position:Option[String], career_level:Option[String], description:Option[String], employer: Option[String], start_month:Option[String],
                              start_year:Option[String], end_month:Option[String], end_year:Option[String], current:Option[Boolean], industry: String, media:Option[Media])
case class Experience(expID: String, status : String ,memberID: String, position:Option[String], career_level:Option[String], description:Option[String], employer: Option[String], start_month:Option[String],
                         start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String], current:Option[Boolean], industry: String, media:Option[Media]
                        )

//Education
case class EducationRequest(memberID: String, school_name: Option[String], field_of_study:Option[String], degree: Option[String], start_year:Option[String], end_year:Option[String], activities: Option[String], media:Option[Media])
case class Education(eduID: String,  status : String , memberID: String, school_name: Option[String], field_of_study:Option[String], degree: Option[String],
                        start_year:Option[String], end_year:Option[String],activities: Option[String],  media:Option[Media], created_date: Option[String], updated_date:Option[String])


//Portfolio
case class PortfolioRequest(memberID:String,  media_type:String, media_title:String, media: String, media_description: String , media_url : String)
case class Portfolio(memberID:String,  status : String ,mediaID: String, media_type:String, media_title:String, media: String, media_description: String , media_url: String, created_date: Option[String], updated_date: Option[String])

//Certification
case class CertificationRequest(memberID: String, cert_name: String, cert_authority: String, licence: Option[String], cert_url: Option[String], start_month:Option[String], start_year:Option[String], end_month:Option[String], end_year:Option[String],  expires: Option[Int] )
case class Certification(memberID: String, status : String , certID: String, cert_name: String, cert_authority: String, licence: Option[String], cert_url: Option[String], start_month:Option[String], start_year:Option[String], end_month:Option[String], end_year:Option[String], created_date: Option[String], updated_date: Option[String], expires: Option[Int] )

//Publication
case class PublicationRequest(memberID: String, publication_title: String, author:Option[String], publication_url:Option[String], publication_month:Option[String], publication_year:Option[String], description: Option[String])
case class Publication(memberID: String, status : String , pubID: String, publication_title: String, author:Option[String], publication_url:Option[String], publication_month:Option[String], publication_year:Option[String], description: Option[String], created_date: Option[String], updated_date: Option[String])

//Award
case class AwardRequest(memberID: String, award_title: String, issuer: Option[String], month:Option[String], year:Option[String], description: Option[String])
case class Award(memberID: String,  status : String ,awID: String, award_title: String, issuer: Option[String], month:Option[String], year:Option[String], description: Option[String],created_date: Option[String], updated_date: Option[String])

//Language
case class LanguageDetails( language: String, fluency: Option[String])
case class Language(memberID: String ,languageDetails : List[LanguageDetails])
case class LanguageRequest(memberID: String, languageDetails : List[LanguageDetails])
case class UpdateLanguage(memberID: String, languageDetails: LanguageDetails)

//web profile
case class WebProfileRequest(memberID: String, profile_url: Option[String])
case class WebProfile(memberID: String, status : String , webpID: String, profile_url: Option[String], created_date: Option[String], updated_date: Option[String])

//skills
case class SkillRequest(memberID: String, skill_title: List[String])
case class Skill(memberID: String ,skill_title: List[String])
case class UpdateSkill(memberID: String, skill_title: String)

//friends
case class Friend(memberID : String , inviteeID : String , firstName : String , conn_type : Option[String])

//connections
case class Connections(memberID : String , inviteeID : String ,conn_type : String , status : String )

case class User(memberID: String, firstname: String, lastname: String, email: String, avatar: String, degree:Int)

//Response format for all apis
case class responseMessage(uid: String, errmsg: String , successmsg : String)


case class DBRegisterDto(var _id: String, status : String ,avatar: String, created_date: Option[String], updated_date: Option[String],
                         registerDto: RegisterDto,
                         experience: Option[userExperience], /*experience collection*/
                         education: Option[userEducation], /*education collection*/
                         Interest: Option[List[String]], /*interest details*/
                         userIP: Option[String], country: Option[String], interest_on_colony: Option[String], employmentStatus: Option[String] ,interest: Option[List[String]]/*extra fields from second step page*/
                         , secondSignup_flag: Option[Boolean] = Some(false), email_verification_flag: Option[Boolean] = Some(false),connections_flag : Option[Boolean]= Some(false) , /*user prfile flags*/
                         lastLogin: Long = 0, loginCount: Int = 0, sessionsStatus: List[SessionStatus] = List(), dateline: Long = System.currentTimeMillis()
                        )

case class userExperience(position: Option[String], career_level: Option[String], description: Option[String], employer: Option[String], start_month: Option[String],
                          start_year: Option[String], end_month: Option[String], end_year: Option[String], current: Option[Boolean],
                          industry: Option[String])

case class userEducation(school_name: Option[String], field_of_study: Option[String], degree: Option[String],
                         start_year: Option[String], end_year: Option[String], activities: Option[String])


case class RegisterDto(email: String, nickname: String, password: String, repassword: String,
                       gender: Int, firstname: String, lastname: String, contact_info: Option[ContactInfo],
                       location:Option[Location], connections: Option[List[ConnectionsDto]],
                       connection_requests: Option[List[String]],
                       friends_with_post:Option[List[String]],
                       user_agent : Option[String])

case class ConnectionsDto (memberID : String , conn_type : String , status : String )

case class SessionStatus(sessionid: String, newCount: Int)

case class Location(city: Option[String], state:Option[String], country: Option[String], countryCode: Option[String], lat: Option[Double], lon: Option[Double], ip: Option[String], region: Option[String], regionName: Option[String], timezone: Option[String], zip: Option[String])

case class ContactInfo(address: String, city: String, state: String, country: String, email:Option[String], mobile_phone: Option[String], birth_day:Option[Int], birth_month:Option[Int], birth_year:Option[Int], twitter_profile:Option[String], facebook_profile:Option[String])

object JsonRepo extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val connectionsDtoFormats: RootJsonFormat[ConnectionsDto] = jsonFormat3(ConnectionsDto)
  implicit val multipleInvitationRequestDtoFormats: RootJsonFormat[MultipleInvitation] = jsonFormat2(MultipleInvitation)
  implicit val mediaDtoFormats: RootJsonFormat[Media] = jsonFormat3(Media)
  implicit val experienceRequestDtoFormats: RootJsonFormat[ExperienceRequest] = jsonFormat12(ExperienceRequest)
  implicit val experienceResponseDtoFormats: RootJsonFormat[Experience] = jsonFormat16(Experience)
  implicit val educationRequestDtoFormats: RootJsonFormat[EducationRequest] = jsonFormat8(EducationRequest)
  implicit val educationResponseDtoFormats: RootJsonFormat[Education] = jsonFormat12(Education)
  implicit val portfolioRequestDtoFormats: RootJsonFormat[PortfolioRequest] = jsonFormat6(PortfolioRequest)
  implicit val portfolioResponseDtoFormats: RootJsonFormat[Portfolio] = jsonFormat10(Portfolio)
  implicit val certificationRequestDtoFormats: RootJsonFormat[CertificationRequest] = jsonFormat10(CertificationRequest)
  implicit val certificationResponseDtoFormats: RootJsonFormat[Certification] = jsonFormat14(Certification)
  implicit val publicationRequestDtoFormats: RootJsonFormat[PublicationRequest] = jsonFormat7(PublicationRequest)
  implicit val publicationResponseDtoFormats: RootJsonFormat[Publication] = jsonFormat11(Publication)
  implicit val awardRequestDtoFormats: RootJsonFormat[AwardRequest] = jsonFormat6(AwardRequest)
  implicit val awardResponseDtoFormats: RootJsonFormat[Award] = jsonFormat10(Award)
  implicit val languageDetailsResponseDtoFormats: RootJsonFormat[LanguageDetails] = jsonFormat2(LanguageDetails)
  implicit val languageRequestDtoFormats: RootJsonFormat[LanguageRequest] = jsonFormat2(LanguageRequest)
  implicit val languageResponseDtoFormats: RootJsonFormat[Language] = jsonFormat2(Language)
  implicit val updateLanguageResponseDtoFormats: RootJsonFormat[UpdateLanguage] = jsonFormat2(UpdateLanguage)
  implicit val webProfileRequestDtoFormats: RootJsonFormat[WebProfileRequest] = jsonFormat2(WebProfileRequest)
  implicit val webProfileResponseDtoFormats: RootJsonFormat[WebProfile] = jsonFormat6(WebProfile)
  implicit val skillsRequestDtoFormats: RootJsonFormat[SkillRequest] = jsonFormat2(SkillRequest)
  implicit val skillsResponseDtoFormats: RootJsonFormat[Skill] = jsonFormat2(Skill)
  implicit val updateSkillsResponseDtoFormats: RootJsonFormat[UpdateSkill] = jsonFormat2(UpdateSkill)
  implicit val UserDtoFormats: RootJsonFormat[User] = jsonFormat6(User)
  implicit val errorMessageDtoFormats: RootJsonFormat[responseMessage] = jsonFormat3(responseMessage)
}
