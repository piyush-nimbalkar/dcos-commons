package com.mesosphere.sdk.zookeeper.scheduler;

import com.google.common.base.Joiner;
import com.mesosphere.sdk.scheduler.DefaultScheduler;
import com.mesosphere.sdk.scheduler.SchedulerBuilder;
import com.mesosphere.sdk.scheduler.SchedulerConfig;
import com.mesosphere.sdk.scheduler.SchedulerRunner;
import com.mesosphere.sdk.specification.DefaultServiceSpec;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;

import java.io.File;
import java.util.*;

/**
 * Zookeeper service.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected one file argument, got: " + Arrays.toString(args));
        }
        SchedulerRunner
                .fromSchedulerBuilder(createSchedulerBuilder(new File(args[0])))
                .run();
    }

    private static SchedulerBuilder createSchedulerBuilder(File yamlSpecFile)
            throws Exception {
        SchedulerConfig schedulerConfig = SchedulerConfig.fromEnv();
        RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(yamlSpecFile).build();
        List<String> zkNodes = ZookeeperConfigUtils.getZookeeperNodes(rawServiceSpec.getName());
        SchedulerBuilder schedulerBuilder = DefaultScheduler.newBuilder(
                DefaultServiceSpec
                        .newGenerator(rawServiceSpec, schedulerConfig, yamlSpecFile.getParentFile())
                        .setAllPodsEnv("ZOOKEEPER_SERVERS", Joiner.on('\n').join(zkNodes))
                        .build(),
                schedulerConfig)
                .setPlansFrom(rawServiceSpec);

        return schedulerBuilder;
    }
}
