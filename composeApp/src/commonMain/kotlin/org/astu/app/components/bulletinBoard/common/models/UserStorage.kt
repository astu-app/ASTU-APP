package org.astu.app.components.bulletinBoard.common.models

import com.benasher44.uuid.uuid4

object UserStorage {
    val user1 = UserSummary(uuid4(), "Анисимова", "Анна", "Максимовна")
    val user2 = UserSummary(uuid4(), "Романова", "Милана", "Матвеевна")
    val user3 = UserSummary(uuid4(),"Фадеев", "Максим", "Михайлович")
    val user4 = UserSummary(uuid4(),"Покровская", "Анастасия", "Андреевна")
    val user5 = UserSummary(uuid4(),"Воробьева", "Софья", "Дмитриевна")
    val user6 = UserSummary(uuid4(),"Левин", "Тимофей", "Владимирович")
}