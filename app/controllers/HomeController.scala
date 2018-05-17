package controllers

import javax.inject._

import play.api.data.Form
import play.api.data.Forms.{ optional, text }
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._

@Singleton
class HomeController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index())
  }

}
