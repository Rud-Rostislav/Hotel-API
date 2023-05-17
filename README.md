Посібник із використання Postman із API готелю:

Привітання:
http://localhost:8080/hotel

--------------------

     КІМНАТИ

--------------------

1. Додавання кімнати:
   Встановіть метод HTTP на POST.
   Встановіть URL:
   http://localhost:8080/hotel/rooms
   У тілі запиту виберіть raw опцію та встановіть для тіла запиту представлення JSON.
   Приклад запиту:

{
"number": "1",
"capacity": 1,
"occupied": false
}

2. Отримання всіх кімнат:
   Встановіть для методу HTTP значення GET.
   Встановіть URL:
   http://localhost:8080/hotel/rooms

3. Отримання однієї кімнати:
   Встановіть для методу HTTP значення GET.
   Встановіть URL
   http://localhost:8080/hotel/rooms/{id}
   Де {id} - порядковий номер кімнати

4. Оновлення кімнати:
   Встановіть метод HTTP на PUT.
   Встановіть URL:
   http://localhost:8080/hotel/rooms/{roomId}
   Замініть {roomId} на порядковий номер кімнати.
   У тілі запиту виберіть raw опцію та встановіть для тіла запиту представлення JSON.
   Приклад запиту:

{
"number": "2",
"capacity": 2,
"occupied": false
}

5. Видалення кімнати:
   Встановіть метод HTTP на DELETE.
   Встановіть URL:
   http://localhost:8080/hotel/rooms/{roomId}
   Замініть {roomId}на ідентифікатор кімнати, яку потрібно видалити.

6. Додавання відвідувача в кімнату:
   Встановіть метод HTTP на POST.
   Встановіть URL:
   http://localhost:8080/hotel/rooms/{roomId}/visitors
   Замініть {roomId}ідентифікатор кімнати, куди ви хочете додати відвідувача.
   У тілі запиту виберіть raw опцію та встановіть для тіла запиту JSON відвідувача, якого ви хочете додати.
   Приклад запиту:

{
"firstName": "John",
"lastName": "Doe",
"passportNumber": "AB123456",
"joinDate": "2023-05-17T10:00:00",
"leaveDate": "2023-05-20T12:00:00"
}



---------------------

     ВІДВІДУВАЧІ

---------------------

1. Переглянути всіх відвідувачів
   Встановіть метод HTTP на GET.
   Встановіть URL:
   http://localhost:8080/hotel/visitors


2. Оновлення інформації відвідувача:
   Встановіть метод HTTP на PUT.
   Встановіть URL:
   http://localhost:8080/hotel/visitors/{visitorId}
   Замініть {visitorId}на ідентифікатор
   відвідувача, якого
   потрібно оновити.
   У тілі запиту виберіть опцію raw та встановіть для тіла запиту JSON-представлення оновленого відвідувача.
   Приклад запиту:

{
"firstName": "John",
"lastName": "Morgan",
"passportNumber": "AB12345"
}

3. Пошук відвідувачів:
   Встановіть для методу HTTP значення GET.
   Для пошуку за прізвищем додайте lastName параметр запиту. Наприклад:
   http://localhost:8080/hotel/visitors?lastName=Doe
   Для пошуку за номером паспорта додайте passportNumber параметр запиту. Наприклад:
   http://localhost:8080/hotel/visitors?passportNumber=AB123456
   Можна поєднати обидва параметри запиту, щоб уточнити пошук. Наприклад:
   http://localhost:8080/hotel/visitors?lastName=Doe&passportNumber=AB123456

4. Видалити відвідувача:
   Встановіть метод HTTP на DELETE.
   Встановіть URL:
   http://localhost:8080/hotel/rooms/{roomId}/visitors/{visitorId}
   Замініть {roomId}на ідентифікатор кімнати, з якої потрібно видалити і {visitorId} номер відвідувача.

5. Перемістити відвідувача до іншої кімнати
   Встановіть метод HTTP на PUT
   Встановіть URL:
   http://localhost:8080/hotel/rooms/1/visitors/{visitorId}/move/2
   1 - кімната з якої треба пересилити, visitorId - id відвідувача, 2 - кімната, куди треба переселити