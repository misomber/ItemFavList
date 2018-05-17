package services

import java.time.ZonedDateTime
import javax.inject.{ Inject, Singleton }

import akka.actor.ActorSystem
import com.github.j5ik2o.rakutenApi.itemSearch.{
  ImageFlagType,
  RakutenItemSearchAPI,
  RakutenItemSearchAPIConfig,
  Item => RakutenItem
}
import models.Item
import play.api.Configuration
import play.api.libs.concurrent.ActorSystemProvider

import scala.concurrent.Future
import scala.concurrent.duration._

@Singleton
class ItemServiceImpl @Inject()(configuration: Configuration, actorSystemProvider: ActorSystemProvider)
    extends ItemService {

  private implicit val system: ActorSystem = actorSystemProvider.get

  import system.dispatcher

  private val config = RakutenItemSearchAPIConfig(
    endPoint = configuration.getString("rakuten.endPoint").get,
    timeoutForToStrict = configuration.getInt("rakuten.timeoutForToStrictInSec").get seconds,
    applicationId = configuration.getString("rakuten.applicationId").get,
    affiliateId = configuration.getString("rakuten.affiliateId")
  )

  private val rakutenItemSearchAPI = new RakutenItemSearchAPI(config)

  override def searchItems(keywordOpt: Option[String]): Future[Seq[Item]] = {
    keywordOpt
      .map { keyword =>
        rakutenItemSearchAPI
          .searchItems(
            keyword = Some(keyword),
            hits = Some(20),
            imageFlag = Some(ImageFlagType.HasImage)
          )
          .map(_.Items.map(convertToItem))
      }
      .getOrElse(Future.successful(Seq.empty))
  }

  private def convertToItem(rakutenItem: RakutenItem): Item = {
    val now = ZonedDateTime.now()
    Item(
      id = None,
      code = rakutenItem.value.itemCode,
      name = rakutenItem.value.itemName,
      url = rakutenItem.value.itemUrl.toString,
      imageUrl = rakutenItem.value.mediumImageUrls.head.value.toString.replace("?_ex=128x128", ""),
      price = rakutenItem.value.itemPrice.toInt,
      createAt = now,
      updateAt = now
    )
  }

}
