package sample


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

lateinit var con:Connection
lateinit var rs:ResultSet
var cf=CredentialFile()
 fun connect(url:String="jdbc:mysql://localhost:3306/gst",user:String="root",pass:String="password"):Boolean{
    try{
        Class.forName("com.mysql.cj.jdbc.Driver")

       con=DriverManager.getConnection(url,user,pass)
        return true

    }catch (e:Exception){
       println(e)

    }
    return false


}
data class Party(var Name:String,var GSTIN:String,var GSTValue:String)
 suspend fun viewparty():ArrayList<Party>{
    var sql="Select * from PARTY"
     var partyinfo=ArrayList<Party>()
    try{
         connect()
        var st=con.createStatement()
        rs=st.executeQuery(sql)
        while (rs.next()){
            partyinfo.add(Party( rs.getString("Name"),rs.getString("GSTIN"),rs.getString("GSTVALUE")))
        }
        return partyinfo
    }catch (e:Exception){
        println(e.localizedMessage+"viewparty error")
    }
    return partyinfo
}
 fun partynames():ArrayList<String> {
   var sql="Select `Name` from PARTY"
    var partyname=ArrayList<String>()
    try {
        connect()
        var st=con.createStatement()
        rs= st.executeQuery(sql)
        while (rs.next()){
            partyname.add(rs.getString("Name"))
        }
        return partyname

    }catch (e:Exception){
        println(e.localizedMessage+"partyname error")
    }
    return partyname
}


   fun main()= runBlocking{
     var start=System.currentTimeMillis()
  // println(connect("jdbc:mysql://localhost:3306/gst","root","password"))
           var pt= withContext(Dispatchers.IO) { viewparty()}

       for (i in pt){
             println(i)
   }
    //viewparty().listIterator().iterator().forEach { println(it.Name) }
       //println(partynames())
   var end=System.currentTimeMillis()
  println("This function takes ${end-start}ms")

}
