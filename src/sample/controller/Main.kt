package sample.controller

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle

class Main:Application(){
    override fun start(stage:Stage) {
      stage.scene= Scene(FXMLLoader.load(javaClass.getResource("/sample/fxml/Main.fxml")))
        stage.title="Login"
        stage.icons.add(Image("/sample/Icons/AppIcon.png"))
        stage.initStyle(StageStyle.UNDECORATED)
        stage.show()
    }
}
fun main(){
    Application.launch(Main::class.java)
}