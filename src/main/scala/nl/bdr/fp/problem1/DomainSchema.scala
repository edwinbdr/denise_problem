package nl.bdr.fp.problem1

import java.util.UUID

import nl.bdr.fp.problem1
import play.api.libs.json.JsValue

object DomainSchema {

  import Domain._
  import MyPostgresProfile.api._

  class AxonEvents(tag: Tag) extends Table[AxonEvent](tag, "AXON_EVENTS") {
    def aggregateId = column[UUID]("AGGREGATE_ID", O.PrimaryKey)

    def sequenceNumber = column[Long]("SEQUENCE_NUMBER")

    def payload = column[JsValue]("payload")

    def payloadType = column[String]("payload_type")

    def * =
      (aggregateId, sequenceNumber, payload, payloadType) <> (AxonEvent.tupled, AxonEvent.unapply)
  }

  val axonEvents = TableQuery[AxonEvents]

  class Organisations(tag: Tag) extends Table[Organisation](tag, "ORGANISATION") {
    def id = column[UUID]("ID", O.PrimaryKey)

    def name = column[String]("NAME")

    def * = (id, name) <> (Organisation.tupled, Organisation.unapply)
  }

  val organisations = TableQuery[Organisations]

  class Warehouses(tag: Tag) extends Table[Warehouse](tag, "WAREHOUSES") {
    def id = column[UUID]("ID", O.PrimaryKey)

    def name = column[String]("NAME")

    def * = (id, name) <> (Warehouse.tupled, Warehouse.unapply)
  }

  val warehouses = TableQuery[Warehouses]

  val schema: problem1.MyPostgresProfile.DDL =
    axonEvents.schema ++ organisations.schema ++ warehouses.schema
}
