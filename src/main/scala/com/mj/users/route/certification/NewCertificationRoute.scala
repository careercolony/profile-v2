package com.mj.users.route.certification

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

trait NewCertificationRoute {
  val newCertificationUserLog = LoggerFactory.getLogger(this.getClass.getName)


  def newCertification(system: ActorSystem): Route = {

    val newCertificationProcessor = system.actorSelection("/*/newCertificationProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("new-certification") {
      post {
        entity(as[CertificationRequest]) { dto =>

          val userResponse = newCertificationProcessor ? dto
          onComplete(userResponse) {
            case Success(resp) =>
              resp match {
                case s: Certification => {
                  complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
                }
                case s: responseMessage =>
                  complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
                case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
              }
            case Failure(error) =>
              newCertificationUserLog.error("Error is: " + error.getMessage)
              complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
          }

        }
      }
    }
  }
}
