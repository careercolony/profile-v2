package com.mj.users

import java.net.InetAddress

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.routing.RoundRobinPool
import akka.stream.ActorMaterializer
import com.mj.users.config.Application
import com.mj.users.config.Application._
import com.mj.users.tools.CommonUtils._
import com.mj.users.tools.RouteUtils
import com.typesafe.config.ConfigFactory


object Server extends App {
  val seedNodesStr = seedNodes
    .split(",")
    .map(s => s""" "akka.tcp://users-cluster@$s" """)
    .mkString(",")

  val inetAddress = InetAddress.getLocalHost
  var configCluster = Application.config.withFallback(
    ConfigFactory.parseString(s"akka.cluster.seed-nodes=[$seedNodesStr]"))

  configCluster = configCluster
    .withFallback(
      ConfigFactory.parseString(s"akka.remote.netty.tcp.hostname=$hostName"))
    .withFallback(
      ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$akkaPort"))

  implicit val system: ActorSystem = ActorSystem("users-cluster", configCluster)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  //Experience
  val newExperienceProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.experience.NewExperienceProcessor]), "newExperienceProcessor")
  val updateExperienceProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.experience.UpdateExperienceProcessor]), "updateExperienceProcessor")
  val getExperienceByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.experience.GetExperienceByMemberProcessor]), "getExperienceByMemberProcessor")
  val getOneExperienceProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.experience.GetOneExperienceProcessor]), "getOneExperienceProcessor")
  val deleteExperienceProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.experience.DeleteExperienceProcessor]), "deleteExperienceProcessor")

  //Education
  val newEducationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.education.NewEducationProcessor]), "newEducationProcessor")
  val updateEducationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.education.UpdateEducationProcessor]), "updateEducationProcessor")
  val getEducationByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.education.GetEducationByMemberProcessor]), "getEducationByMemberProcessor")
  val getOneEducationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.education.GetOneEducationProcessor]), "getOneEducationProcessor")
  val deleteEducationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.education.DeleteEducationProcessor]), "deleteEducationProcessor")

  //Portfolio
  val newPortfolioProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.portfolio.NewPortfolioProcessor]), "newPortfolioProcessor")
  val updatePortfolioProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.portfolio.UpdatePortfolioProcessor]), "updatePortfolioProcessor")
  val getPortfolioByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.portfolio.GetPortfolioByMemberProcessor]), "getPortfolioByMemberProcessor")
  val getOnePortfolioProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.portfolio.GetOnePortfolioProcessor]), "getOnePortfolioProcessor")
  val deletePortfolioProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.portfolio.DeletePortfolioProcessor]), "deletePortfolioProcessor")

  //certification
  val newCertificationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.certification.NewCertificationProcessor]), "newCertificationProcessor")
  val updateCertificationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.certification.UpdateCertificationProcessor]), "updateCertificationProcessor")
  val getCertificationByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.certification.GetCertificationByMemberProcessor]), "getCertificationByMemberProcessor")
  val getOneCertificationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.certification.GetOneCertificationProcessor]), "getOneCertificationProcessor")
  val deleteCertificationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.certification.DeleteCertificationProcessor]), "deleteCertificationProcessor")

  //award
  val newAwardProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.award.NewAwardProcessor]), "newAwardProcessor")
  val updateAwardProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.award.UpdateAwardProcessor]), "updateAwardProcessor")
  val getAwardByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.award.GetAwardByMemberProcessor]), "getAwardByMemberProcessor")
  val getOneAwardProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.award.GetOneAwardProcessor]), "getOneAwardProcessor")
  val deleteAwardProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.award.DeleteAwardProcessor]), "deleteAwardProcessor")


  //language
  val newLanguageProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.language.NewLanguageProcessor]), "newLanguageProcessor")
  val updateLanguageProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.language.UpdateLanguageProcessor]), "updateLanguageProcessor")
  val getLanguageByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.language.GetLanguageByMemberProcessor]), "getLanguageByMemberProcessor")
  val deleteLanguageProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.language.DeleteLanguageProcessor]), "deleteLanguageProcessor")


  //publication
  val newPublicationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.publication.NewPublicationProcessor]), "newPublicationProcessor")
  val updatePublicationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.publication.UpdatePublicationProcessor]), "updatePublicationProcessor")
  val getPublicationByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.publication.GetPublicationByMemberProcessor]), "getPublicationByMemberProcessor")
  val getOnePublicationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.publication.GetOnePublicationProcessor]), "getOnePublicationProcessor")
  val deletePublicationProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.publication.DeletePublicationProcessor]), "deletePublicationProcessor")

  //web profile
  val newWebProfileProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.webprofile.NewWebProfileProcessor]), "newWebProfileProcessor")
  val updateWebProfileProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.webprofile.UpdateWebProfileProcessor]), "updateWebProfileProcessor")
  val getWebProfileByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.webprofile.GetWebProfileByMemberProcessor]), "getWebProfileByMemberProcessor")
  val getOneWebProfileProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.webprofile.GetOneWebProfileProcessor]), "getOneWebProfileProcessor")
  val deleteWebProfileProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.webprofile.DeleteWebProfileProcessor]), "deleteWebProfileProcessor")

  //skills
  val newSkillProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.skills.NewSkillsProcessor]), "newSkillProcessor")
  val updateSkillProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.skills.UpdateSkillsProcessor]), "updateSkillProcessor")
  val getSkillByMemberProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.skills.GetSkillsByMemberProcessor]), "getSkillByMemberProcessor")
  val deleteSkillProcessor = system.actorOf(RoundRobinPool(poolSize).props(Props[processor.skills.DeleteSkillsProcessor]), "deleteSkillProcessor")

  import system.dispatcher

  Http().bindAndHandle(RouteUtils.logRoute, "0.0.0.0", port)

  consoleLog("INFO",
    s"User server started! Access url: https://$hostName:$port/")
}
