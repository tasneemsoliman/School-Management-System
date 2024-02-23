import java.security.MessageDigest

class User(val username: String, private var passwordHash: String, val role: String) {
  def authenticate(enteredPassword: String): Boolean = {
    val enteredHash = User.hashPassword(enteredPassword)
    enteredHash == passwordHash
  }

  def setPassword(newPassword: String): Unit = {
    passwordHash = User.hashPassword(newPassword)
    updateUserFile()
  }

  private def updateUserFile(): Unit = {
    val usersFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\users.txt"
    val lines = scala.io.Source.fromFile(usersFile).getLines().toSeq
    val updatedLines = lines.map { line =>
      val Array(existingUsername, existingPasswordHash, existingRole) = line.split("\\|")
      if (existingUsername == username) {
        s"$username|$passwordHash|$role"
      } else {
        line
      }
    }

    val writer = new java.io.BufferedWriter(new java.io.FileWriter(usersFile))
    try {
      updatedLines.foreach(line => writer.write(s"$line\n"))
    } finally {
      writer.close()
    }
  }
}

object User {
  def hashPassword(password: String): String = {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(password.getBytes("UTF-8"))
    digest.map("%02x".format(_)).mkString
  }
}
