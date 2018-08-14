package io.ktor.experimental.client.redis

suspend fun Redis.clusterAddSlots(vararg slots: Long) = commandUnit("cluster", "addslots", *slots.toTypedArray())

suspend fun Redis.clusterCountFailureReports(nodeId: Int): Long =
    commandLong("cluster", "count-failure-reports", nodeId)

suspend fun Redis.clusterCountKeysInSlot(slot: Long): Long =
    commandLong("cluster", "countkeysinslot", slot)

suspend fun Redis.clusterDelSlots(vararg slots: Long) =
    commandUnit("cluster", "delslots", *slots.toTypedArray())

enum class RedisClusterFailOverMode { FORCE, TAKEOVER }

suspend fun Redis.clusterFailover(mode: RedisClusterFailOverMode = RedisClusterFailOverMode.FORCE) =
    commandUnit("cluster", "failover", mode.name)

suspend fun Redis.clusterForget(nodeId: Int): Unit =
    commandUnit("cluster", "forget", nodeId)

suspend fun Redis.clusterGetKeysInSlot(slot: Long, count: Int): List<String> =
    commandArrayString("cluster", "getkeysinslot", slot, count)


suspend fun Redis.clusterInfo(): String =
    commandString("cluster", "info") ?: ""

suspend fun Redis.clusterKeyslot(key: String): Long =
    commandLong("cluster", "keyslot", key)

suspend fun Redis.clusterMeet(ip: String, port: Int): Unit =
    commandUnit("cluster", "meet", ip, port)

suspend fun Redis.clusterNodes(): String? =
    commandString("cluster", "nodes")

suspend fun Redis.clusterReplicate(nodeId: Long): Unit =
    commandUnit("cluster", "replicate", nodeId)

suspend fun Redis.clusterReset(hard: Boolean = false): Unit =
    commandUnit("cluster", "reset", if (hard) "hard" else "soft")

suspend fun Redis.clusterSaveconfig(): Unit =
    commandUnit("cluster", "saveconfig")

internal suspend fun Redis.clusterSetConfigEpoch(todo: Any): Any = TODO()

internal suspend fun Redis.clusterSetSlot(todo: Any): Any = TODO()

suspend fun Redis.clusterSlaves(nodeId: Long): String? = commandString("cluster", "slaves", nodeId)

suspend fun Redis.clusterSlots(nodeId: Long): Any? = executeText("cluster", "slots")

suspend fun Redis.readonly(): Unit = commandUnit("readonly")

suspend fun Redis.readwrite(): Unit = commandUnit("readwrite")

