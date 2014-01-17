import akka.actor.{ActorLogging, Actor, Props}
import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress

object Server {
  def props(port: Int): Props = Props(classOf[Server], port)
}

class Server(port: Int) extends Actor with ActorLogging {
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
      val handler = context actorOf ClientHandler.props(sender)
      sender ! Register(handler)
      Chat.chat ! Chat.Register(handler)
  }
}
