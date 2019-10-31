package nl.bdr.fp.problem1

import java.util.UUID

import nl.bdr.fp.problem1.MyPostgresProfile.api._
import slick.dbio.Effect
import slick.sql.FixedSqlStreamingAction

object OrganisationDao {

  import Domain._
  import DomainSchema._

  def getOrganisationQuery(uuid: UUID): Query[Organisations, Organisation, Seq] =
    organisations.filter(_.id === uuid)

  def getOrganisation(uuid: UUID): FixedSqlStreamingAction[Seq[Organisation], Organisation, Effect.Read] = getOrganisationQuery(uuid).result
}
