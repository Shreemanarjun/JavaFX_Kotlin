package sample.controller

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXDatePicker
import com.jfoenix.controls.JFXTextField
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import reporttablemake
import sample.DatabaseProvider
import sample.Report
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ReportController:Initializable{
    @FXML
    lateinit var reportname: JFXTextField
    lateinit var partynamebox: JFXComboBox<String>
    lateinit var gstinbox: JFXComboBox<String>
    lateinit var cgst: JFXTextField
    lateinit var sgst: JFXTextField
    lateinit var date: JFXDatePicker
    lateinit var cgstvalue: JFXTextField
    lateinit var sgstvalue: JFXTextField
    lateinit var taxvaluebox: JFXComboBox<String>
    lateinit var invoicetxtfield: JFXTextField
    lateinit var  amounttxtfield: JFXTextField
    lateinit var  totalamount: JFXTextField
    lateinit var  addpartybtn: JFXButton
    lateinit var  removepartybtn: JFXButton
    lateinit var  printtablebtn: JFXButton
    lateinit var reset:JFXButton
    lateinit var reporttable: TableView<Report>
    lateinit var createreportbtn: JFXButton
    lateinit var createreportstatus: Label
    lateinit var roundbox:JFXComboBox<String>
    lateinit var sl:TableColumn<Report,Int>
    lateinit var dater:TableColumn<Report,String>
    lateinit var invoiceno:TableColumn<Report,String>
    lateinit var gstin:TableColumn<Report,String>
    lateinit var name:TableColumn<Report,String>
    lateinit var amount: TableColumn<Report,Double>
    lateinit var sgsttax:TableColumn<Report,Double>
    lateinit var cgsttax:TableColumn<Report,Double>
    lateinit var sgstv:TableColumn<Report,Double>
    lateinit var cgstv:TableColumn<Report,Double>
    lateinit var totalamt:TableColumn<Report,Double>

    var db=DatabaseProvider()
    var globaltotal:BigDecimal= BigDecimal(0.0)
    val rx="[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)".toRegex()
    var alert=Alert(Alert.AlertType.INFORMATION)
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        roundbox.items=FXCollections.observableArrayList("UP","DOWN","NO ROUND")

        partyboxmake(partynamebox)
        gstinboxmake()
        gstvaluechange()
        amountfieldtextchange()
      //  reporttablemake("dd",reporttable,sl, dater, invoiceno, gstin, name, amount, sgsttax, cgsttax, sgstv, cgstv, totalamt)

    }
    @FXML
   fun createreport(){
        val okimage= ImageView(Image("/sample/Icons/ok.png"))
        okimage.isSmooth=true
        okimage.isPreserveRatio=true
        okimage.fitWidth=28.0
        okimage.fitHeight=25.0
        val erimage=ImageView(Image("/sample/Icons/err.png"))
        erimage.isSmooth=true
        erimage.isPreserveRatio=true
        erimage.fitWidth=38.0
        erimage.fitHeight=31.0
      val rname=reportname.text
          if(rname.isNullOrEmpty()){
              println("name is empty")
              reportname.requestFocus()

          }
        else{
              println(name)
              GlobalScope.launch {
                  withContext(Dispatchers.Main){
                      if (db.reportcreation(rname)){
                          createreportstatus.graphic= okimage
                          createreportstatus.style="-fx-text-fill:green"
                          createreportstatus.text="Report created successfully"
                          reporttablemake(rname,reporttable,sl, dater, invoiceno, gstin, name, amount, sgsttax, cgsttax, sgstv, cgstv, totalamt)

                      }
                      else {
                          createreportstatus.graphic = erimage
                          createreportstatus.style="-fx-text-fill:red"
                          createreportstatus.text = "Failed to create report .Report already created"
                      }
                  }
              }

          }
   }
fun deletereport(){
    val okimage= ImageView(Image("/sample/Icons/ok.png"))
    okimage.isSmooth=true
    okimage.isPreserveRatio=true
    okimage.fitWidth=28.0
    okimage.fitHeight=25.0
    val erimage=ImageView(Image("/sample/Icons/err.png"))
    erimage.isSmooth=true
    erimage.isPreserveRatio=true
    erimage.fitWidth=38.0
    erimage.fitHeight=31.0
    val rname=reportname.text
    if(rname.isNullOrEmpty()){
        println("name is empty")
        reportname.requestFocus()

    }
    else {

        if (rname == "gstvalue" || rname == "party") {
            createreportstatus.graphic = erimage
            createreportstatus.style = "-fx-text-fill:red"
            createreportstatus.text = "Default table cannot be deleted.\nPlease change the name of report. "
        } else if (db.reportdeletion(rname)) {
            createreportstatus.graphic = okimage
            createreportstatus.style = "-fx-text-fill:blue"
            createreportstatus.text = "Report deleted successfully"
            reporttablemake(rname, reporttable, sl, dater, invoiceno, gstin, name, amount, sgsttax, cgsttax, sgstv, cgstv, totalamt)

        } else {
            createreportstatus.graphic = erimage
            createreportstatus.style = "-fx-text-fill:red"
            createreportstatus.text = "Failed to delete report.Report not created yet "
        }




    }
}
    fun addparty() {
        val status=createreportstatus.text
        val rname = reportname.text
        val pname = partynamebox.selectionModel.selectedItem
        val gsn = gstinbox.selectionModel.selectedItem
        val tax = taxvaluebox.selectionModel.selectedItem
        val c = cgst.text
        val s = sgst.text
        var d:String=""
        if(date.value!=null){
            val ds1=date.value.toString()
            if(!ds1.isBlank()) {
                val sdf1 = SimpleDateFormat("yyyy-MM-dd")
                val sdf2 = SimpleDateFormat("dd-MM-yyyy")
               d = sdf2.format(sdf1.parse(ds1))
                print("Date is $d")
            }
        }
        else{
            date.requestFocus()
            d= null.toString()
        }


        val inv = invoicetxtfield.text
        val amt = amounttxtfield.text
        val cv = cgstvalue.text
        val sv = sgstvalue.text
        val tm = totalamount.text
if(status=="Report created successfully"){
    when{
        rname.isNullOrBlank()->reportname.requestFocus()
        pname.isNullOrBlank()->partynamebox.requestFocus()
        gsn.isNullOrBlank()->gstinbox.requestFocus()
        tax.isNullOrBlank()->taxvaluebox.requestFocus()
        c.isNullOrBlank()->cgst.requestFocus()
        s.isNullOrBlank()->sgst.requestFocus()
       date.value==null || date.value.toString().isBlank()->date.requestFocus()
        inv.isNullOrBlank()->invoicetxtfield.requestFocus()
        amt.isNullOrBlank() or (amt == "0") ->amounttxtfield.requestFocus()
        cv.isNullOrBlank()->cgstvalue.requestFocus()
        sv.isNullOrBlank()->sgstvalue.requestFocus()
        tm.isNullOrBlank()->totalamount.requestFocus()
        else->{
          if(!db.reportAdd(reportname = rname,invoiceno = inv,date =d ,
                          gstin = gsn,name =pname ,amount =amt.toDouble() ,cgsttax =c.toDouble() ,sgsttax =s.toDouble() ,
                          cgstvalue = cv.toDouble(),sgstvalue =sv.toDouble() ,totalamt =tm.toDouble() )) {
              showalert("error", "Failed to add in report", "Same invoice number is already added.\nPlease change the invoice number")
              invoicetxtfield.requestFocus()
          }
              else
            reporttablemake(rname,reporttable,sl, dater, invoiceno, gstin, name, amount, sgsttax, cgsttax, sgstv, cgstv, totalamt)
        }

    }
}
else{
    showalert("error","Failed to add party in report","Please create report first")
    reportname.requestFocus()
}
    }
    fun removeparty(){
        if (createreportstatus.text=="Report created successfully") {
            if (reporttable.selectionModel.selectedItem != null) {
                val item = reporttable.selectionModel.selectedItem
                val invoice: String? = item.invoiceno
                val r=reportname.text
                if (invoice.isNullOrBlank()) {
                    showalert("error", "invoice no is empty", "Please enter the invoice no.")
                }
                println("report name:$r and invoice:$invoice ")
               if (db.reportremove(r,invoice.toString())) {
                   showalert("information", "Report deleted successfully", " invoice no:$invoice deleted from report:$r")

                   reporttable.selectionModel.clearSelection()
                   reporttablemake(r,reporttable,sl, dater, invoiceno, gstin, name, amount, sgsttax, cgsttax, sgstv, cgstv, totalamt)
               }
                else {
                   showalert("error","Cannot delete report","Please check invoice no")
               }

            }
            else
                showalert("error","No row selected","Please select a row to remove")
        }

         else {
            showalert("error", "Report name is required", "Please create a report first")
            reportname.requestFocus()


        }
    }
    fun showalert(type:String,title:String,contenttext:String){
      // alert.alertType=Alert.AlertType.

        when (type) {
            "Error".toLowerCase() -> {
                alert.alertType=Alert.AlertType.ERROR
            }
            "Warning".toLowerCase() ->{
                alert.alertType=Alert.AlertType.WARNING
            }
            "Information".toLowerCase() ->{
                alert.alertType=Alert.AlertType.INFORMATION
            }
            "Confirmation".toLowerCase() ->{
                alert.alertType=Alert.AlertType.CONFIRMATION
            }
          //  else-> alert.alertType=Alert.AlertType.NONE

        }

        alert.title=title
        alert.contentText=contenttext
        alert.showAndWait()
    }

    fun amountfieldtextchange(){
        amounttxtfield.textProperty().addListener{
            _, oldvalue, newValue ->
            when{
                newValue.isNullOrEmpty()->{
                    amounttxtfield.text=""
                    cgstvalue.text=""
                    sgstvalue.text=""
                    totalamount.text=""
                }
                rx.matches(newValue)->try {
                    val value=newValue.toDouble()
                    val gst=cgst.text.toDouble()
                    //  amounttxtfield.text=newValue
                    val gstv=(value*gst/100)
                    cgstvalue.text=gstv.toString()
                    sgstvalue.text=cgstvalue.text
                    globaltotal=(value+2*gstv).toBigDecimal()
                    totalamount.text=(value+2*gstv).toBigDecimal().toString()
                }catch (e:Exception){
                    println(e.localizedMessage)
                    amounttxtfield.text="0"
                }
                else->amounttxtfield.text=oldvalue
            }
        }

    }
    fun partyboxmake(partybox:JFXComboBox<String>){
        partybox.items=FXCollections.observableArrayList(db.partynames())
        when{
            partybox.items.isNullOrEmpty()-> {
                partybox.items = FXCollections.observableArrayList("No party added yet")
            }
        }
       partybox.selectionModel.selectFirst()

    }
    fun gstinboxmake() = when {
        partynamebox.selectionModel.selectedItem.isNullOrEmpty() -> {
            partynamebox.items=FXCollections.observableArrayList("No party added yet")
            partynamebox.selectionModel.selectFirst()
        }
        else -> {
            gstinbox.items = FXCollections.observableArrayList(db.getgstin(partynamebox.selectionModel.selectedItem))
            getgstvalue(partynamebox.selectionModel.selectedItem)
            when {
                gstinbox.items.isNullOrEmpty() -> {
                    gstinbox.items = FXCollections.observableArrayList("No GSTIN Added")
                    gstinbox.selectionModel.selectFirst()
                }
                else -> {
                    gstinbox.selectionModel.selectFirst()
                }
            }

        }
    }
    fun gstvaluechange(){
        val value=taxvaluebox.selectionModel.selectedItem
if(value.toString()!="No tax value is added") {
    when {
        value.isNullOrEmpty() -> {
            cgst.text = "0"
            sgst.text = "0"
        }
        else -> {
            cgst.text = (value.toDouble() / 2).toString()
            sgst.text = cgst.text
            calculategst()
        }
    }
}

    }
    fun calculategst(){
        val gst=cgst.text.toDouble()
        val amount=amounttxtfield.text
        when {
            amount.isNullOrEmpty() -> {
                amounttxtfield.text="0"
            }
            else -> {
                val gstv=(amount.toDouble()*gst/100)
                cgstvalue.text= (amount.toDouble()+gstv).toString()
                sgstvalue.text=cgstvalue.text
                totalamount.text=(amount.toDouble()+gstv*2).toString()
                globaltotal=(amount.toDouble()+gstv*2).toBigDecimal()
            }
        }

    }

    fun getgstvalue(name:String){
        println("gst value name is $name")
        val valuelist= db.partyvalues(name)
        taxvaluebox.items=FXCollections.observableArrayList(valuelist)
        when {
            taxvaluebox.items.isNullOrEmpty() -> {

                taxvaluebox.items = FXCollections.observableArrayList("No tax value is added")
                taxvaluebox.selectionModel.selectFirst()
            }
            else->
                taxvaluebox.selectionModel.selectFirst()
        }
    }
    fun roundtotal(){
        if (totalamount.text.isNullOrEmpty()){
            totalamount.text=""
        }
        else {

            when (roundbox.selectionModel.selectedItem) {
                "UP" -> {
                    totalamount.text = globaltotal.setScale(0, RoundingMode.CEILING).toString()
                }
                "DOWN"->{
                    totalamount.text =globaltotal.setScale(0, RoundingMode.FLOOR).toString()

                }

                else -> {
                    totalamount.text =globaltotal.toString()
                }
            }

        }

    }
    fun reset(){
        partynamebox.selectionModel.selectFirst()
        gstinbox.selectionModel.selectFirst()
        taxvaluebox.selectionModel.selectFirst()
        date.value= null
        invoicetxtfield.text=""
        amounttxtfield.text="0"
        cgstvalue.text="0"
        sgstvalue.text="0"
        totalamount.text="0"
    }
    fun printreport() {
        val rname = reportname.text
        val status = createreportstatus.text
        if (rname.isNullOrBlank()) {
            showalert("error", "Report name is required", "Please create report first")
            reportname.requestFocus()
        }
        else{
            if (status=="Report created successfully"){
                GlobalScope.launch {
                    withContext(Dispatchers.IO){db.reportprint(rname)}
                }
            }

            else{
              showalert("error", "Report creation failed", "Please create report first")
                reportname.requestFocus()

            }
        }

    }
}
