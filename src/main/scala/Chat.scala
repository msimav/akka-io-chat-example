import akka.actor.{ActorSystem, ActorRef, Actor, Props}

object Chat {
  def props: Props = Props[Chat]

  // TODO: Think better implementation
  var _chat: Option[ActorRef] = None
  def chat(implicit system: ActorSystem) = _chat getOrElse {
    val ref = system actorOf props
    _chat = Some(ref)
    ref
  }

  sealed trait ChatProtocol
  case class Register(client: ActorRef) extends ChatProtocol
  case class UnRegister(client: ActorRef) extends ChatProtocol
  case class BroadCast(msg: String) extends ChatProtocol
}

class Chat extends Actor {
  import Chat._

  var clients = Set.empty[ActorRef]

  def receive: Receive = {
    case Register(client) =>
      clients += client

    case UnRegister(client) =>
      clients -= client

    case b @ BroadCast(msg) =>
      clients foreach { _ ! b }
  }
}
