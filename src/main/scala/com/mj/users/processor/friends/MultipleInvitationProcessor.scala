package com.mj.users.processor.friends

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.mj.users.config.MessageConfig
import com.mj.users.model.{Connections, Friend, MultipleInvitation, responseMessage}
import com.mj.users.mongo.FriendsDao.updateUserConnections
import com.mj.users.mongo.Neo4jConnector.updateNeo4j

import scala.concurrent.ExecutionContext.Implicits.global

class MultipleInvitationProcessor extends Actor with MessageConfig {

  implicit val timeout = Timeout(500, TimeUnit.SECONDS)


  def receive = {

    case (invitationFriend: MultipleInvitation) => {
      val origin = sender()
      if (invitationFriend.connections.isDefined) {
        val response = invitationFriend.connections.get.map(friendID => {
          val script = s"MATCH (a:users {memberID:'${invitationFriend.memberID}'} ), (b:users {memberID:'${friendID.memberID}'} ) CREATE (a)-[r:FRIEND {status:'pending', conn_type:'${friendID.conn_type}'}]->(b)"
          val result = updateNeo4j(script).map(response => response match {
            case count if count > 0 => {
              updateUserConnections(Connections(invitationFriend.memberID, friendID.memberID, friendID.conn_type, "pending") ,true)
            }
            case 0 => {
              origin ! responseMessage("", s"Error found for email : ${friendID.memberID}", "")
            }
          })

          result.recover {
            case e: Throwable => {
              origin ! responseMessage("", e.getMessage, "")
            }
          }
        }
        )

        response.map(resp => origin ! responseMessage("", "", s"Connection request was successfully sent to all members"))


      } else
        origin ! responseMessage("", "", s"No Member ID to sent the connection request")


    }
  }
}
