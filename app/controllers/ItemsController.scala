package controllers

import javax.inject.{ Inject, Singleton }

import akka.pattern.AskTimeoutException
import akka.util.Timeout
import com.github.j5ik2o.rakutenApi.itemSearch.ItemSearchException
import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc._
import services.{ ItemService, ItemServiceImpl, UserService }

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration._

@Singleton
class ItemsController @Inject()(val userService: UserService,
                                val itemService: ItemService,
                                val messagesApi: MessagesApi)(implicit ec: ExecutionContext)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with AuthenticationElement {

  implicit val timeout = Timeout(20 seconds)

  private val searchItemform: Form[Option[String]] = Form {
    "keyword" -> optional(text)
  }

  private def recoderHandler(
      currentUser: User,
      keywordOpt: Option[String]
  )(implicit requestHeader: RequestHeader): PartialFunction[Throwable, Result] = {
    case ex: ItemSearchException =>
      Logger.error(Messages("RakutenAPICallError"), ex)
      InternalServerError(
        views.html.items
          .index(currentUser,
                 searchItemform.fill(keywordOpt).withGlobalError(Messages("RakutenAPICallError")),
                 Seq.empty)
      )
    case ex: AskTimeoutException =>
      Logger.error(Messages("RakutenAPICallError"), ex)
      InternalServerError(
        views.html.items
          .index(currentUser,
                 searchItemform.fill(keywordOpt).withGlobalError(Messages("RakutenAPICallError")),
                 Seq.empty)
      )
  }

  private def searchItems(
      currentUser: User,
      keywordOpt: Option[String]
  )(implicit requestHeader: RequestHeader): Future[Result] =
    itemService
      .searchItems(keywordOpt)
      .map { items =>
        Logger.debug(s"items = $items")
        Ok(views.html.items.index(currentUser, searchItemform.fill(keywordOpt), items))
      }
      .recover(recoderHandler(currentUser, keywordOpt))

  def index(keywordOpt: Option[String]): Action[AnyContent] = AsyncStack { implicit request =>
    searchItems(loggedIn, keywordOpt)
  }

  def show(id: Long): Action[AnyContent] = AsyncStack { implicit request =>
    val user = loggedIn
    itemService
      .getItemById(id)
      .map { itemOpt =>
        itemOpt
          .map { item =>
            Ok(views.html.items.show(Some(user), item))
          }
          .getOrElse(InternalServerError(Messages("InternalError")))
      }
  }

}
