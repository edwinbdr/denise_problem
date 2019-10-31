package nl.bdr.fp.problem1

import java.time.Instant
import java.util.UUID

import nl.bdr.fp.problem1.MyPostgresProfile.api._

object DummyData {

  import Domain._
  import DomainSchema._

  private[this] val dummyOrganisations = Seq(
    Organisation(id = UUID.randomUUID(), name = "BigData Republic"),
    Organisation(id = UUID.randomUUID(), name = "JCore"),
    Organisation(id = UUID.randomUUID(), name = "JDriven")
  )
  private[this] val dummyWarehouses = Seq(
    Warehouse(id = UUID.randomUUID(), name = "Downstairs"),
    Warehouse(id = UUID.randomUUID(), name = "Upstairs"),
    Warehouse(id = UUID.randomUUID(), name = "Outside")
  )
  private[this] val dummyEvents = for ((idx, organisation) <- Stream.from(1).zip(dummyOrganisations)) yield
    FulfillmentOrderCreatedEvent(
      fulfillmentOrder = "order" + (idx.hashCode() % 500),
      fulfillingOrganization = organisation.id,
      latestDeliveryDateTime = Instant.now().plusSeconds(3600 * idx),
      deliveryLocation = "location" + (100 - idx),
      createdBy = "createdBy" + idx,
      createdAt = Instant.now().minusSeconds(3600 * idx),
      fulfillmentOrderContext = "context")

  import JsonSerialization._

  private[this] val dummyAxonEvents = for ((event, idx) <- dummyEvents.zipWithIndex) yield
    AxonEvent(
      aggregateId = UUID.randomUUID(),
      sequenceNumber = idx,
      payload = fulfillmentOrderCreatedEventFormat.writes(event),
      payloadType = event.getClass.getName
    )

  val insertDummyData: DBIOAction[Unit, NoStream, Effect.Write] =
    DBIO.seq(
      organisations ++= dummyOrganisations,
      warehouses ++= dummyWarehouses,
      axonEvents ++= dummyAxonEvents)
}
