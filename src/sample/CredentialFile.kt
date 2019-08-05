package sample

import java.io.*


class CredentialFile {

    fun getCredentials() :MutableList<String>{
        var credential=MutableList<String>(3){""}
        try {
            credential=File("cred.txt").bufferedReader().useLines { it.toMutableList() }
              return credential
        } catch (e: Exception) {
            print(e)
          return credential
        }

    }
    fun geturl():String{
        return getCredentials()[0]
    }
    fun getuser():String{
        return  getCredentials()[1]
    }
    fun getpassword():String{
        return getCredentials()[2]
    }


    fun SetCredential(url: String, user: String, pass: String): Boolean {
        try {
            File("cred.txt").writeText("$url\n$user\n$pass")
            return true
        } catch (e: Exception) {
            print(e)
            return false
        }

    }

}


fun main(){
 var cf=CredentialFile()
   println(cf.SetCredential("myurl","user","pass"))
  println("${cf.geturl()}\n${cf.getuser()}\n${cf.getpassword().trim()}")
}