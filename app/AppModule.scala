import com.google.inject.AbstractModule
import services._

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
    bind(classOf[ItemService]).to(classOf[ItemServiceImpl])
    bind(classOf[ItemUserService]).to(classOf[ItemUserServiceImpl])
  }

}