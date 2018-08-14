package io.ktor.experimental.client.redis

/**
 * Assign new hash slots to receiving node
 *
 * https://redis.io/commands/cluster-addslots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterAddSlots(vararg slots: Long) = commandUnit("cluster", "addslots", *slots.toTypedArray())

/**
 * Return the number of failure reports active for a given node
 *
 * https://redis.io/commands/cluster-count-failure-reports
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterCountFailureReports(nodeId: Int): Long =
    commandLong("cluster", "count-failure-reports", nodeId)

/**
 * Return the number of local keys in the specified hash slot
 *
 * https://redis.io/commands/cluster-countkeysinslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterCountKeysInSlot(slot: Long): Long =
    commandLong("cluster", "countkeysinslot", slot)

/**
 * Set hash slots as unbound in receiving node
 *
 * https://redis.io/commands/cluster-delslots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterDelSlots(vararg slots: Long) =
    commandUnit("cluster", "delslots", *slots.toTypedArray())

enum class RedisClusterFailOverMode { FORCE, TAKEOVER }

/**
 * Forces a slave to perform a manual failover of its master.
 *
 * https://redis.io/commands/cluster-failover
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterFailover(mode: RedisClusterFailOverMode = RedisClusterFailOverMode.FORCE) =
    commandUnit("cluster", "failover", mode.name)

/**
 * Remove a node from the nodes table
 *
 * https://redis.io/commands/cluster-forget
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterForget(nodeId: Int): Unit =
    commandUnit("cluster", "forget", nodeId)

/**
 * Return local key names in the specified hash slot
 *
 * https://redis.io/commands/cluster-getkeysinslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterGetKeysInSlot(slot: Long, count: Int): List<String> =
    commandArrayString("cluster", "getkeysinslot", slot, count)

/**
 * Provides info about Redis Cluster node state
 *
 * https://redis.io/commands/cluster-info
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterInfo(): String =
    commandString("cluster", "info") ?: ""

/**
 * Returns the hash slot of the specified key
 *
 * https://redis.io/commands/cluster-keyslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterKeyslot(key: String): Long =
    commandLong("cluster", "keyslot", key)

/**
 * Force a node cluster to handshake with another node
 *
 * https://redis.io/commands/cluster-meet
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterMeet(ip: String, port: Int): Unit =
    commandUnit("cluster", "meet", ip, port)

/**
 * Get Cluster config for the node
 *
 * https://redis.io/commands/cluster-nodes
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterNodes(): String? =
    commandString("cluster", "nodes")

/**
 * Reconfigure a node as a slave of the specified master node
 *
 * https://redis.io/commands/cluster-replicate
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterReplicate(nodeId: Long): Unit =
    commandUnit("cluster", "replicate", nodeId)

/**
 * Reset a Redis Cluster node
 *
 * https://redis.io/commands/cluster-reset
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterReset(hard: Boolean = false): Unit =
    commandUnit("cluster", "reset", if (hard) "hard" else "soft")

/**
 * Forces the node to save cluster state on disk
 *
 * https://redis.io/commands/cluster-saveconfig
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSaveconfig(): Unit =
    commandUnit("cluster", "saveconfig")

/**
 * Set the configuration epoch in a new node
 *
 * https://redis.io/commands/cluster-set-config-epoch
 *
 * @since 3.0.0
 */
internal suspend fun Redis.clusterSetConfigEpoch(todo: Any): Any = TODO()

/**
 * Bind a hash slot to a specific node
 *
 * https://redis.io/commands/cluster-setslot
 *
 * @since 3.0.0
 */
internal suspend fun Redis.clusterSetSlot(todo: Any): Any = TODO()

/**
 * List slave nodes of the specified master node
 *
 * https://redis.io/commands/cluster-slaves
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSlaves(nodeId: Long): String? = commandString("cluster", "slaves", nodeId)

/**
 * Get array of Cluster slot to node mappings
 *
 * https://redis.io/commands/cluster-slots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSlots(nodeId: Long): Any? = executeText("cluster", "slots")

/**
 * Enables read queries for a connection to a cluster slave node
 *
 * https://redis.io/commands/readonly
 *
 * @since 3.0.0
 */
suspend fun Redis.readonly(): Unit = commandUnit("readonly")

/**
 * Disables read queries for a connection to a cluster slave node
 *
 * https://redis.io/commands/readwrite
 *
 * @since 3.0.0
 */
suspend fun Redis.readwrite(): Unit = commandUnit("readwrite")

