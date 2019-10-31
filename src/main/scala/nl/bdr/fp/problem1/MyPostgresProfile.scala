package nl.bdr.fp.problem1

import com.github.tminglei.slickpg.PgPlayJsonSupport
import play.api.libs.json.{JsValue, Json}
import slick.ast.BaseTypedType
import slick.jdbc.{JdbcType, PostgresProfile}

trait MyPostgresProfile extends PostgresProfile with PgPlayJsonSupport {

  import Domain._
  import JsonSerialization._

  override val pgjson: String = "jsonb"

  override val api: API = new API {}

  val plainAPI = new API with PlayJsonPlainImplicits

  trait API extends super.API with JsonImplicits {
    implicit val organisationJsonTypeMapper: JdbcType[Organisation] with BaseTypedType[Organisation] = MappedJdbcType.base[Organisation, JsValue](Json.toJson(_), _.as[Organisation])
    implicit val warehouseJsonTypeMapper: JdbcType[Warehouse] with BaseTypedType[Warehouse] = MappedJdbcType.base[Warehouse, JsValue](Json.toJson(_), _.as[Warehouse])
    implicit val fulfilmentOrderCreatedEvent: JdbcType[FulfillmentOrderCreatedEvent] with BaseTypedType[FulfillmentOrderCreatedEvent] = MappedJdbcType.base[FulfillmentOrderCreatedEvent, JsValue](Json.toJson(_), _.as[FulfillmentOrderCreatedEvent])
  }

}

object MyPostgresProfile extends MyPostgresProfile