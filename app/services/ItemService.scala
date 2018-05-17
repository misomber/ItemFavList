package services

import models.Item

import scala.concurrent.Future

trait ItemService {

  def searchItems(keywordOpt: Option[String]): Future[Seq[Item]]

}