package services

import models._
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

trait UserService {

  def create(user: User)(implicit dbSession: DBSession = AutoSession): Try[Long]

  def findById(userId: Long)(implicit dbSession: DBSession = AutoSession): Try[Option[User]]

  def findByEmail(email: String)(implicit dbSession: DBSession = AutoSession): Try[Option[User]]

}
