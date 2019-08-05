package sample.controller
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import java.net.URL
import java.util.*
class AdminController:Initializable {
    @FXML
    lateinit var custompane: Pane
    lateinit var FileMenu:Menu
    lateinit var CloseWindow:MenuItem
    lateinit var About:MenuItem
    lateinit var logout:MenuItem
    override fun initialize(location: URL?, resources: ResourceBundle?) {

        FileMenu.accelerator=KeyCombination.keyCombination("Alt+M")
        CloseWindow.accelerator= KeyCombination.keyCombination("Alt+C")
        About.accelerator= KeyCombination.keyCombination("Alt+A")
        logout.accelerator= KeyCombination.keyCombination("Alt+L")
        runBlocking {
            var loadpane: Pane = async{
                FXMLLoader.load<Pane>(javaClass.getResource("/sample/fxml/Dashboard.fxml"))
            }.await()
            custompane.children.add(loadpane)
        }

    }


    fun loaddash()= runBlocking {

            var loadpane: Pane = async{
                FXMLLoader.load<Pane>(javaClass.getResource("/sample/fxml/Dashboard.fxml"))
            }.await()
            custompane.children.clear()
            custompane.children.add(loadpane)


     }
     fun closeadmin(){
        Platform.exit()
    }
    fun loadpartypanel(){
        var loadpane: Pane = FXMLLoader.load(javaClass.getResource("/sample/fxml/PartyPanel.fxml"))
        custompane.children.clear()
        custompane.children.add(loadpane)
    }
    fun logout(evt:ActionEvent){

        var parent: Parent = FXMLLoader.load(javaClass.getResource("/sample/fxml/Main.fxml"))
        var stage = Stage()
        var scene = Scene(parent)
        stage.scene = scene
        stage.sizeToScene()
        stage.icons.add(Image("/sample/Icons/AppIcon.png"))
        stage.title = "Login"
        // stage.isFullScreen = true
       // stage.isMaximized = true
        stage.initStyle(StageStyle.UNDECORATED)
        var m=evt.source as MenuItem
        while(m.parentPopup==null){
            var menu=m.parentMenu
        }

        m.parentPopup.ownerWindow.scene.window.hide()
        stage.show()


    }

}