package com.mj.users.route.friends

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives.{complete, path, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.mj.users.model.JsonRepo._
import com.mj.users.model.{Friend, User, responseMessage}
import org.slf4j.LoggerFactory
import spray.json._

import scala.collection.mutable.MutableList
import scala.util.{Failure, Success}

trait SearchPeopleRoute {
  val searchPeopleLog = LoggerFactory.getLogger(this.getClass.getName)


  def searchPeople(system: ActorSystem): Route = {

    val searchPeopleProcessor = system.actorSelection("/*/searchPeopleProcessor")
    implicit val timeout = Timeout(20, TimeUnit.SECONDS)


    path("people-you-may-know" / "memberID" / Segment) { (memberID: String) =>
      get {

        val userResponse = searchPeopleProcessor ? memberID
        onComplete(userResponse) {
          case Success(resp) =>
            println("resp"+resp.getClass)
            resp match {
              case s: List[User] =>
                complete(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s.toJson.toString)))
              case _ => complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", resp.toString, "").toJson.toString)))
            }
          case Failure(error) =>
            searchPeopleLog.error("Error is: " + error.getMessage)
            complete(HttpResponse(status = BadRequest, entity = HttpEntity(MediaTypes.`application/json`, responseMessage("", error.getMessage, "").toJson.toString)))
        }

      }

    }
  }
}
