from abc import ABC
from course_section import CourseSection
from student import Student

class Course(ABC):
    
    def __init__(self, course_code, quota, credit, theoretical, practical):
        self.course_code = course_code
        self.quota = quota
        self.credit = credit
        self.theoretical = theoretical
        self.practical = practical
        self.course_section = None
        self.non_registered_collision = set()
        self.non_registered_quota = set()
        
        
    def is_elligible_past_course(self, student):
        stu_transcript = student.transcript
        return not stu_transcript.has_passed_course(self) ##If student hasn't passed this course yet
    
    
    def when_requested(self, student: Student):
        collided_sections = student.schedule.collided_hours(self.course_section)
        if student.schedule.is_collision(self.course_section):
            student.logging += "\nAdvisor didn't approve " + self.__str__() +  " because of more than one hour collision with "
            student.logging += [cs.course.__str__() + " " for cs in collided_sections]
            student.logging += "in schedule"
            self.non_registered_students.add(student)


    def total_hours(self):
        return self.theoretical + self.practical
    
    
    
    def __str__(self):
        return self.course_code
        
    
    