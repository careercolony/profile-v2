package com.mj.users.route.experience

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.mj.users.model.JsonRepo._
import com.mj.users.model.{responseMessage, _}
import org.slf4j.LoggerFactory
import spray.json._

import scala.util.{Failure, Success}

trait NewExperienceRoute {
  val newExperienceUserLog = LoggerFactory.getLogger(this.getClass.getName)


  def newExperience(system: ActorSystem): Route = {

    val newExperienceProcessor = system.actorSelection("/*/newExperienceProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("new-experience") {
      post {
        entity(as[ExperienceRequest]) { dto =>

          val userResponse = newExperienceProcessor ? dto
          onComplete(userResponse) {
            case Success(resp) =>
              resp match {
                case s: Experience => {
                  complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
                }
                case s: responseMessage =>
                  complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
                case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
              }
            case Failure(error) =>
              newExperienceUserLog.error("Error is: " + error.getMessage)
              complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
          }

        }
      }
    }
  }
}
