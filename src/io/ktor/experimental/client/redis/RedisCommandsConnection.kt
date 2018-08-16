package io.ktor.experimental.client.redis

/**
 * Authenticate to the server
 *
 * https://redis.io/commands/auth
 *
 * @since 1.0.0
 */
suspend fun Redis.auth(password: String) = commandString("auth", password)

/**
 * Echo the given string
 *
 * https://redis.io/commands/echo
 *
 * @since 1.0.0
 */
suspend fun Redis.echo(msg: String) = commandString("echo", msg)

/**
 * Ping the server
 *
 * https://redis.io/commands/ping
 *
 * @since 1.0.0
 */
suspend fun Redis.ping(msg: String) = commandString("ping", msg)

/**
 * Ping the server
 *
 * https://redis.io/commands/ping
 *
 * @since 1.0.0
 */
suspend fun Redis.ping(): String? = commandString("ping")

/**
 * Change the selected database for the current connection
 *
 * https://redis.io/commands/select
 *
 * @since 1.0.0
 */
suspend fun Redis.select(db: Int): Redis = this.apply { commandString("select", db) }

/**
 * This command swaps two Redis databases, so that immediately all the clients connected
 * to a given database will see the data of the other database, and the other way around.
 *
 * https://redis.io/commands/swapdb
 *
 * @since 4.0.0
 */
suspend fun Redis.swapdb(db1: Int, db2: Int) = this.apply { commandString("swapdb", db1, db2) }

/**
 * Close the connection
 *
 * https://redis.io/commands/quit
 *
 * @since 1.0.0
 */
suspend fun Redis.quit() = commandUnit("quit")
