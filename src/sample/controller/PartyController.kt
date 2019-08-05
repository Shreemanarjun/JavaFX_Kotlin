package sample.controller

import com.jfoenix.controls.*
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea
import sample.partynames
import java.net.URL
import java.util.*


class PartyController :Initializable{
    @FXML
    lateinit var partynamefield:JFXTextField
    lateinit var gstinfield:JFXTextField
    lateinit var gstvaluefield:JFXTextField
    lateinit var partybox:JFXComboBox<String>
   lateinit var addtextarea:TextArea
    //lateinit var resultext1:JFXTextArea

    override fun initialize(location: URL?, resources: ResourceBundle?) {
            partybox.items =  FXCollections.observableArrayList(partynames())
            when {
                partybox.items.isNullOrEmpty() -> {
                    partybox.items=FXCollections.observableArrayList("No party added yet")
                }
                else -> {
                    partybox.selectionModel.selectFirst()
                }
            }
    }

   fun addparty(evt:ActionEvent){

        var partyname=partynamefield.text
        var gstin=gstinfield.text
        var gst=gstvaluefield.text

        when {
            partyname.isEmpty() -> partynamefield.requestFocus()
            gstin.isEmpty() || gstin.length != 15 -> gstinfield.requestFocus()
            gst.isEmpty() || gst.length != 2 -> gstvaluefield.requestFocus()
            gstin.length == 15 && gst.length == 2 -> {
                addtextarea.text = "Party name:- $partyname \n GSTIN:- $gstin \n GST:- $gst \n added successfully "
                addtextarea.style = "-fx-background-color:white; -fx-text-fill:GREEN;"
            }
            else -> {
                addtextarea.text = "Error in adding party.please check further"
                addtextarea.style ="-fx-background-color:white; -fx-text-fill:Red;"
            }

        }
        }
    }

    fun remove(){
        //resultext1.text="${partybox.value} is removed"
       // resultext1.style="-fx-background-color:white; -fx-text-fill:GREEN;"
    }



