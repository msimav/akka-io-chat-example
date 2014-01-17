import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import akka.io.Tcp.{Write, Received, PeerClosed}
import akka.util.ByteString

object ClientHandler {
  def props(socket: ActorRef): Props = Props(classOf[ClientHandler], socket)
}

class ClientHandler(socket: ActorRef) extends Actor with ActorLogging{
  import Chat._
  import context.system

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
