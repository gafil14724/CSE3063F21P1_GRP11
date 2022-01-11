import random


from grade import Grade

class Transcript(object):
    
    def __init__(self, student):
        self.student = student
        self.current_courses = []
        self.grades = []
    
    
    def get_completed_credits(self):
        passed_courses = self.get_passed_courses()
        return sum(course.credits for course in passed_courses)
    
    
    def get_passed_courses(self):    
        return [grade.course for grade in self.grades if grade.is_passed()]
    
    
    def has_passed_course(self, course):
        if course is None:
            return True
        return course in self.get_passed_courses()
    
    
    def has_passed_courses(self, courses):
        return all(self.has_passed_course(course) for course in courses)
        
        
    def add_passed_course(self, course):
        grade = random.randint(50, 100)
        self.grades.append(Grade(course, grade))
        
        
    def add_failed_course(self, course):
        grade = random.randint(0, 49)
        self.grades.append(Grade(course, grade))
        
        
        
    