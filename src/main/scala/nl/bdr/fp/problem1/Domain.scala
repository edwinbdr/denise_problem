package nl.bdr.fp.problem1

import java.time.Instant
import java.util.UUID

import play.api.libs.json.JsValue

object Domain {

  case class Organisation(id: UUID, name: String)

  case class Warehouse(id: UUID, name: String)

  case class FulfillmentOrderCreatedEvent(
                                           fulfillmentOrder: String,
                                           fulfillingOrganization: UUID,
                                           latestDeliveryDateTime: Instant,
                                           deliveryLocation: String,
                                           createdBy: String,
                                           createdAt: Instant,
                                           fulfillmentOrderContext: String)

  case class EventEntryFulfillmentOrderCreated(aggregateId: UUID, sequenceNumber: Long, fulfilmentOrderCreatedEvent: FulfillmentOrderCreatedEvent)

  case class AxonEvent(
                        aggregateId: UUID = UUID.randomUUID(),
                        sequenceNumber: Long = 0,
                        payload: JsValue,
                        payloadType: String)

}
