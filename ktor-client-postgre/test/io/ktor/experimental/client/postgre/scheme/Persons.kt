package io.ktor.experimental.client.postgre.scheme

import io.ktor.experimental.client.sql.*

internal suspend fun SqlClient.createPersons(): SqlQueryResult = execute(
    """
    CREATE TABLE IF NOT EXISTS Persons (
        PersonID int,
        LastName varchar(255),
        FirstName varchar(255),
        Address varchar(255),
        City varchar(255)
    );
    """.trimIndent()
)

internal suspend fun SqlClient.dropPersons(): SqlQueryResult = execute("DROP TABLE IF EXISTS Persons;")

internal suspend fun SqlClient.addPerson(
    id: Int,
    lastName: String, firstName: String,
    address: String, city: String
): SqlQueryResult = execute("INSERT INTO Persons VALUES($id, '$lastName', '$firstName', '$address', '$city');")

internal suspend fun SqlClient.addFakePersons(count: Int) = repeat(count) {
    addPerson(
        it,
        "fake-lastname-$it", "fake-firstname-$it",
        "fake-address-$it", "fake-city-$it"
    )
}

