package sample.controller
import com.jfoenix.controls.JFXComboBox
import javafx.animation.Animation.INDEFINITE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import javafx.util.Duration
import kotlinx.coroutines.*
import sample.Party
import sample.partynames
import sample.viewparty
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DashboardController :Initializable {
    @FXML
    var datelbl: Label = Label()
    lateinit var timelbl: Label
    lateinit var partytable: TableView<Party>
    lateinit var Name: TableColumn<Party, String>
    lateinit var GSTIN: TableColumn<Party, String>
    lateinit var GSTValue: TableColumn<Party, String>


    override fun initialize(location: URL?, resources: ResourceBundle?) {


        Name.cellValueFactory = PropertyValueFactory("Name")
        GSTIN.cellValueFactory = PropertyValueFactory("GSTIN")
        GSTValue.cellValueFactory = PropertyValueFactory("GSTValue")

        //partytable.columnResizePolicy=TableView.CONSTRAINED_RESIZE_POLICY
        GlobalScope.launch {
            var partymodel = withContext(Dispatchers.IO) { FXCollections.observableArrayList(viewparty()) }

            partytable.items = partymodel


        }

            Name.prefWidth = 165.0
            GSTIN.prefWidth = 140.0
            GSTValue.prefWidth = 87.0
            var timeline = Timeline(KeyFrame(Duration.seconds(1.0), EventHandler<ActionEvent> {
                var cal = GregorianCalendar();
                var localdate = LocalDateTime.now()
                var second = cal.get(Calendar.SECOND)
                var minute = cal.get(Calendar.MINUTE)
                var hour = cal.get(Calendar.HOUR)
                timelbl.text = "$hour:$minute:$second"
                datelbl.text = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localdate)
            }))

            timeline.run {
                cycleCount = INDEFINITE
                play()
            }
        }





    }







