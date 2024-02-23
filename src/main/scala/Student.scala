class Student(username: String, passwordHash: String, role: String, val studentId: String, val courses: List[String], val grades: Map[String, Double])
  extends User(username, passwordHash, role) {

  def this() = this("user","1234","role","S000",List.empty[String],Map.empty[String, Double])

  override def toString: String = {
    val coursesInfo = courses.mkString(", ")
    val gradesInfo = grades.map { case (courses, grade) => s"$courses: $grade" }.mkString(", ")
    s"${super.toString}, Student ID: $studentId, Courses: $coursesInfo, Grades: {$gradesInfo}"
  }
}
