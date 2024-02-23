import scala.collection.mutable.Map
import scala.util.Using


object UserManager {

  def loadUsersFromFile(): Map[String, User] = {
    val usersFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\users.txt"

    Using(scala.io.Source.fromFile(usersFile)) { source =>
      val loadedUsers = Map[String, User]()
      val lines = source.getLines()

      lines.foreach { line =>
        println(s"Processing line: $line")
        val Array(username, passwordHash, role, id, courses, gradesOrEmpty) = line.split("\\|")
        val user = role match {
          case "Student" =>
            val coursesList = courses.split(",").toList
            val gradesMap = gradesOrEmpty.split(",").map { pair =>
              val Array(course, grade) = pair.split(":")
              course -> grade.toDouble
            }.toMap
            new Student(username, passwordHash, role, id, coursesList, gradesMap)
          case "Teacher" =>
            val subjectsList = courses.split(",").toList
            new Teacher(username, passwordHash, role, id, subjectsList)
          case _ => new User(username, passwordHash, role)
        }
        loadedUsers.put(username, user)
      }
      loadedUsers
    }.getOrElse {
      println("Failed to read users from file.")
      Map.empty[String, User]
    }
  }

  def registerUser(username: String, password: String, role: String, id: String = "", courses: String = "", gradesOrEmpty: String = ""): Boolean = {
    val usersFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\users.txt"
    val lines = scala.io.Source.fromFile(usersFile).getLines()
    val userExists = lines.exists(_.startsWith(s"$username|"))

    if (!userExists) {
      val writer = new java.io.BufferedWriter(new java.io.FileWriter(usersFile, true))
      try {
        val passwordHash = User.hashPassword(password)
        val userData = role match {
          case "Student" => s"$username|$passwordHash|$role|$id|$courses|$gradesOrEmpty\n"
          case "Teacher" => s"$username|$passwordHash|$role|$id|$courses\n"
          case _ => s"$username|$passwordHash|$role\n"
        }
        writer.write(userData)
        true
      } finally {
        writer.close()
      }
    } else {
      false
    }
  }

  def loginUser(username: String, password: String): Boolean = {
    val usersFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\users.txt"

    // Read the file content and find the user entry
    val userEntry = scala.io.Source.fromFile(usersFile).getLines().find(_.startsWith(s"$username|"))

    userEntry match {
      case Some(userData) =>
        // Extract stored password hash and compare it with the entered password hash
        val Array(_, storedPasswordHash, _, _, _, _) = userData.split("\\|")
        val enteredPasswordHash = User.hashPassword(password)
        enteredPasswordHash == storedPasswordHash

      case None => // Username not found
        false
    }
  }
}
