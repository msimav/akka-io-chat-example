import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import akka.io.Tcp.{Write, Received, PeerClosed}
import akka.util.ByteString

object ClientHandler {
  def props(socket: ActorRef, chat: ActorRef): Props = Props(classOf[ClientHandler], socket, chat)
}

class ClientHandler(socket: ActorRef, chat: ActorRef) extends Actor with ActorLogging {
  import Chat._

  def receive: Receive = {
    case Received(data) =>
      val msg = data.utf8String
      log info s"Received: $msg"
      chat ! BroadCast(msg)

    case PeerClosed =>
      log info s"Connection Closed: ${self.path}"
      chat ! UnRegister(self)

    case BroadCast(msg) =>
      socket ! Write(ByteString(msg))
  }
}
