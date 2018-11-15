package io.ktor.experimental.client.postgre

import ch.qos.logback.classic.*
import ch.qos.logback.classic.spi.*
import ch.qos.logback.core.*
import com.palantir.docker.compose.*
import com.palantir.docker.compose.configuration.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.sql.*
import org.junit.*
import org.junit.rules.*
import org.slf4j.*
import org.slf4j.Logger
import java.net.*
import java.util.concurrent.*

class IntegrationTest {

    @Rule
    @JvmField
    val timeout = Timeout(5, TimeUnit.SECONDS)

    @Test
    fun simpleRequestTest() = postgreTest(address) {
        createPersons()

        repeat(10) { id ->
            addPerson(id, "lastName-$id", "firstName-$id", "address-$id", "city-$id")
        }

        val persons = query("SELECT * from Persons; SELECT * from Persons;")

        for (table in persons) {
            println("TABLE: $table")

            for (row in table) {
                println("${row["PersonID"].asString()}: ${row["LastName"].asString()}")
            }
        }
    }

    private suspend fun SqlClient.addPerson(
        id: Int,
        lastName: String, firstName: String,
        address: String, city: String
    ) {
        query("INSERT INTO Persons VALUES($id, '$lastName', '$firstName', '$address', '$city');")
    }

    private suspend fun SqlClient.createPersons() = query(
        "CREATE TABLE IF NOT EXISTS Persons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255),\n" +
                "    FirstName varchar(255),\n" +
                "    Address varchar(255),\n" +
                "    City varchar(255) \n" +
                ");"
    )

    companion object {
        val POSTGRE_SERVICE = "postgre"
        val POSTGRE_PORT = 5432
        val POSTGRE_PASSWORD = "hello"

        lateinit var address: InetSocketAddress

        @JvmField
        @ClassRule
        val dockerCompose = DockerComposeRule.builder()
            .file("resources/compose-postgre.yml")
            .waitingForService("postgre", HealthChecks.toHaveAllPortsOpen())
            .shutdownStrategy(ShutdownStrategy.GRACEFUL)
            .build()!!

        @BeforeClass
        @JvmStatic
        fun init() {
            (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger).apply {
                level = Level.ALL
            }

            val postgre = dockerCompose
                .containers()
                .container(POSTGRE_SERVICE)
                .port(POSTGRE_PORT)!!

            address = InetSocketAddress(postgre.ip, postgre.externalPort)
        }

        @AfterClass
        @JvmStatic
        fun cleanup() {
            dockerCompose.containers().container(POSTGRE_SERVICE).stop()
        }
    }

}
