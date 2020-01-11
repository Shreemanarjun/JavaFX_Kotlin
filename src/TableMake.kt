import javafx.collections.FXCollections
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import kotlinx.coroutines.*
import sample.DatabaseProvider
import sample.Party
import sample.Report

var db=DatabaseProvider()
fun partytablemake(TableName:TableView<Party>,Name:TableColumn<Party,String>,GSTIN:TableColumn<Party,String>,GstValue:TableColumn<Party,String>){
    Name.cellValueFactory = PropertyValueFactory("Name")
    GSTIN.cellValueFactory = PropertyValueFactory("GSTIN")
    GstValue.cellValueFactory = PropertyValueFactory("GstValue")
    TableName.placeholder=Label("No information added yet.")
    GlobalScope.launch {
        val partymodel = withContext(Dispatchers.Main){ FXCollections.observableArrayList(db.viewparty()) }
            TableName.items=partymodel
    }
    TableName.columnResizePolicy=TableView.UNCONSTRAINED_RESIZE_POLICY
}
fun reporttablemake(reportname:String,TableName: TableView<Report>,sl:TableColumn<Report,Int>,date:TableColumn<Report,String>,invoiceno:TableColumn<Report,String>,gstin:TableColumn<Report,String>,
                    name:TableColumn<Report,String>, amount: TableColumn<Report,Double>, sgsttax:TableColumn<Report,Double>,cgsttax:TableColumn<Report,Double>,
                    sgstv:TableColumn<Report,Double>, cgstv:TableColumn<Report,Double>, totalamt:TableColumn<Report,Double>){
    sl.cellValueFactory=PropertyValueFactory("sl")
    date.cellValueFactory=PropertyValueFactory("date")
    invoiceno.cellValueFactory=PropertyValueFactory("invoiceno")
    gstin.cellValueFactory=PropertyValueFactory("gstin")
    name.cellValueFactory=PropertyValueFactory("name")
    amount.cellValueFactory=PropertyValueFactory("amount")
    sgsttax.cellValueFactory=PropertyValueFactory("sgsttax")
    cgsttax.cellValueFactory=PropertyValueFactory("cgsttax")
    sgstv.cellValueFactory=PropertyValueFactory("sgstv")
    cgstv.cellValueFactory=PropertyValueFactory("cgstv")
    totalamt.cellValueFactory=PropertyValueFactory("totalamt")
    TableName.placeholder=Label("No information added yet.")
    GlobalScope.launch {
    val reportmodel= withContext(Dispatchers.Main){FXCollections.observableArrayList(db.reportinfo(reportname))}
    TableName.items=reportmodel

}

}