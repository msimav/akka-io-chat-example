import akka.actor.{ActorRef, ActorLogging, Actor, Props}
import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress

object Server {
  def props(port: Int, chat: ActorRef): Props = Props(classOf[Server], port, chat)
}

class Server(port: Int, chat: ActorRef) extends Actor with ActorLogging {
  import Tcp._
  import context.system

  override def preStart = {
    IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", port))
  }

  def receive: Receive = {
    case Bound(localAddress) =>
      log info s"Connected: $localAddress"

    case CommandFailed(_: Bind) =>
      log error "Bind Error, stopping"
      context stop self

    case c @ Connected(remote, _) =>
      log info s"Client Connected: $remote"
      val handler = context actorOf ClientHandler.props(sender, chat)
      sender ! Register(handler)
      chat ! Chat.Register(handler)
  }
}
