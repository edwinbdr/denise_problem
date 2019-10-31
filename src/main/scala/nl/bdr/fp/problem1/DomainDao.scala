package nl.bdr.fp.problem1

import nl.bdr.fp.problem1.MyPostgresProfile.api._
import play.api.libs.json.{JsError, JsResult, JsSuccess, Json}

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

object DomainDao {

  import Domain._
  import DomainSchema._

  def eventsOfType[T](implicit t: ClassTag[T]): Query[AxonEvents, AxonEvent, Seq] =
    axonEvents.filter(_.payloadType === t.runtimeClass.getName)

  private[this] val getEventEntriesFulfillmentOrderCreatedEventsQuery =
    for {
      (e, o) <- eventsOfType[FulfillmentOrderCreatedEvent] join organisations on (_.payload +> "fulfillment" +>> "organisation" === _.id.asColumnOf[String])
    } yield (e.payload, o)

  def getEventEntriesFulfillmentOrderCreatedEvents(implicit ec: ExecutionContext): DBIOAction[Seq[(FulfillmentOrderCreatedEvent, Organisation)], NoStream, Effect.Read with Effect] =
    getEventEntriesFulfillmentOrderCreatedEventsQuery.result.flatMap { results =>
      import JsonSerialization._

      val jsResult = results.foldLeft[JsResult[Seq[(FulfillmentOrderCreatedEvent, Organisation)]]](JsSuccess(Seq[(FulfillmentOrderCreatedEvent, Organisation)]())) {
        case (curResult, (json, organisation)) =>
          for {
            seq <- curResult
            event <- fulfillmentOrderCreatedEventFormat.reads(json)
          } yield (event, organisation) +: seq
      }
      jsResult match {
        case JsSuccess(v, _) => DBIO.successful(v)
        case JsError(e) => DBIO.failed(new Exception(Json.prettyPrint(JsError.toJson(e))))
      }
    }
}
