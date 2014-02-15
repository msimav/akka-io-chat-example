import akka.actor.ActorSystem

object Main extends App {
  val system = ActorSystem("tcp-server")
  val chat = system.actorOf(Chat.props, "chat-server-actor")
  val tcp = system.actorOf(Server.props(8080, chat), "tcp-server-actor")
}
