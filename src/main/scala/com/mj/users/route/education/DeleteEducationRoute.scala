package com.mj.users.route.education

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

trait DeleteEducationRoute {
  val DeleteEducationUserLog = LoggerFactory.getLogger(this.getClass.getName)


  def DeleteEducation(system: ActorSystem): Route = {

    val DeleteEducationProcessor = system.actorSelection("/*/deleteEducationProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("delete-education" / "memberID" / Segment / "eduID" / Segment) { (memberID: String, eduID: String) =>
      get {
        val userResponse = DeleteEducationProcessor ? (memberID , eduID)
        onComplete(userResponse) {
          case Success(resp) =>
            resp match {
              case s: responseMessage => if (s.successmsg.nonEmpty) {
                complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              } else
                complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
            }
          case Failure(error) =>
            DeleteEducationUserLog.error("Error is: " + error.getMessage)
            complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
        }


      }
    }

  }
}
