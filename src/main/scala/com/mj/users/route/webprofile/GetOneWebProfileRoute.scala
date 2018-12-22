package com.mj.users.route.webprofile

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

trait GetOneWebProfileRoute {
  val GetOneWebProfileUserLog = LoggerFactory.getLogger(this.getClass.getName)


  def GetOneWebProfile(system: ActorSystem): Route = {

    val GetOneWebProfileProcessor = system.actorSelection("/*/getOneWebProfileProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("get-one-webp" / "memberID" / Segment / "webpID" / Segment) { (memberID: String, webpID: String) =>
      get {
        val userResponse = GetOneWebProfileProcessor ? (memberID, webpID)
        onComplete(userResponse) {
          case Success(resp) =>
            resp match {
              case s: WebProfile => {
                complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              }
              case s: responseMessage =>
                complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
            }
          case Failure(error) =>
            GetOneWebProfileUserLog.error("Error is: " + error.getMessage)
            complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
        }


      }
    }

  }
}
