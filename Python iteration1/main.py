from grade import *
from registration_system import RegistrationSystem


grade = Grade("qqq", 89)
grade.vat = "vatof grade"

grade2 = Grade("qqq", 45)
grade2.amanin = (1, 2, "3", 3)


print(grade.letter_grade())
print(grade2.letter_grade())

regsys = RegistrationSystem()



