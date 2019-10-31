package nl.bdr.fp.problem1

import java.time.Instant
import java.util.UUID

import nl.bdr.fp.problem1.Domain._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object JsonSerialization {

  implicit val organisationFormat: Format[Organisation] = (
    (__ \ "id").format[UUID] and
      (__ \ "name").format[String]
    ) (Organisation.apply, unlift(Organisation.unapply))

  implicit val warehouseFormat: Format[Warehouse] = (
    (__ \ "id").format[UUID] and
      (__ \ "name").format[String]
    ) (Warehouse.apply, unlift(Warehouse.unapply))

  implicit val fulfillmentOrderCreatedEventFormat: Format[FulfillmentOrderCreatedEvent] = (
    (__ \ "fulfillment" \ "order").format[String] and
      (__ \ "fulfillment" \ "organisation").format[UUID] and
      (__ \ "latest_delivery_date_time").format[Instant] and
      (__ \ "delivery" \ "location").format[String] and
      (__ \ "created" \ "by").format[String] and
      (__ \ "created" \ "at").format[Instant] and
      (__ \ "fulfillment" \ "order_context").format[String]
    ).apply(FulfillmentOrderCreatedEvent.apply, unlift(FulfillmentOrderCreatedEvent.unapply))

}
