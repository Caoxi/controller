package org.opendaylight.controller.cluster.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationImplTest {

    private static ConfigurationImpl configuration;

    @BeforeClass
    public static void staticSetup(){
        configuration = new ConfigurationImpl("module-shards.conf", "modules.conf");
    }

    @Test
    public void testConstructor(){
        Assert.assertNotNull(configuration);
    }

    @Test
    public void testGetMemberShardNames(){
        List<String> memberShardNames =
            configuration.getMemberShardNames("member-1");

        assertTrue(memberShardNames.contains("people-1"));
        assertTrue(memberShardNames.contains("cars-1"));

        // Retrieve once again to hit cache

        memberShardNames =
            configuration.getMemberShardNames("member-1");

        assertTrue(memberShardNames.contains("people-1"));
        assertTrue(memberShardNames.contains("cars-1"));

    }

    @Test
    public void testGetMembersFromShardName(){
        List<String> members =
            configuration.getMembersFromShardName("default");

        assertEquals(3, members.size());

        assertTrue(members.contains("member-1"));
        assertTrue(members.contains("member-2"));
        assertTrue(members.contains("member-3"));

        assertFalse(members.contains("member-26"));

        // Retrieve once again to hit cache
        members =
            configuration.getMembersFromShardName("default");

        assertEquals(3, members.size());

        assertTrue(members.contains("member-1"));
        assertTrue(members.contains("member-2"));
        assertTrue(members.contains("member-3"));

        assertFalse(members.contains("member-26"));


        // Try to find a shard which is not present

        members =
            configuration.getMembersFromShardName("foobar");

        assertEquals(0, members.size());
    }

    @Test
    public void testReadConfigurationFromFile(){
        File f = new File("./module-shards.conf");
        ConfigFactory.parseFile(f);
    }

    @Test
    public void testGetAllShardNames(){
        Set<String> allShardNames = configuration.getAllShardNames();

        assertEquals(4, allShardNames.size());
        assertTrue(allShardNames.contains("default"));
        assertTrue(allShardNames.contains("people-1"));
        assertTrue(allShardNames.contains("cars-1"));
        assertTrue(allShardNames.contains("test-1"));
    }
}
