package io.ktor.experimental.client.postgre

import ch.qos.logback.classic.*
import com.palantir.docker.compose.*
import com.palantir.docker.compose.configuration.*
import com.palantir.docker.compose.connection.waiting.*
import io.ktor.experimental.client.postgre.protocol.*
import io.ktor.experimental.client.postgre.scheme.*
import io.ktor.experimental.client.sql.*
import kotlinx.coroutines.*
import org.junit.*
import org.junit.Test
import org.junit.rules.*
import org.slf4j.*
import org.slf4j.Logger
import java.net.*
import java.util.concurrent.*
import kotlin.test.*

class IntegrationTest {

    @Rule
    @JvmField
    val timeout = Timeout(10, TimeUnit.HOURS)

    @Test
    fun simpleRequestTest() = postgreTest(address) {
        createPersons()

        repeat(10) { id ->
            addPerson(id, "lastName-$id", "firstName-$id", "address-$id", "city-$id")
        }


        val persons = select("SELECT * from Persons; SELECT * from Persons;")

        for (table in persons) {
            println("TABLE: $table")
            for (row in table) {
                println("${row["PersonID"].asString()}: ${row["LastName"].asString()}")
            }
        }

        dropPersons()
    }

    @Test
    fun createDatabaseTest() = postgreTest(address) {
        execute("CREATE DATABASE goods;")
        execute("CREATE DATABASE orders;")

        assertFailsWith<PostgreException> {
            runBlocking { execute("CREATE DATABASE goods;") }
        }

        execute("DROP DATABASE goods;")
        assertFailsWith<PostgreException> {
            runBlocking { execute("DROP DATABASE goods;") }
        }

        execute("CREATE DATABASE goods;")

        assertFailsWith<PostgreException> {
            runBlocking { execute("CREATE DATABASE goods; CREATE DATABASE stores;") }
        }

        execute("DROP DATABASE orders;")
        execute("DROP DATABASE goods;")
    }

//    @Test
    fun prepareQueryTest() = postgreTest(address) {
        createPersons()

        connection {
            val statement = prepare("INSERT INTO Persons VALUES ($1, $2, $3, $4, $5);")

            statement.execute("1", "hello-2", "hello-3", "hello-4", "hello-5")

            val persons = select("SELECT * FROM Persons;")
            var count = 0
            for (table in persons) {
                for (person in table) {
                    count++

                    assertEquals("1", person["Id"].asString())
                    assertEquals("hello-2", person["LastName"].asString())
                    assertEquals("hello-3", person["FirstName"].asString())
                    assertEquals("hello-4", person["Address"].asString())
                    assertEquals("hello-5", person["City"].asString())
                }
            }

            assertEquals(1, count)
        }
    }

    @Test
    fun createTableTest() = postgreTest(address) {
        createPersons()

        addFakePersons(10)
        addFakePersons(10)
    }

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
