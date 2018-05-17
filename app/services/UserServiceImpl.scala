package services

import javax.inject._
import models.User
import scalikejdbc.DBSession

import scala.util.Try

@Singleton
class UserServiceImpl extends UserService {

  override def create(user: User)(implicit dbSession: DBSession): Try[Long] = Try {
    User.create(user)
  }

  override def findById(userId: Long)(implicit dbSession: DBSession): Try[Option[User]] =
    Try {
      User.findById(userId)
    }

  override def findByEmail(email: String)(implicit dbSession: DBSession): Try[Option[User]] =
    Try {
      User.where('email -> email).apply().headOption
    }

}