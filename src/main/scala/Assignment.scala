import scala.util.Using

case class Assignment(
                       assignmentId: String,
                       courseId: String,
                       studentId: String,
                       assignmentTitle: String,
                       assignmentScore: Option[Double],
                       submissionDate: String,
                       isSubmitted: Boolean
                     )

object Assignment {
  private val assignmentsFile = "C:\\Users\\user\\IdeaProjects\\School Management System\\files\\assignments.txt"

  def addAssignment(assignment: Assignment): Boolean = {
    val assignmentString = s"${assignment.assignmentId}|${assignment.courseId}|${assignment.studentId}|" +
      s"${assignment.assignmentTitle}|${assignment.assignmentScore.getOrElse("")}|" +
      s"${assignment.submissionDate}|${assignment.isSubmitted}\n"

    try {
      val writer = new java.io.BufferedWriter(new java.io.FileWriter(assignmentsFile, true))
      writer.write(assignmentString)
      writer.close()
      true
    } catch {
      case e: Exception =>
        println(s"Failed to add assignment: ${e.getMessage}")
        false
    }
  }

  def submitAssignment(assignmentId: String): Boolean = {
    val allAssignments = Using(scala.io.Source.fromFile(assignmentsFile)) { source =>
      source.getLines().toList
    }.getOrElse(List.empty[String])

    val updatedAssignments = allAssignments.map { line =>
      val fields = line.split("\\|")
      if (fields(0) == assignmentId) {
        val updatedLine = fields.updated(fields.length - 1, "true").mkString("|")
        updatedLine
      } else {
        line
      }
    }

    try {
      val writer = new java.io.BufferedWriter(new java.io.FileWriter(assignmentsFile))
      writer.write(updatedAssignments.mkString("\n"))
      writer.close()
      true
    } catch {
      case e: Exception =>
        println(s"Failed to submit assignment: ${e.getMessage}")
        false
    }
  }

  def getAssignmentsForStudent(studentId: String): List[Assignment] = {
    val allAssignments = Using(scala.io.Source.fromFile(assignmentsFile)) { source =>
      source.getLines().toList
    }.getOrElse(List.empty[String])

    allAssignments.flatMap { line =>
      val fields = line.split("\\|")
      if (fields(2) == studentId) {
        Some(
          Assignment(
            fields(0),
            fields(1),
            fields(2),
            fields(3),
            if (fields(4).isEmpty) None else Some(fields(4).toDouble),
            fields(5),
            fields(6).toBoolean
          )
        )
      } else {
        None
      }
    }
  }
}
