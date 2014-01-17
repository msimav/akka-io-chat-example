import akka.actor.ActorSystem

object Main extends App {
  val system = ActorSystem("tcp-server")
  system actorOf Server.props(8080)
}
