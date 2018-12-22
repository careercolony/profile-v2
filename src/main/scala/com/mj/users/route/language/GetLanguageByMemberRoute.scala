package com.mj.users.route.language

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives.{complete, path, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.mj.users.model.JsonRepo._
import com.mj.users.model.{responseMessage, _}
import org.slf4j.LoggerFactory
import spray.json._

import scala.util.{Failure, Success}

trait GetLanguageByMemberRoute {
  val getLanguageByMemberUserLog = LoggerFactory.getLogger(this.getClass.getName)


  def getLanguageByMember(system: ActorSystem): Route = {

    val getLanguageByMemberProcessor = system.actorSelection("/*/getLanguageByMemberProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("get-language" / "memberID" / Segment) { (memberID: String) =>
      get {

        val userResponse = getLanguageByMemberProcessor ? memberID
        onComplete(userResponse) {
          case Success(resp) =>
            resp match {
              case s: List[Language] => {
                complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              }
              case s: responseMessage =>
                complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
            }
          case Failure(error) =>
            getLanguageByMemberUserLog.error("Error is: " + error.getMessage)
            complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
        }


      }
    }

  }
}
