package sergey.dashko.services

import java.net.{InetAddress, Socket}
import java.io.{PrintWriter, _}

import cats.effect.{IO, Resource}

class EventSender(address: InetAddress, port: Int) {

  def sendEvents(events: Seq[String]): IO[Unit] = {
    val writerHolder: Resource[IO, PrintWriter] = for {
      socket <- Resource.fromAutoCloseable(IO(new Socket(address, port)))
      outStream <- Resource.fromAutoCloseable(IO(socket.getOutputStream))
      out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outStream)))
    } yield out

    writerHolder.use { writer => IO {
      events.foreach{ string =>
        writer.println(string)
        writer.flush()
      }}
    }}

}
