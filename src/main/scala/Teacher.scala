class Teacher(username: String, passwordHash: String, role: String, val teacherId: String, val courses: List[String])
  extends User(username, passwordHash, role) {

  override def toString: String = {
    val subjectsInfo = courses.mkString(", ")
    s"${super.toString}, Teacher ID: $teacherId, Subjects Taught: $subjectsInfo"
  }
}
