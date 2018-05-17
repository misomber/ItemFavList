package controllers

import javax.inject._

import jp.t2v.lab.play2.auth.OptionalAuthElement
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import services.UserService

@Singleton
class HomeController @Inject()(val userService: UserService, val messagesApi: MessagesApi)
    extends Controller
    with I18nSupport
    with AuthConfigSupport
    with OptionalAuthElement {

  def index: Action[AnyContent] = StackAction { implicit request =>
    Ok(views.html.index(loggedIn))
  }

}
