package sample.controller

import com.jfoenix.controls.*
import javafx.application.Application.launch

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader.load
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.stage.Stage
import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sample.connect
import java.net.URL
import java.util.*
import kotlinx.coroutines.*



class Controller:Initializable{
    @FXML
    lateinit var txtUserName:JFXTextField
    lateinit var txtPassword:JFXPasswordField
    lateinit var lblStatus:Label
    lateinit var DBurl:JFXTextField
    lateinit var DBuser:JFXTextField
    lateinit var DBpass:JFXTextField
    lateinit var DBlist:JFXComboBox<String>
     lateinit var pindicator:ProgressIndicator
    lateinit var savebtn:JFXButton
    lateinit var ExitButton:JFXButton
    lateinit var loginbtn:JFXButton
    lateinit var ExitButton1:JFXButton
    lateinit var logintextarea:JFXTextArea
    var alert=Alert(Alert.AlertType.INFORMATION)


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        savebtn.isVisible=false
        var exitimage=ImageView(Image("/sample/Icons/shut.png"))
        exitimage.isPreserveRatio=true
        exitimage.isSmooth=true
        exitimage.fitWidth=46.0
        exitimage.fitHeight=34.0
        ExitButton.graphic=exitimage
        ExitButton1.graphic=exitimage
        var dblist= arrayOf("Mysql","Custom")
        var obs=FXCollections.observableList(dblist.toList())
        DBlist.items.clear()
        DBlist.items=obs
        DBlist.value=obs.get(0)
    }
    fun login(evt:ActionEvent){
        when {
            txtUserName.text=="admin" && txtPassword.text=="pass" -> {
                lblStatus.text = "User logged in successfully"

                    var parent: Parent = load(javaClass.getResource("/sample/fxml/AdminPanel.fxml"))
                    var stage = Stage()
                    var scene = Scene(parent)
                    stage.scene = scene
                    stage.sizeToScene()
                    stage.icons.add(Image("/sample/Icons/AppIcon.png"))
                    stage.title = "Admin Panel"
                    // stage.isFullScreen = true
                    stage.isMaximized = true
                    stage.initStyle(StageStyle.UNDECORATED)
                    stage.show()
                    (evt.source as Node).scene.window.hide()


            }
            else -> {

                alert.alertType=Alert.AlertType.WARNING
                lblStatus.text="User login failed"
                alert.title="Login failed"
                lblStatus.textFill=Color.RED
                alert.contentText="Hey your credentials are incorrect,You can not enter to system"
                alert.showAndWait()
            }
        }

    }

   fun check(){
          var url=DBurl.text
        var uname=DBuser.text
        var pass=DBpass.text
        if(connect(url, uname, pass)){
            var i=0.0
            pindicator.style="-fx-progress-color:blue;"
            while (i<=1.0){
                i+=0.1
                pindicator.progress=i
            }
            alert.alertType=Alert.AlertType.CONFIRMATION
            alert.title="Connected successfully to the database."
            alert.headerText=null
            alert.contentText="You can now save the settings."
           var result= alert.showAndWait()
            if (result.get()== ButtonType.OK){
                savebtn.isVisible=true
            }
            else{
                pindicator.progress=0.0
                savebtn.isVisible=false
            }


        }
        else {
            pindicator.progress=0.0
            pindicator.style="-fx-progress-color:red;"
            alert.title = "failed to connect"
            alert.contentText="You can not save setting .Please Provide correct configuration."
            savebtn.isVisible=false
            alert.showAndWait()

        }
    }
    fun onselect(){
        if(DBlist.selectionModel.selectedIndex==0){
            DBurl.text="jdbc:mysql://localhost:3306/gst"
            DBuser.text="root"
            DBpass.text="password"
        }
        else if(DBlist.selectionModel.selectedItem=="Custom"){
            DBurl.text=""
            DBuser.text=""
            DBpass.text=""

        }
    }
    fun close(){
        Platform.exit()
    }
}