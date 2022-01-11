from student_id import StudentId
from transcript import Transcript
from schedule import Schedule


class Student(object):
    
    
    def __init__(self, name, current_year, registration_order, registration_system):
        self.name = name
        self.current_year = current_year
        self.registration_order = registration_order
        self.registration_system = registration_system
        self.transcript = Transcript()
        self.student_id = StudentId(self)
        self.semester_num = 0
        self.logging = ""
        self.schedule = Schedule(self)
        
        
        
        
