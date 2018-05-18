package services

import java.time.ZonedDateTime
import javax.inject.Singleton

import models.{ ItemUser, WantHaveType }
import scalikejdbc._

@Singleton
class ItemUserServiceImpl extends ItemUserService {

  override def want(userId: Long, itemId: Long): Boolean = DB.localTx { implicit session =>
    if (isWantingById(userId, itemId)) false
    else {
      val now = ZonedDateTime.now()
      val itemUser = ItemUser(
        None,
        itemId,
        userId,
        WantHaveType.Want.toString,
        now,
        now
      )
      ItemUser.create(itemUser) > 0L
    }
  }

  override def doNotWant(userId: Long, itemId: Long): Boolean = DB.localTx { implicit session =>
    if (isWantingById(userId, itemId)) {
      val column = ItemUser.column
      ItemUser.deleteBy(
        sqls
          .eq(column.itemId, itemId)
          .and
          .eq(column.userId, userId)
          .and
          .eq(column.`type`, WantHaveType.Want.toString)
      ) == 1
    } else false
  }

  private def isWantingById(userId: Long, itemId: Long)(implicit dbSession: DBSession): Boolean = {
    val column = ItemUser.column
    ItemUser.countBy(
      sqls
        .eq(column.itemId, itemId)
        .and
        .eq(column.userId, userId)
        .and
        .eq(column.`type`, WantHaveType.Want.toString)
    ) == 1L
  }

}