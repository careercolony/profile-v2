package com.mj.users.tools

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.mj.users.route.award._
import com.mj.users.route.certification._
import com.mj.users.route.education._
import com.mj.users.route.experience._
import com.mj.users.route.friends._
import com.mj.users.route.language._
import com.mj.users.route.portfolio._
import com.mj.users.route.publication._
import com.mj.users.route.webprofile._
import com.mj.users.route.skills._
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

object RouteUtils extends NewExperienceRoute with UpdateExperienceRoute with GetExperienceByMemberRoute with GetOneExperienceRoute with DeleteExperienceRoute
  with NewEducationRoute with UpdateEducationRoute with GetEducationByMemberRoute with GetOneEducationRoute with DeleteEducationRoute
  with NewPortfolioRoute with UpdatePortfolioRoute with GetPortfolioByMemberRoute with GetOnePortfolioRoute with DeletePortfolioRoute
  with NewCertificationRoute with UpdateCertificationRoute with GetCertificationByMemberRoute with GetOneCertificationRoute with DeleteCertificationRoute
  with NewAwardRoute with UpdateAwardRoute with GetAwardByMemberRoute with GetOneAwardRoute with DeleteAwardRoute
  with NewLanguageRoute with UpdateLanguageRoute with GetLanguageByMemberRoute with DeleteLanguageRoute
  with NewPublicationRoute with UpdatePublicationRoute with GetPublicationByMemberRoute with GetOnePublicationRoute with DeletePublicationRoute
  with NewWebProfileRoute with UpdateWebProfileRoute with GetWebProfileByMemberRoute with GetOneWebProfileRoute with DeleteWebProfileRoute
  with NewSkillsRoute with UpdateSkillsRoute with GetSkillsByMemberRoute  with DeleteSkillsRoute
with AcceptInvitationRoute with DeclineInvitationRoute with FollowInvitationRoute with UnFollowInvitationRoute with SearchPeopleRoute
with InvitationRoute{

  /*  createUsersCollection()
    createOnlinesCollection()*/

  def badRequest(request: HttpRequest): StandardRoute = {
    val method = request.method.value.toLowerCase
    val path = request.getUri().path()
    val queryString = request.getUri().rawQueryString().orElse("")
    method match {
      case _ =>
        complete((StatusCodes.NotFound, "404 error, resource not found!"))
    }
  }

  //log duration and request info route
  def logDuration(inner: Route)(implicit ec: ExecutionContext): Route = { ctx =>
    val rejectionHandler = RejectionHandler.default
    val start = System.currentTimeMillis()
    val innerRejectionsHandled = handleRejections(rejectionHandler)(inner)
    mapResponse { resp =>
      val currentTime = new DateTime()
      val currentTimeStr = currentTime.toString("yyyy-MM-dd HH:mm:ss")
      val duration = System.currentTimeMillis() - start
      var remoteAddress = ""
      var userAgent = ""
      var rawUri = ""
      ctx.request.headers.foreach(header => {
        //this setting come from nginx
        if (header.name() == "X-Real-Ip") {
          remoteAddress = header.value()
        }
        if (header.name() == "User-Agent") {
          userAgent = header.value()
        }
        //you must set akka.http.raw-request-uri-header=on config
        if (header.name() == "Raw-Request-URI") {
          rawUri = header.value()
        }
      })
      Future {
        val mapPattern = Seq("user")
        var isIgnore = false
        mapPattern.foreach(pattern =>
          isIgnore = isIgnore || rawUri.startsWith(s"/$pattern"))
        if (!isIgnore) {
          println(
            s"# $currentTimeStr ${ctx.request.uri} [$remoteAddress] [${ctx.request.method.name}] [${resp.status.value}] [$userAgent] took: ${duration}ms")
        }
      }
      resp
    }(innerRejectionsHandled)(ctx)
  }

  def routeRoot(implicit ec: ExecutionContext,
                system: ActorSystem,
                materializer: ActorMaterializer) = {
    routeLogic ~
      extractRequest { request =>
        badRequest(request)
      }
  }


  def routeLogic(implicit ec: ExecutionContext,
                 system: ActorSystem,
                 materializer: ActorMaterializer) = {
    newExperience(system) ~ updateExperience(system) ~ getExperienceByMember(system) ~ GetOneExperience(system) ~ DeleteExperience(system) ~
      newEducation(system) ~ updateEducation(system) ~ getEducationByMember(system) ~ GetOneEducation(system) ~ DeleteEducation(system) ~
      newPortfolio(system) ~ updatePortfolio(system) ~ getPortfolioByMember(system) ~ GetOnePortfolio(system) ~ DeletePortfolio(system) ~
      newCertification(system) ~ updateCertification(system) ~ getCertificationByMember(system) ~ GetOneCertification(system) ~ DeleteCertification(system) ~
      newLanguage(system) ~ updateLanguage(system) ~ getLanguageByMember(system)  ~ DeleteLanguage(system) ~
      newAward(system) ~ updateAward(system) ~ getAwardByMember(system) ~ GetOneAward(system) ~ DeleteAward(system) ~
      newPublication(system) ~ updatePublication(system) ~ getPublicationByMember(system) ~ GetOnePublication(system) ~ DeletePublication(system) ~
      newWebProfile(system) ~ updateWebProfile(system) ~ getWebProfileByMember(system) ~ GetOneWebProfile(system) ~ DeleteWebProfile(system) ~
      newSkill(system) ~ updateSkill(system) ~ getSkillByMember(system)  ~ DeleteSkill(system) ~
      invitation(system) ~ Acceptinvitation(system) ~ DeclineInvitation(system) ~ searchPeople(system) ~ followInvitation(system) ~ UnFollowInvitation(system)
  }

  def logRoute(implicit ec: ExecutionContext,
               system: ActorSystem,
               materializer: ActorMaterializer) = logDuration(routeRoot)
}
