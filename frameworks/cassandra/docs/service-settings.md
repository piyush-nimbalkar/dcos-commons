---
layout: layout.pug
navigationTitle:
excerpt:
title: Service Settings
menuWeight: 21
---

# Service Name

You must configure each instance of DC/OS Apache Cassandra in a given DC/OS cluster with a different service name. You can configure the service name in the **service** section of the advanced installation section of the DC/OS web interface. The default service name (used in many examples here) is `cassandra`.

*   **In DC/OS CLI options.json**: `name`: string (default: `cassandra`)
*   **DC/OS web interface**: The service name cannot be changed after the cluster has started.

# Data Center

You can configure the name of the logical data center that this Cassandra cluster runs in. This sets the `dc` property in the `cassandra-rackdc.properties` file.

*   **In DC/OS CLI options.json**: `data_center`: string (default: `dc1`)
*   **DC/OS web interface**: `CASSANDRA_LOCATION_DATA_CENTER`: `string`

# Rack

You can configure the name of the rack that this Cassandra cluster runs in. This sets the `rack` property in the `cassandra-rackdc.properties` file.

*   **In DC/OS CLI options.json**: `rack`: string (default: `rac1`)
*   **DC/OS web interface**: `CASSANDRA_LOCATION_RACK`: `string`

# Remote Seeds

You can configure the remote seeds from another Cassandra cluster that this cluster should communicate with to establish cross-data-center replication. This should be a comma-separated list of node hostnames, such as `node-0-server.cassandra.autoip.dcos.thisdcos.directory,node-1-server.cassandra.autoip.dcos.thisdcos.directory`. For more information on multi-data-center configuration, see [Configuring Multi-data-center Deployments](#configuring-multi-data-center-deployments).

*   **In DC/OS CLI options.json**: `remote_seeds`: string (default: `""`)
*   **DC/OS web interface**: `TASKCFG_ALL_REMOTE_SEEDS`: `string`

# Backup/Restore Strategy

You can configure whether the creation, transfer, and restoration of backups occurs in serial or in parallel across nodes. This option must be set to either `serial` or `parallel`. Running backups and restores in parallel has the potential to saturate your network. For this reason, we recommend that you use the default configuration for backup strategy.

*   **In DC/OS CLI options.json**: `backup_restore_strategy`: string (default: `"serial"`)
*   **DC/OS web interface**: `BACKUP_RESTORE_STRATEGY`: `string`

# Virtual networks

<!-- Note: This partially duplicates the Virtual Networks section of the common install.md template -->
The Cassandra service can be run on a virtual network such as the DC/OS overlay network, affording each node its own IP address (IP per container). For details about virtual networks on DC/OS see the [documentation](/latest/networking/virtual-networks/#virtual-network-service-dns). For the Cassandra service, using a virtual network means that nodes no longer use reserved port resources on the Mesos agents.  This allows nodes to share machines with other applications that may need to use the same ports that Cassandra does. That means, however, that we cannot guarantee that the ports on the agents containing the reserved resources for Cassandra will be available, therefore we do not allow a service to change from a virtual network to the host network. **Once the service is deployed on a virtual network it must remain on that virtual network**. The only way to move your data to Cassandra on the host network is through a migration.

# Zones

Placement constraints can be applied to zones by referring to the `@zone` key. For example, one could spread pods across a minimum of 3 different zones by specifying the constraint `[["@zone", "GROUP_BY", "3"]]`.

<!--
When the region awareness feature is enabled (currently in beta), the `@region` key can also be referenced for defining placement constraints. Any placement constraints that do not reference the `@region` key are constrained to the local region.
-->
## Example

Suppose we have a Mesos cluster with zones `a`,`b`,`c`.

## Balanced Placement for a Single Region

```
{
  ...
  "count": 6,
  "placement_constraint": "[[\"@zone\", \"GROUP_BY\", \"3\"]]"
}
```

- Instances will all be evenly divided between zones `a`,`b`,`c`.
