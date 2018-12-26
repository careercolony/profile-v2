package com.mj.users.processor.friends

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Connections, Friend, responseMessage}
import com.mj.users.mongo.Neo4jConnector.updateNeo4j
import com.mj.users.mongo.FriendsDao.updateUserDetails
import com.mj.users.mongo.FriendsDao.insertUserDetails
import scala.concurrent.ExecutionContext.Implicits.global

class AcceptInvitationProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (invitationFriend: Friend) => {
      val origin = sender()
      val script = s"MATCH (me:users {memberID: '${invitationFriend.memberID}' })-[r:FRIEND]->(b:users {memberID: '${invitationFriend.inviteeID}'})  SET r.status = 'active' return b.firstname"

      val result = updateNeo4j(script).map(response =>
        response match {
          case count if count > 0 => println("here")
            updateUserDetails(Connections(invitationFriend.memberID,invitationFriend.inviteeID,invitationFriend.conn_type.get,"active")).flatMap(resp =>
             {println("1dsf:")
            insertUserDetails(Connections(invitationFriend.inviteeID,invitationFriend.memberID,invitationFriend.conn_type.get,"active"))}
          ).map(resp =>
            origin ! responseMessage("", "", s"Connection request was successfully sent to ${invitationFriend.firstName}"))
          case 0 => origin ! responseMessage("", s"Error found for email : ${invitationFriend.firstName}", "")
        })

      result.recover {
        case e: Throwable => {
          origin ! responseMessage("", e.getMessage, "")
        }
      }
    }
  }
}
