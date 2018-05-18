package controllers

import javax.inject._

import jp.t2v.lab.play2.auth.OptionalAuthElement
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc._
import services.{ ItemService, UserService }

@Singleton
class HomeController @Inject()(val userService: UserService,
                               val itemService: ItemService,
                               val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with OptionalAuthElement {

  def index: Action[AnyContent] = StackAction { implicit request =>
    itemService
      .getLatestItems()
      .map { items =>
        Ok(views.html.index(loggedIn, items))
      }
      .getOrElse(InternalServerError(Messages("InternalError")))
  }
}
