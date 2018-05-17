import com.google.inject.AbstractModule
import services.{ UserService, UserServiceImpl }

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
  }

}