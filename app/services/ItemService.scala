package services

import models._
import scalikejdbc.{ AutoSession, DBSession }

import scala.concurrent.Future
import scala.util.Try

trait ItemService {

  def searchItems(keywordOpt: Option[String]): Future[Seq[Item]]

  def getItemByCode(itemCode: String)(implicit dbSession: DBSession = AutoSession): Future[Option[Item]]

  def getItemAndCreateByCode(itemCode: String)(implicit dbSession: DBSession = AutoSession): Future[Item]

  def getItemsByUserId(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Seq[Item]]

  def getItemById(itemId: Long)(implicit dbSession: DBSession = AutoSession): Future[Option[Item]]

  def getLatestItems(limit: Int = 20): Try[Seq[Item]]

  def getItemsByRanking(`type`: WantHaveType.Value)(implicit dbSession: DBSession = AutoSession): Try[Seq[(Item, Int)]]

}