package io.ktor.experimental.client.postgre

import com.palantir.docker.compose.*
import com.palantir.docker.compose.connection.waiting.*
import org.junit.*
import java.net.*


class IntegrationTest {

    @Test
    fun createTableTest() = postgreTest(address) {
        query(
            "CREATE TABLE Persons (\n" +
                    "    PersonID int,\n" +
                    "    LastName varchar(255),\n" +
                    "    FirstName varchar(255),\n" +
                    "    Address varchar(255),\n" +
                    "    City varchar(255) \n" +
                    ");"
        )
    }

    companion object {
        val POSTGRE_SERVICE = "postgre"
        val POSTGRE_PORT = 5432
        val POSTGRE_PASSWORD = "hello"

        lateinit var address: InetSocketAddress

        @JvmField
        @ClassRule
        val docker = DockerComposeRule.builder()
            .file("resources/compose-postgre.yml")
            .waitingForService("postgre", HealthChecks.toHaveAllPortsOpen())
            .build()!!

        @BeforeClass
        @JvmStatic
        fun init() {
            val postgre = docker
                .containers()
                .container(POSTGRE_SERVICE)
                .port(POSTGRE_PORT)!!

            address = InetSocketAddress(postgre.ip, postgre.externalPort)
        }
    }
}
