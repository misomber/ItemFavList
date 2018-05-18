package models

import java.time.ZonedDateTime

import scalikejdbc._, jsr310._
import skinny.orm.{ Alias, SkinnyCRUDMapper, SkinnyJoinTable }

case class ItemUser(id: Option[Long],
                    itemId: Long,
                    userId: Long,
                    `type`: String,
                    createAt: ZonedDateTime,
                    updateAt: ZonedDateTime)
    extends SkinnyJoinTable[ItemUser] {
  override def defaultAlias: Alias[ItemUser] = createAlias("iu")
}

object ItemUser extends SkinnyCRUDMapper[ItemUser] {

  override def tableName = "item_user"

  override def defaultAlias: Alias[ItemUser] = createAlias("iu")

  private def toNamedValues(record: ItemUser): Seq[(Symbol, Any)] = Seq(
    'itemId   -> record.itemId,
    'userId   -> record.userId,
    'type     -> record.`type`,
    'createAt -> record.createAt,
    'updateAt -> record.updateAt
  )

  override def extract(rs: WrappedResultSet, n: ResultName[ItemUser]): ItemUser =
    autoConstruct(rs, n)

  def create(itemUser: ItemUser)(implicit session: DBSession = AutoSession): Long =
    createWithAttributes(toNamedValues(itemUser): _*)

  def update(itemUser: ItemUser)(implicit session: DBSession = AutoSession): Int =
    updateById(itemUser.id.get).withAttributes(toNamedValues(itemUser): _*)

}
