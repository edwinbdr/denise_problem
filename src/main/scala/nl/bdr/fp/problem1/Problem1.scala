package nl.bdr.fp.problem1

import nl.bdr.fp.problem1.DomainSchema._
import nl.bdr.fp.problem1.MyPostgresProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

object Scratch {
  /**
   * Finds associations between fulfillment order created events and organisations and
   * reports these associations as Strings describing them.
   */
  def upcastFulfillmentOrderCreatedEvents(doUpdate: Boolean)(implicit ec: ExecutionContext): DBIOAction[Seq[String], NoStream, Effect.Read] =
    for (events <- DomainDao.getEventEntriesFulfillmentOrderCreatedEvents) yield
      for ((event, organisation) <- events) yield
        s"""The event "${event.fulfillmentOrder}" is associated with organisation "${organisation.name}""""
}

object Application extends App {

  /** An object which describes the database to connect to. */
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def sqlWithFutures(db: Database): Future[String] = {
    for {
      _ <- db.run(schema.create).recover { case e => println(e) }
      _ <- Future(println("Schema created"))
      _ <- db.run(DummyData.insertDummyData)
      _ <- Future(println("Dummy data created"))
      ordersAndOrganisations <- db.run(Scratch.upcastFulfillmentOrderCreatedEvents(doUpdate = false))
      _ <- db.run(schema.drop)
    } yield ordersAndOrganisations.mkString("\n")
  }

  val sqlWithDBIOActions: DBIOAction[String, NoStream, Effect.All] =
    DBIO.seq(
      schema.create,
      DBIO.from(Future(println("Schema created"))),
      DummyData.insertDummyData,
      DBIO.from(Future(println("Dummy data created")))
    )
      .andThen(Scratch.upcastFulfillmentOrderCreatedEvents(doUpdate = false).map(_.mkString("\n")))
      .andFinally(schema.drop)

  val db: Database = Database.forURL(url = "jdbc:postgresql://localhost/edwindejong", driver = "org.postgresql.Driver")

  println("Futures method")
  println(Await.result(sqlWithFutures(db), 30 seconds))

  println("DB Actions method")
  println(Await.result(db.run(sqlWithDBIOActions), 30 seconds))


}
