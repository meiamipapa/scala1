import scala.actors._
import java.lang.Thread


// Actorクラスを継承 actメソッドを実装して
class SampleActor extends Actor {
   def act():Unit ={
    println("SampleActor start")
 
    // receiveはメッセージを受け取るまでスレッドを止める
    receive {
      case "hello" => {
         println("hello Main.")
      }
    }
 

    Actor.loop
    {
      // reactは、メッセージを処理している間のみスレッドが起動する。
      // Actor.loopと組み合わせていわゆるメッセージループ
      react {
        case "what your name?" => {
            reply("my name is SampleActor") // replyで返信
        }
        case "stop" => {
           println("SampleActor exit")
           reply(0)
           exit
        }
      }
    } 
  }
}

object Main
{
  def main( arg:Array[String] ):Unit = {
    // startでアクターを開始する
    val lst = List(new SampleActor().start,
                   new SampleActor().start,
                   new SampleActor().start )

    // ! で非同期メッセージを投げる
    lst.foreach{ _ ! "hello" }

    // !? で同期メッセージを投げる（返信まち）
    //   式の途中ではプレースホルダが使えないみたい。
    lst.foreach{ obj:Actor => println( obj !? "what your name?") }

    // Thread.sleep(1000*30)
    //val line = Console.readLine

    // 終了のお願い
    lst.foreach{ _ !? "stop" }

    println("Main exit")
  }
}
