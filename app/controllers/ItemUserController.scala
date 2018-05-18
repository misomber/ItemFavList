package controllers

import javax.inject.{ Inject, Singleton }

import jp.t2v.lab.play2.auth.AuthenticationElement
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, AnyContent, Controller }
import services.{ ItemService, ItemUserService, UserService }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ItemUserController @Inject()(
    val userService: UserService,
    val itemService: ItemService,
    val itemUserService: ItemUserService,
    val messagesApi: MessagesApi
)(implicit ec: ExecutionContext)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with AuthenticationElement {

  private val itemCodeForm: Form[String] = Form {
    "itemCode" -> nonEmptyText
  }

  def want: Action[AnyContent] = AsyncStack { implicit request =>
    itemCodeForm
      .bindFromRequest()
      .fold(
        { formWithError =>
          Future.successful(BadRequest(formWithError.errors.flatMap(_.messages).mkString(", ")))
        }, { itemCode =>
          itemService.getItemAndCreateByCode(itemCode).map { result =>
            if (itemUserService.want(loggedIn.id.get, result.id.get))
              Ok
            else
              InternalServerError(Messages("InternalError"))
          }
        }
      )
  }

  def doNotWant: Action[AnyContent] = AsyncStack { implicit request =>
    itemCodeForm
      .bindFromRequest()
      .fold(
        { formWithError =>
          Future.successful(BadRequest(formWithError.errors.flatMap(_.messages).mkString(", ")))
        }, { itemCode =>
          itemService.getItemByCode(itemCode).map { resultOpt =>
            lazy val errorResponse = InternalServerError(Messages("InternalError"))
            resultOpt
              .map { result =>
                if (itemUserService.doNotWant(loggedIn.id.get, result.id.get))
                  Ok
                else
                  errorResponse
              }
              .getOrElse(errorResponse)
          }
        }
      )
  }

}
