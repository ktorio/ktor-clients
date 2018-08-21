package io.ktor.experimental.client.redis

/**
 * Assign new hash slots to receiving node
 *
 * https://redis.io/commands/cluster-addslots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterAddSlots(vararg slots: Long): Unit =
    executeTyped("CLUSTER", "ADDSLOTS", *slots.toTypedArray())

/**
 * Return the number of failure reports active for a given node
 *
 * https://redis.io/commands/cluster-count-failure-reports
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterCountFailureReports(nodeId: Int): Long =
    executeTyped("CLUSTER", "COUNT-FAILURE-REPORTS", nodeId)

/**
 * Return the number of local keys in the specified hash slot
 *
 * https://redis.io/commands/cluster-countkeysinslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterCountKeysInSlot(slot: Long): Long =
    executeTyped("CLUSTER", "COUNTKEYSINSLOT", slot)

/**
 * Set hash slots as unbound in receiving node
 *
 * https://redis.io/commands/cluster-delslots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterDelSlots(vararg slots: Long): Unit =
    executeTyped("CLUSTER", "DELSLOTS", *slots.toTypedArray())

enum class RedisClusterFailOverMode { FORCE, TAKEOVER }

/**
 * Forces a slave to perform a manual failover of its master.
 *
 * https://redis.io/commands/cluster-failover
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterFailover(mode: RedisClusterFailOverMode = RedisClusterFailOverMode.FORCE): Unit =
    executeTyped("CLUSTER", "FAILOVER", mode.name)

/**
 * Remove a node from the nodes table
 *
 * https://redis.io/commands/cluster-forget
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterForget(nodeId: Int): Unit =
    executeTyped("CLUSTER", "FORGET", nodeId)

/**
 * Return local key names in the specified hash slot
 *
 * https://redis.io/commands/cluster-getkeysinslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterGetKeysInSlot(slot: Long, count: Int): List<String> =
    executeArrayString("CLUSTER", "GETKEYSINSLOT", slot, count)

/**
 * Provides info about Redis Cluster node state
 *
 * https://redis.io/commands/cluster-info
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterInfo(): String =
    executeTypedNull<String>("CLUSTER", "INFO") ?: ""

/**
 * Returns the hash slot of the specified key
 *
 * https://redis.io/commands/cluster-keyslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterKeyslot(key: String): Long =
    executeTyped("CLUSTER", "KEYSLOT", key)

/**
 * Force a node cluster to handshake with another node
 *
 * https://redis.io/commands/cluster-meet
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterMeet(ip: String, port: Int): Unit =
    executeTyped("CLUSTER", "MEET", ip, port)

/**
 * Get Cluster config for the node
 *
 * https://redis.io/commands/cluster-nodes
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterNodes(): String? =
    executeTypedNull<String>("CLUSTER", "NODES")

/**
 * Reconfigure a node as a slave of the specified master node
 *
 * https://redis.io/commands/cluster-replicate
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterReplicate(nodeId: Long): Unit =
    executeTyped("CLUSTER", "REPLICATE", nodeId)

/**
 * Reset a Redis Cluster node
 *
 * https://redis.io/commands/cluster-reset
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterReset(hard: Boolean = false): Unit =
    executeTyped("CLUSTER", "RESET", if (hard) "HARD" else "SOFT")

/**
 * Forces the node to save cluster state on disk
 *
 * https://redis.io/commands/cluster-saveconfig
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSaveconfig(): Unit =
    executeTyped("CLUSTER", "SAVECONFIG")

/**
 * Set the configuration epoch in a new node
 *
 * https://redis.io/commands/cluster-set-config-epoch
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSetConfigEpoch(epoch: Long): Unit =
    executeTyped("CLUSTER", "SET-CONFIG-EPOCH", epoch)

/**
 * Bind a hash slot to a specific node
 *
 * https://redis.io/commands/cluster-setslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSetSlotImporting(slot: Long, sourceNodeId: Long): Unit =
    executeTyped("CLUSTER", "SETSLOT", slot, "IMPORTING", sourceNodeId)

/**
 * Bind a hash slot to a specific node
 *
 * https://redis.io/commands/cluster-setslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSetSlotMigrating(slot: Long, destNodeId: Long): Unit =
    executeTyped("CLUSTER", "SETSLOT", slot, "MIGRATING", destNodeId)

/**
 * Bind a hash slot to a specific node
 *
 * https://redis.io/commands/cluster-setslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSetSlotStable(slot: Long): Unit =
    executeTyped("CLUSTER", "SETSLOT", slot, "STABLE")

/**
 * Bind a hash slot to a specific node
 *
 * https://redis.io/commands/cluster-setslot
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSetSlotNode(slot: Long, nodeId: Long): Unit =
    executeTyped("CLUSTER", "SETSLOT", slot, "NODE", nodeId)

/**
 * List slave nodes of the specified master node
 *
 * https://redis.io/commands/cluster-slaves
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSlaves(nodeId: Long): String? =
    executeTypedNull<String>("CLUSTER", "SLAVES", nodeId)

/**
 * Get array of Cluster slot to node mappings
 *
 * https://redis.io/commands/cluster-slots
 *
 * @since 3.0.0
 */
suspend fun Redis.clusterSlots(nodeId: Long): Any? =
    executeText("CLUSTER", "SLOTS")

/**
 * Enables read queries for a connection to a cluster slave node
 *
 * https://redis.io/commands/readonly
 *
 * @since 3.0.0
 */
suspend fun Redis.readonly(): Unit =
    executeTyped("READONLY")

/**
 * Disables read queries for a connection to a cluster slave node
 *
 * https://redis.io/commands/readwrite
 *
 * @since 3.0.0
 */
suspend fun Redis.readwrite(): Unit =
    executeTyped("READWRITE")

